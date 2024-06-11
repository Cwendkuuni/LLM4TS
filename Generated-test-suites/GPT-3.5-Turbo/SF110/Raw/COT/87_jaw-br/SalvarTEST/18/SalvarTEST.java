
import org.junit.runner.*;
import org.junit.runner.notification.*;
 
public class SalvarTestRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(SalvarTest.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(result.wasSuccessful());
   }
}