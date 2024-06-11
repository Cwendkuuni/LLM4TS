import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

  @Test
  public void testComputeValue() {
    CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(null, null);

    // Test when the left argument is less than the right argument. The expected result is true.
    // Create a mock context and set the left and right argument values
    EvalContext context1 = new EvalContext();
    expression.args[0] = createMockExpression(5);
    expression.args[1] = createMockExpression(10);
    assertTrue((boolean) expression.computeValue(context1));

    // Test when the left argument is equal to the right argument. The expected result is true.
    // Create a mock context and set the left and right argument values
    EvalContext context2 = new EvalContext();
    expression.args[0] = createMockExpression(5);
    expression.args[1] = createMockExpression(5);
    assertTrue((boolean) expression.computeValue(context2));

    // Test when the left argument is greater than the right argument. The expected result is false.
    // Create a mock context and set the left and right argument values
    EvalContext context3 = new EvalContext();
    expression.args[0] = createMockExpression(10);
    expression.args[1] = createMockExpression(5);
    assertFalse((boolean) expression.computeValue(context3));
  }

  @Test
  public void testGetSymbol() {
    CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(null, null);
    assertEquals("<=", expression.getSymbol());
  }

  @Test
  public void testConstructor() {
    Expression arg1 = createMockExpression(5);
    Expression arg2 = createMockExpression(10);
    CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

    assertNotNull(expression.args);
    assertEquals(2, expression.args.length);
    assertEquals(arg1, expression.args[0]);
    assertEquals(arg2, expression.args[1]);
  }

  private Expression createMockExpression(double value) {
    return new Expression() {
      @Override
      public Object computeValue(EvalContext context) {
        return value;
      }
    };
  }
}