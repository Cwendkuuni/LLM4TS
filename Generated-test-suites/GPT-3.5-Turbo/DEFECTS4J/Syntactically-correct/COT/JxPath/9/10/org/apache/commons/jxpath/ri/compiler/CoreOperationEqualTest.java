import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationEqualTest {
  
  @Test
  public void testComputeValue() {
    EvalContext context = new EvalContext();
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
    
    Object result = operation.computeValue(context);
    assertEquals(Boolean.class, result.getClass());
  }
  
  @Test
  public void testGetSymbol() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
    
    String symbol = operation.getSymbol();
    assertEquals("=", symbol);
  }
  
}