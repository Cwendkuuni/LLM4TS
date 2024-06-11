import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {
  
  @Test
  public void testComputeValue() {
    CoreOperationLessThan operation = new CoreOperationLessThan(new Expression1(), new Expression2());
    EvalContext context = new EvalContext();
    Object result = operation.computeValue(context);
    assertTrue((boolean) result);
  }

  @Test
  public void testGetSymbol() {
    CoreOperationLessThan operation = new CoreOperationLessThan(new Expression1(), new Expression2());
    String symbol = operation.getSymbol();
    assertEquals("<", symbol);
  }
  
  private class Expression1 extends Expression {
    public Object computeValue(EvalContext context) {
      return 2;
    }
  }
  
  private class Expression2 extends Expression {
    public Object computeValue(EvalContext context) {
      return 5;
    }
  }
}