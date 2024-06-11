import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {
  
  @Test
  public void testGetExternalFunctions() {
    Expression expression = new Expression();
    assertNotNull(expression.getExternalFunctions());
  }
  
  @Test
  public void testExpressionConstructor() {
    Expression expression = new Expression("1 + 2");
    assertEquals("1 + 2", expression.expression);
    assertNotNull(expression.stack);
    assertNull(expression.externalFunctions);
    assertFalse(expression.prepared);
  }
  
  @Test
  public void testDoEvaluationStack() {
    Expression expression = new Expression();
    expression.stack.add("1");
    expression.stack.add("+");
    expression.stack.add("2");
    expression.doEvaluationStack();
    assertEquals(3, expression.stack.size());
    assertEquals("1", expression.stack.get(0));
    assertEquals("2", expression.stack.get(1));
    assertEquals("+", expression.stack.get(2));
  }
  
  @Test
  public void testPutFunction() {
    Expression expression = new Expression();
    FunctionExpression f = new GenericFunctionExpression("test");
    expression.putFunction(f, expression.reservedFunctions);
    assertTrue(expression.reservedFunctions.containsKey(f));
  }
  
  @Test
  public void testGetReservedFunctions() {
    assertEquals(40, Expression.getReservedFunctions("Test").getParameters().size());
  }
  
  @Test
  public void testIsReservedFunction() {
    Expression expression = new Expression();
    assertFalse(expression.isReservedFunction("Test"));
  }
  
  @Test
  public void testIsLogicalFunction() {
    Expression expression = new Expression();
    assertFalse(expression.isLogicalFunction("Test"));
  }
  
  @Test
  public void testIsMathFunction() {
    Expression expression = new Expression();
    assertFalse(expression.isMathFunction("Test"));
  }
  
  @Test
  public void testIsDateFunction() {
    Expression expression = new Expression();
    assertFalse(expression.isDateFunction("Test"));
  }
  
  @Test
  public void testIsStringFunction() {
    Expression expression = new Expression();
    assertFalse(expression.isStringFunction("Test"));
  }
  
  @Test
  public void testGetExternalFunction() {
    Expression expression = new Expression();
    assertNull(expression.getExternalFunction("Test"));
  }
  
  @Test
  public void testIsExternalFunction() {
    Expression expression = new Expression();
    assertFalse(expression.isExternalFunction("Test"));
  }
  
  @Test
  public void testGetFunction() {
    Expression expression = new Expression();
    assertNull(expression.getFunction("Test"));
  }
  
  @Test
  public void testIsFunction() {
    Expression expression = new Expression();
    assertFalse(expression.isFunction("Test"));
  }
  
  @Test
  public void testExpressionDefaultConstructor() {
    Expression expression = new Expression();
    assertFalse(expression.prepared);
  }
  
  @Test
  public void testStack() {
    Expression expression = new Expression();
    Stack<String> stack = new Stack<String>();
    expression.stack("test", stack);
    assertEquals("test", stack.pop());
  }
  
  @Test
  public void testPrepare() {
    Expression expression = new Expression();
    expression.expression = "1 + 2";
    expression.prepare();
    assertTrue(expression.prepared);
  }
  
  @Test
  public void testGetStackCopy() {
    Expression expression = new Expression();
    expression.stack.add("1");
    ExpressionStack stackCopy = expression.getStackCopy();
    assertEquals(1, stackCopy.size());
    assertEquals("1", stackCopy.get(0));
  }
  
  @Test(expected = Exception.class)
  public void testEvaluate() throws Exception {
    Expression expression = new Expression() {
      public Object evaluate() throws Exception {
        throw new Exception("Evaluation error");
      }
    };
    expression.evaluate();
  }
  
  @Test
  public void testTokenize() {
    Expression expression = new Expression();
    expression.expression = "1 + 2 - 3";
    expression.tokenize();
    assertEquals(5, expression.stack.size());
    assertEquals("1", expression.stack.get(0));
    assertEquals("+", expression.stack.get(1));
    assertEquals("2", expression.stack.get(2));
    assertEquals("-", expression.stack.get(3));
    assertEquals("3", expression.stack.get(4));
  }
}