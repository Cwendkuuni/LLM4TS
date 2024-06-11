import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

  @Test
  public void testComputeValue() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    EvalContext context = new EvalContext();
    context.setVariable("var1", "value1");
    context.setVariable("var2", "value2");
    
    Object result = expression.computeValue(context);
    assertEquals(Boolean.TRUE, result);
  }

  @Test
  public void testGetPrecedence() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    int result = expression.getPrecedence();
    assertEquals(RELATIONAL_EXPR_PRECEDENCE, result);
  }

  @Test
  public void testIsSymmetric() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    boolean result = expression.isSymmetric();
    assertEquals(false, result);
  }

  @Test
  public void testEvaluateCompare() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    boolean result = expression.evaluateCompare(0);
    assertEquals(true, result);
  }

  @Test
  public void testCompute() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    Object result = expression.compute("value1", "value2");
    assertEquals(false, result);
  }

  @Test
  public void testReduce() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    Object result = expression.reduce("value1");
    assertEquals("value1", result);
  }

  @Test
  public void testContainsMatch() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    Iterator it = new Iterator();
    boolean result = expression.containsMatch(it, "value1");
    assertEquals(false, result);
  }

  @Test
  public void testFindMatch() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    Iterator lit = new Iterator();
    Iterator rit = new Iterator();
    boolean result = expression.findMatch(lit, rit);
    assertEquals(false, result);
  }
}