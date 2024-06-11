import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

   @Test
   public void testComputeValue() {
      Expression arg1 = new Constant("5");
      Expression arg2 = new Constant("3");

      CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
      EvalContext context = new EvalContext();

      Object result = operation.computeValue(context);

      assertTrue(result instanceof Boolean);
      assertTrue((Boolean) result);
   }

   @Test
   public void testGetSymbol() {
      Expression arg1 = new Constant("5");
      Expression arg2 = new Constant("3");

      CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

      String symbol = operation.getSymbol();

      assertEquals(">", symbol);
   }
}