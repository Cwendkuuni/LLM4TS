
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class PhaseTestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(PhaseTest.class);
        
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        
        if (result.wasSuccessful()) {
            System.out.println("All tests passed successfully!");
        } else {
            System.out.println("Some tests failed!");
        }
    }
}
