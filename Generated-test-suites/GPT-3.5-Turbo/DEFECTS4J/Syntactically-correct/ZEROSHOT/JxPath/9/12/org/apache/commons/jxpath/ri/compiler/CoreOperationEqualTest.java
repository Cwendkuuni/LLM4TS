import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationEqualTest {
  
  @Test
  public void testComputeValue() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
    EvalContext context = new EvalContext();
    
    // Test when equal(context, args[0], args[1]) returns true
    assertTrue(operationEqual.computeValue(context));
    
    // Test when equal(context, args[0], args[1]) returns false
    assertFalse(operationEqual.computeValue(context));
  }
  
  @Test
  public void testGetSymbol() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
    
    assertEquals("=", operationEqual.getSymbol());
  }
}