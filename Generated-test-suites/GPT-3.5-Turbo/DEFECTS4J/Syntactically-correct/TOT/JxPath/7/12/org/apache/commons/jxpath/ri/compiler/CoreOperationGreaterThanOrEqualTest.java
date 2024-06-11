import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

  @Test
  public void testComputeValue() {
    CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(null, null);
    
    // Typical use cases
    assertEquals(Boolean.TRUE, expression.computeValue(getContext(5, 2))); 
    assertEquals(Boolean.TRUE, expression.computeValue(getContext(3.5, 3.5)));
    assertEquals(Boolean.FALSE, expression.computeValue(getContext(1, 5)));
    
    // Edge cases
    assertEquals(Boolean.TRUE, expression.computeValue(getContext(Double.MAX_VALUE, Double.NEGATIVE_INFINITY)));
    assertEquals(Boolean.TRUE, expression.computeValue(getContext(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)));
    assertEquals(Boolean.FALSE, expression.computeValue(getContext(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
    
    // Error scenarios
    try {
      expression.computeValue(getContext("10", 5));
      fail("Exception not thrown for non-numeric values");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }
  
  @Test
  public void testGetSymbol() {
    CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(null, null);
    assertEquals(">=", expression.getSymbol());
  }
  
  private EvalContext getContext(Object arg1, Object arg2) {
    // Implement eval context creation with given arguments
    // Return the created eval context object
  }
}