import org.junit.*;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

  @Test
  public void testGetPrecedence() {
    Expression[] args = new Expression[2];
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
    int result = expression.getPrecedence();
    assertEquals(3, result);
  }

  @Test
  public void testIsSymmetric() {
    Expression[] args = new Expression[2];
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
    boolean result = expression.isSymmetric();
    assertFalse(result);
  }

  private static class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {
    public CoreOperationRelationalExpressionImpl(Expression[] args) {
      super(args);
    }
  }

}