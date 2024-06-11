import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

  @Test
  public void testComputeValue_comparison() {
    Expression arg1 = new Expression() {
      public Object computeValue(EvalContext context) {
        return 2.0;
      }
    };

    Expression arg2 = new Expression() {
      public Object computeValue(EvalContext context) {
        return 3.0;
      }
    };

    CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

    EvalContext context = new EvalContext(null);

    Object result = operation.computeValue(context);

    assertFalse((Boolean) result);
  }

  @Test
  public void testGetSymbol() {
    Expression arg1 = new Expression() {
      public Object computeValue(EvalContext context) {
        return 2.0;
      }
    };

    Expression arg2 = new Expression() {
      public Object computeValue(EvalContext context) {
        return 3.0;
      }
    };

    CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

    String symbol = operation.getSymbol();

    assertEquals(">=", symbol);
  }
}