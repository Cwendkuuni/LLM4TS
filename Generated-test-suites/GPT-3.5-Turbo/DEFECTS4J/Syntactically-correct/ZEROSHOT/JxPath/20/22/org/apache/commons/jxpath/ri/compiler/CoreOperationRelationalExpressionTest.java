import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
  
  @Test
  public void testComputeValue() {
    // Test case 1
    EvalContext context1 = new EvalContext();
    context1.setSingleNodePointer(new SelfContext());
    Expression[] args1 = new Expression[2];
    args1[0] = new SelfContext();
    args1[1] = new SelfContext();
    CoreOperationRelationalExpression expression1 = new CoreOperationRelationalExpression(args1) {
      protected boolean evaluateCompare(int compare) {
        return true;
      }
    };
    Object result1 = expression1.computeValue(context1);
    assertTrue((boolean)result1);
    
    // Test case 2
    EvalContext context2 = new EvalContext();
    context2.setSingleNodePointer(new SelfContext());
    Expression[] args2 = new Expression[2];
    args2[0] = new SelfContext();
    args2[1] = new SelfContext();
    CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(args2) {
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    Object result2 = expression2.computeValue(context2);
    assertFalse((boolean)result2);
  }
  
  @Test
  public void testGetPrecedence() {
    Expression[] args = new Expression[2];
    args[0] = new SelfContext();
    args[1] = new SelfContext();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    int precedence = expression.getPrecedence();
    assertEquals(13, precedence);
  }
  
  @Test
  public void testIsSymmetric() {
    Expression[] args = new Expression[2];
    args[0] = new SelfContext();
    args[1] = new SelfContext();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    boolean symmetric = expression.isSymmetric();
    assertFalse(symmetric);
  }
  
}