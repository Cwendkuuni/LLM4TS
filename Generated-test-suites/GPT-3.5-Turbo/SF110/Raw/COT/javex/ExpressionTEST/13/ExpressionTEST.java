
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
  @Test
  public void testGetExternalFunctions() {
    Expression expression = new Expression("") {};
    List expected = new ArrayList();
    List result = expression.getExternalFunctions();
    assertEquals(expected, result);
  }

  @Test
  public void testConstructor() {
    Expression expression = new Expression("") {};
    String expectedExpression = "";
    String resultExpression = expression.expression;
    assertEquals(expectedExpression, resultExpression);
    ExpressionStack expectedStack = new ExpressionStack();
    ExpressionStack resultStack = expression.stack;
    assertEquals(expectedStack, resultStack);
  }

  @Test
  public void testDoEvaluationStack() {
    Expression expression = new Expression("") {};
    expression.stack = new ExpressionStack();
    expression.stack.add("+");
    expression.stack.add("5");
    expression.stack.add("-");
    expression.stack.add("3");
    expression.doEvaluationStack();
    ExpressionStack expected = new ExpressionStack();
    expected.add("+");
    expected.add("-3");
    expected.add("5");
    ExpressionStack result = expression.stack;
    assertEquals(expected, result);
  }

  @Test
  public void testGetReservedFunctions() {
    FunctionExpression expected = null;
    FunctionExpression result = Expression.getReservedFunctions("+");
    assertEquals(expected, result);
  }

  @Test
  public void testIsReservedFunction() {
    Expression expression = new Expression("") {};
    boolean result = expression.isReservedFunction("+");
    assertEquals(false, result);
  }

  @Test
  public void testIsLogicalFunction() {
    Expression expression = new Expression("") {};
    expression.stack = new ExpressionStack();
    expression.stack.add("and");
    boolean result = expression.isLogicalFunction("and");
    assertEquals(true, result);
    result = expression.isLogicalFunction("or");
    assertEquals(false, result);
  }

  @Test
  public void testIsMathFunction() {
    Expression expression = new Expression("") {};
    expression.stack = new ExpressionStack();
    expression.stack.add("+");
    boolean result = expression.isMathFunction("+");
    assertEquals(true, result);
    result = expression.isMathFunction("-");
    assertEquals(false, result);
  }

  @Test
  public void testIsDateFunction() {
    Expression expression = new Expression("") {};
    expression.stack = new ExpressionStack();
    expression.stack.add("dateadd");
    boolean result = expression.isDateFunction("dateadd");
    assertEquals(true, result);
    result = expression.isDateFunction("datediff");
    assertEquals(false, result);
  }

  @Test
  public void testIsStringFunction() {
    Expression expression = new Expression("") {};
    expression.stack = new ExpressionStack();
    expression.stack.add("substring");
    boolean result = expression.isStringFunction("substring");
    assertEquals(true, result);
    result = expression.isStringFunction("startswith");
    assertEquals(false, result);
  }

  @Test
  public void testIsExternalFunction() {
    Expression expression = new Expression("") {};
    List externalFunctions = new ArrayList();
    externalFunctions.add(new FunctionExpression("avg"));
    expression.externalFunctions = externalFunctions;
    boolean result = expression.isExternalFunction("avg");
    assertEquals(true, result);
    result = expression.isExternalFunction("sum");
    assertEquals(false, result);
  }

  @Test
  public void testGetFunction() {
    Expression expression = new Expression("") {};
    FunctionExpression expected = null;
    FunctionExpression result = expression.getFunction("+");
    assertEquals(expected, result);
    expression.stack = new ExpressionStack();
    expression.stack.add("length");
    expected = new LengthFunction();
    result = expression.getFunction("length");
    assertEquals(expected, result);
  }

  @Test
  public void testIsFunction() {
    Expression expression = new Expression("") {};
    expression.stack = new ExpressionStack();
    expression.stack.add("+");
    boolean result = expression.isFunction("+");
    assertEquals(true, result);
    result = expression.isFunction("-");
    assertEquals(false, result);
  }

  @Test
  public void testPrepare() {
    Expression expression = new Expression("") {};
    expression.stack = new ExpressionStack();
    expression.stack.add("2");
    expression.stack.add("*");
    expression.stack.add("(");
    expression.stack.add("3");
    expression.stack.add("+");
    expression.stack.add("4");
    expression.stack.add(")");
    expression.stack.add("/");
    expression.stack.add("5");
    expression.stack.add("-");
    expression.stack.add("6");
    expression.prepare();
    ExpressionStack expected = new ExpressionStack();
    expected.add("2");
    expected.add("3");
    expected.add("4");
    expected.add("+");
    expected.add("*");
    expected.add("5");
    expected.add("6");
    expected.add("-");
    expected.add("/");
    ExpressionStack result = expression.stack;
    assertEquals(expected, result);
    assertEquals(true, expression.prepared);
  }

  @Test
  public void testGetStackCopy() {
    Expression expression = new Expression("") {};
    expression.stack = new ExpressionStack();
    expression.stack.add("3");
    ExpressionStack expected = new ExpressionStack();
    expected.add("3");
    ExpressionStack result = expression.getStackCopy();
    assertEquals(expected, result);
  }

  @Test
  public void testEvaluate() {
    // Test cases for evaluate() method
  }

  @Test
  public void testTokenize() {
    Expression expression = new Expression("") {};
    expression.expression = "2 + (3 + 4) / 5 - 6";
    expression.tokenize();
    ExpressionStack expected = new ExpressionStack();
    expected.add("2");
    expected.add("+");
    expected.add("(");
    expected.add("3");
    expected.add("+");
    expected.add("4");
    expected.add(")");
    expected.add("/");
    expected.add("5");
    expected.add("-");
    expected.add("6");
    ExpressionStack result = expression.stack;
    assertEquals(expected, result);
  }
}
