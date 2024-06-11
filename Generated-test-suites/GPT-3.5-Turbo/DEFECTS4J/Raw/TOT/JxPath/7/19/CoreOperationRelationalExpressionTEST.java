import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
  
  @Test
  public void testGetPrecedence() {
    CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(null);

    int result = exp.getPrecedence();
    
    assertEquals(3, result);
  }
  
  @Test
  public void testIsSymmetric() {
    CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(null);
    
    boolean result = exp.isSymmetric();
    
    assertFalse(result);
  }
  
  @Test
  public void testConstructor() {
    Expression[] args = new Expression[2];
    CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(args);

    Expression[] result = exp.getArgs();
    
    assertArrayEquals(args, result);
  }
  
  // Edge case: expression with null args
  @Test
  public void testConstructorWithNullArgs() {
    CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(null);

    Expression[] result = exp.getArgs();
    
    assertNull(result);
  }
  
  // Error scenario: expression with only one arg
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithOneArg() {
    Expression[] args = new Expression[1];
    CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(args);
  }
  
  // Error scenario: expression with more than two args
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithMoreThanTwoArgs() {
    Expression[] args = new Expression[3];
    CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(args);
  }
  
}