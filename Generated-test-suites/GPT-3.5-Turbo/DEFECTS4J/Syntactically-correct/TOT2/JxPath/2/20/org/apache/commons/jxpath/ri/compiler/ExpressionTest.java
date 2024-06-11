import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
  
  @Test
  public void testIsContextDependent() {
    Expression expression = new Expression() {
      @Override
      public boolean computeContextDependent() {
        return true;
      }
    };
    
    assertTrue(expression.isContextDependent());
  }
  
  @Test
  public void testComputeValue() {
    Expression expression = new Expression() {
      @Override
      public boolean computeContextDependent() {
        return false;
      }
      
      @Override
      public Object computeValue(EvalContext context) {
        return "Test Value";
      }
      
      @Override
      public Object compute(EvalContext context) {
        return "Test Compute";
      }
    };
    
    EvalContext context = new EvalContext();
    assertEquals("Test Value", expression.computeValue(context));
  }
  
  // Add more test cases for other methods as needed
  
}