import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {
  
  @Test
  public void testComputeValue() {
    EvalContext context = new EvalContext();
    CoreOperationLessThan operation = new CoreOperationLessThan(new Expression() {
      public Object computeValue(EvalContext context) {
        return 10;
      }
    }, new Expression() {
      public Object computeValue(EvalContext context) {
        return 5;
      }
    });
    
    Object result = operation.computeValue(context);
    assertEquals(Boolean.FALSE, result);
  }
  
  @Test
  public void testGetSymbol() {
    CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
    String symbol = operation.getSymbol();
    assertEquals("<", symbol);
  }
}