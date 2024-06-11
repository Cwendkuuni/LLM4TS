import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

   @Test
   public void testComputeValue() {
      // typical use cases
      double arg1 = 5;
      double arg2 = 3;
      CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
      Boolean result = operation.computeValue(null);
      assertEquals(true, result);

      arg1 = 3;
      arg2 = 5;
      operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
      result = operation.computeValue(null);
      assertEquals(false, result);

      arg1 = 5;
      arg2 = 5;
      operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
      result = operation.computeValue(null);
      assertEquals(true, result);

      // edge cases
      arg1 = Double.MIN_VALUE;
      arg2 = Double.MIN_VALUE;
      operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
      result = operation.computeValue(null);
      assertEquals(true, result);

      arg1 = Double.MAX_VALUE;
      arg2 = Double.MAX_VALUE;
      operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
      result = operation.computeValue(null);
      assertEquals(true, result);

      // error scenarios
      arg1 = Double.NaN;
      arg2 = 5;
      operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
      result = operation.computeValue(null);
      assertEquals(false, result);

      arg1 = 5;
      arg2 = Double.NaN;
      operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
      result = operation.computeValue(null);
      assertEquals(false, result);
   }

   @Test
   public void testGetSymbol() {
      CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(3, 5);
      assertEquals(">=", operation.getSymbol());
   }
}