import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
  
  @Test
  public void testComputeValue() {
    EvalContext context = new EvalContext();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTestStub();
    
    // Test with true result
    Object result1 = expression.computeValue(context);
    assertEquals(Boolean.TRUE, result1);
    
    // Test with false result
    Object result2 = expression.computeValue(context);
    assertEquals(Boolean.FALSE, result2);
  }
  
  @Test
  public void testGetPrecedence() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTestStub();
    
    int precedence = expression.getPrecedence();
    
    assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
  }
  
  @Test
  public void testIsSymmetric() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTestStub();
    
    boolean symmetric = expression.isSymmetric();
    
    assertEquals(false, symmetric);
  }
  
  class CoreOperationRelationalExpressionTestStub extends CoreOperationRelationalExpression {
    
    public CoreOperationRelationalExpressionTestStub() {
      super(null);
    }
    
    @Override
    protected boolean evaluateCompare(int compare) {
      // Implementation for test purposes only
      return compare > 0;
    }
  }
}