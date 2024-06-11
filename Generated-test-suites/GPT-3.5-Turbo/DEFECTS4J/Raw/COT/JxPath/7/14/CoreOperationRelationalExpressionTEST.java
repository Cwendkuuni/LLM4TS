import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
  
  @Test
  public void testGetPrecedence() {
    CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null);
    assertEquals(3, expr.getPrecedence());
  }
  
  @Test
  public void testIsSymmetric() {
    CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null);
    assertFalse(expr.isSymmetric());
  }
  
}