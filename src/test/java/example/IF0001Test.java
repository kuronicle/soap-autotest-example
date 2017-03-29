package example;

import static org.junit.Assert.*;

import org.junit.Test;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockService;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestRunner.Status;
import com.eviware.soapui.model.testsuite.TestSuite;

public class IF0001Test {

	private static final String TEST_DATA_ROOT_DIR = "src/test/resources/example/IF0001/";

	@Test
	public void UT0001() throws Exception {
		WsdlProject project = new WsdlProject(TEST_DATA_ROOT_DIR + "IF0001-soap-ui-project.xml");
		TestSuite testSuite = project.getTestSuiteByName("TestSuite");
		TestCase testCaseServer = testSuite.getTestCaseByName("TestCase0001-server");
		TestCase testCaseClient = testSuite.getTestCaseByName("TestCase0001-client");

		TestRunner runnerServer = testCaseServer.run(new PropertiesMap(), true);

		while (runnerServer.getStatus().equals(Status.INITIALIZED)) {
			System.out.println("wait initializing server.");
			Thread.sleep(1000);
		}

		TestRunner runnerClient = testCaseClient.run(new PropertiesMap(), false);

		System.out.println(runnerServer.getReason());

		assertEquals(Status.FINISHED, runnerServer.getStatus());
		assertEquals(Status.FINISHED, runnerClient.getStatus());
	}

	@Test
	public void UT0002() throws Exception {
		WsdlProject project = new WsdlProject(TEST_DATA_ROOT_DIR + "IF0001-soap-ui-project.xml");
		TestSuite testSuite = project.getTestSuiteByName("TestSuite");
		TestCase testCaseClient = testSuite.getTestCaseByName("TestCase0001-client");

		WsdlMockService mockService = project.getMockServiceByName("SampleServiceSoapBinding MockService");
		mockService.setPort(8089);
		mockService.setPath("/tc0001");
		mockService.start();

		TestRunner runnerServer = testCaseClient.run(new PropertiesMap(), false);

		System.out.println(runnerServer.getReason());

		assertEquals(Status.FINISHED, runnerServer.getStatus());

		mockService.release();
	}

}
