import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

  @Test
  public void testGetPrecedence() {
    CoreOperationRelationalExpression testExpression = new CoreOperationRelationalExpression(null);
    int precedence = testExpression.getPrecedence();
    assertEquals(3, precedence);
  }

  @Test
  public void testIsSymmetric() {
    CoreOperationRelationalExpression testExpression = new CoreOperationRelationalExpression(null);
    boolean symmetric = testExpression.isSymmetric();
    assertEquals(false, symmetric);
  }

}