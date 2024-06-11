import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

  @Test
  public void testComputeValue() {
    Expression[] args = {};
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      @Override
      protected boolean evaluateCompare(int compare) {
        return compare > 0;
      }
    };
    
    EvalContext context = null;
    
    Object result = expression.computeValue(context);
    
    assertEquals(Boolean.FALSE, result);
  }
  
  @Test
  public void testGetPrecedence() {
    Expression[] args = {};
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      @Override
      protected boolean evaluateCompare(int compare) {
        return true;
      }
    };
    
    int precedence = expression.getPrecedence();
    
    assertEquals(3, precedence);
  }
  
  @Test
  public void testIsSymmetric() {
    Expression[] args = {};
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      @Override
      protected boolean evaluateCompare(int compare) {
        return true;
      }
    };
    
    boolean symmetric = expression.isSymmetric();
    
    assertEquals(false, symmetric);
  }

}