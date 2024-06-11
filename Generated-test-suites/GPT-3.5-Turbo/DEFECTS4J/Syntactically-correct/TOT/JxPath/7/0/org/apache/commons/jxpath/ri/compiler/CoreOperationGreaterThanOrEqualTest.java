import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
  
  @Test
  public void testComputeValue() {
    EvalContext context = null; // Initialize with a valid context object
    
    // Test with two equal integer values
    Expression arg1 = new Expression(value1);
    Expression arg2 = new Expression(value1);
    CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    Object result = operation.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertTrue((Boolean) result);
    
    // Test with two different integer values where arg2 > arg1
    arg1 = new Expression(value1);
    arg2 = new Expression(value2);
    operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    result = operation.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertFalse((Boolean) result);
    
    // Test with two different integer values where arg1 > arg2
    arg1 = new Expression(value2);
    arg2 = new Expression(value1);
    operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    result = operation.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertTrue((Boolean) result);
    
    // Test with floating point values
    arg1 = new Expression(value3);
    arg2 = new Expression(value4);
    operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    result = operation.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertTrue((Boolean) result);
    
    // Test with edge case of arg1 = Double.MAX_VALUE and arg2 = Double.MAX_VALUE
    arg1 = new Expression(Double.MAX_VALUE);
    arg2 = new Expression(Double.MAX_VALUE);
    operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    result = operation.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertTrue((Boolean) result);
    
    // Test with edge case of arg1 = Double.MIN_VALUE and arg2 = Double.MIN_VALUE
    arg1 = new Expression(Double.MIN_VALUE);
    arg2 = new Expression(Double.MIN_VALUE);
    operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    result = operation.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertTrue((Boolean) result);
    
    // Test with error scenario where arg1 is null
    arg1 = null;
    arg2 = new Expression(value1);
    operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    result = operation.computeValue(context);
    assertNull(result);
    
    // Test with error scenario where arg2 is null
    arg1 = new Expression(value1);
    arg2 = null;
    operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    result = operation.computeValue(context);
    assertNull(result);
  }
  
  @Test
  public void testGetSymbol() {
    Expression arg1 = new Expression(value1);
    Expression arg2 = new Expression(value2);
    CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    String symbol = operation.getSymbol();
    assertEquals(">=", symbol);
  }
}