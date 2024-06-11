import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

  @Test
  public void testComputeValue() {
    Expression[] args = new Expression[2];
    args[0] = new TestExpression();
    args[1] = new TestExpression();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      @Override
      protected boolean evaluateCompare(int compare) {
        return compare > 0;
      }
    };
    Assert.assertEquals(Boolean.TRUE, expression.computeValue(null));
  }

  @Test
  public void testGetPrecedence() {
    Expression[] args = new Expression[2];
    args[0] = new TestExpression();
    args[1] = new TestExpression();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      @Override
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    Assert.assertEquals(4, expression.getPrecedence());
  }

  @Test
  public void testIsSymmetric() {
    Expression[] args = new Expression[2];
    args[0] = new TestExpression();
    args[1] = new TestExpression();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      @Override
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    Assert.assertEquals(false, expression.isSymmetric());

  }

  private class TestExpression extends Expression {
    @Override
    public Object computeValue(EvalContext context) {
      return null;
    }
  }

}