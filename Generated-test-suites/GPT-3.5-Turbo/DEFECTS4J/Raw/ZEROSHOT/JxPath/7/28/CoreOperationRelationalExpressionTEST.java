import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
  
  @Test
  public void testGetPrecedence() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
      @Override
      protected boolean isSymmetric() {
        return false;
      }
    };
    int precedence = expression.getPrecedence();
    Assert.assertEquals(3, precedence);
  }
  
  @Test
  public void testIsSymmetric() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
      @Override
      protected int getPrecedence() {
        return 3;
      }
    };
    boolean symmetric = expression.isSymmetric();
    Assert.assertEquals(false, symmetric);
  }
  
}