import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

   @Test
   public void testComputeValue() {
      // Typical Use Case: Different values
      Expression arg1 = new Expression("value1");
      Expression arg2 = new Expression("value2");
      CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
      EvalContext context = new EvalContext();
      Object result = operation.computeValue(context);
      assertEquals(Boolean.TRUE, result);

      // Edge Case: Same values
      arg1 = new Expression("value");
      arg2 = new Expression("value");
      operation = new CoreOperationNotEqual(arg1, arg2);
      context = new EvalContext();
      result = operation.computeValue(context);
      assertEquals(Boolean.FALSE, result);

      // Error Scenario: Null values
      arg1 = new Expression(null);
      arg2 = new Expression(null);
      operation = new CoreOperationNotEqual(arg1, arg2);
      context = new EvalContext();
      result = operation.computeValue(context);
      assertNull(result);
   }

   @Test
   public void testGetSymbol() {
      Expression arg1 = new Expression("value1");
      Expression arg2 = new Expression("value2");
      CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
      String result = operation.getSymbol();
      assertEquals("!=", result);
   }

}