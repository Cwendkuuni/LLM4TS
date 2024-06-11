import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
  
  @Test
  public void testGetPrecedence() {
    CoreOperationRelationalExpression coreOpRelExp = new CoreOperationRelationalExpression(null);
    int expected = 3;
    int actual = coreOpRelExp.getPrecedence();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsSymmetric() {
    CoreOperationRelationalExpression coreOpRelExp = new CoreOperationRelationalExpression(null);
    boolean expected = false;
    boolean actual = coreOpRelExp.isSymmetric();
    assertEquals(expected, actual);
  }
  
}