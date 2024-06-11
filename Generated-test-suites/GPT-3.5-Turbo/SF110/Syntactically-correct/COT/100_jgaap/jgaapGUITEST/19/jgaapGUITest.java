
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class jgaapGUITestRunner {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(jgaapGUITest.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("Tests succeeded: " + result.wasSuccessful());
	}
}