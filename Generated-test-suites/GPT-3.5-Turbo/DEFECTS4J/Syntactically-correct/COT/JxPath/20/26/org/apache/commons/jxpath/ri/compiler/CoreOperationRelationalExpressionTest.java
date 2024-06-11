
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
  
  @Test
  public void testComputeValue() {
    // Test case 1
    Expression[] args1 = new Expression[2];
    args1[0] = new Expression();
    args1[1] = new Expression();
    
    CoreOperationRelationalExpression test1 = new CoreOperationRelationalExpression(args1) {
      
      @Override
      protected boolean evaluateCompare(int compare) {
        return false;
      }
      
    };
    
    EvalContext context1 = new EvalContext();
    assertFalse(test1.computeValue(context1));
    
    // Test case 2
    Expression[] args2 = new Expression[2];
    args2[0] = new Expression();
    args2[1] = new Expression();
    
    CoreOperationRelationalExpression test2 = new CoreOperationRelationalExpression(args2) {
      
      @Override
      protected boolean evaluateCompare(int compare) {
        return true;
      }
      
    };
    
    EvalContext context2 = new EvalContext();
    assertTrue(test2.computeValue(context2));
  }
  
}
