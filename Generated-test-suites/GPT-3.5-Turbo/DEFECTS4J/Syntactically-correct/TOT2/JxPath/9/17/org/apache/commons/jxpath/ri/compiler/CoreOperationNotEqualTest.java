import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {
  
  @Test
  public void testComputeValue_NotEqual() {
    CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new MockExpression(5), new MockExpression(10));
    assertTrue((Boolean) coreOperation.computeValue(null));
  }
  
  @Test
  public void testComputeValue_Equal() {
    CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new MockExpression(5), new MockExpression(5));
    assertFalse((Boolean) coreOperation.computeValue(null));
  }
  
  @Test
  public void testGetSymbol() {
    CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(null, null);
    assertEquals("!=", coreOperation.getSymbol());
  }
  
  // Additional test cases for potential edge cases and exceptions
  
  @Test
  public void testComputeValue_NullArgs() {
    CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new MockExpression(null), new MockExpression(null));
    assertFalse((Boolean) coreOperation.computeValue(null));
  }
  
  @Test
  public void testComputeValue_DifferentTypes() {
    CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new MockExpression(5), new MockExpression("5"));
    assertTrue((Boolean) coreOperation.computeValue(null));
  }
  
  @Test
  public void testComputeValue_Exception() {
    CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new MockExpression(5), new MockExpression(0));
    assertTrue((Boolean) coreOperation.computeValue(null));
  }
  
  class MockExpression implements Expression {
    private Object value;
    
    public MockExpression(Object value) {
      this.value = value;
    }
    
    @Override
    public Object computeValue(EvalContext context) {
      return value;
    }
    
    @Override
    public String getSymbol() {
      return null;
    }
  }
}