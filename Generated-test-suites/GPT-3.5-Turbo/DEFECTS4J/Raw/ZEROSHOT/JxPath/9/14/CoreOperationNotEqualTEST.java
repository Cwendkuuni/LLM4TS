import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationEqualTest {

   private CoreOperationNotEqual operation;

   @Before
   public void setUp() {
      operation = new CoreOperationNotEqual(null, null);
   }

   @Test
   public void testComputeValueEqual() {
      EvalContext context = new EvalContext();
      Object result = operation.computeValue(context);
      assertEquals(Boolean.FALSE, result);
   }

   @Test
   public void testComputeValueNotEqual() {
      EvalContext context = new EvalContext();
      context.setExpressionContext(null);
      Object result = operation.computeValue(context);
      assertEquals(Boolean.TRUE, result);
   }

   @Test
   public void testGetSymbol() {
      String symbol = operation.getSymbol();
      assertEquals("!=", symbol);
   }

   @Test
   public void testEquals() {
      CoreOperationNotEqual operation1 = new CoreOperationNotEqual(null, null);
      CoreOperationNotEqual operation2 = new CoreOperationNotEqual(null, null);
      assertEquals(operation1, operation2);
   }

   @Test
   public void testNotEquals() {
      CoreOperationNotEqual operation1 = new CoreOperationNotEqual(null, null);
      CoreOperationNotEqual operation2 = new CoreOperationNotEqual(null, new Object());
      assertNotEquals(operation1, operation2);
   }
}