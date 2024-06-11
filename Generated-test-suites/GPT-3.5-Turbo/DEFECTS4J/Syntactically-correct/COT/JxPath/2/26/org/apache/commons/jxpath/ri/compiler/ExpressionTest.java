import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

  @Test
  public void testIsContextDependent() {
    Expression expression = new Expression() {
      @Override
      public boolean computeContextDependent() {
        // TODO: Implement test case for computeContextDependent()
      }
    };

    // TODO: Write the test case for isContextDependent()
  }
  
  @Test
  public void testComputeValue() {
    Expression expression = new Expression() {
      @Override
      public boolean computeContextDependent() {
        // TODO: Implement test case for computeContextDependent()
      }
      
      @Override
      public Object computeValue(EvalContext context) {
        // TODO: Implement test case for computeValue()
      }
    };

    // TODO: Write the test case for computeValue()
  }
  
  // Repeat the same structure for the remaining methods (compute, iterate, iteratePointers)

}