import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {
  
  @Test
  public void testComputeValue() {
    EvalContext context = new EvalContext();
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
    
    // Test case 1
    arg1.setValue(5);
    arg2.setValue(10);
    assertFalse(operation.computeValue(context));
    
    // Test case 2
    arg1.setValue(10);
    arg2.setValue(10);
    assertTrue(operation.computeValue(context));
  }
  
  @Test
  public void testGetSymbol() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
    
    assertEquals("!=", operation.getSymbol());
  }
}