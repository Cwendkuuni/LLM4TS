
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class WeightTestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(WeightTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        if (result.wasSuccessful()) {
            System.out.println("All tests passed");
        }
    }
}