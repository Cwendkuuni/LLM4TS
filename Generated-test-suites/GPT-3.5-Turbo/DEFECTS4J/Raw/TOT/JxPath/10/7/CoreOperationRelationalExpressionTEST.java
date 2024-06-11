import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

  @Test
  public void testComputeValue_equalValues_returnTrue() {
    Expression left = new TestExpression(5);
    Expression right = new TestExpression(5);
    Expression[] args = {left, right};
    CoreOperationRelationalExpression operation = new TestCoreOperationRelationalExpression(args);

    EvalContext context = new TestEvalContext();
    Object result = operation.computeValue(context);

    assertTrue(result instanceof Boolean);
    assertTrue((boolean) result);
  }

  @Test
  public void testComputeValue_leftSmallerThanRight_returnTrue() {
    Expression left = new TestExpression(3);
    Expression right = new TestExpression(5);
    Expression[] args = {left, right};
    CoreOperationRelationalExpression operation = new TestCoreOperationRelationalExpression(args);

    EvalContext context = new TestEvalContext();
    Object result = operation.computeValue(context);

    assertTrue(result instanceof Boolean);
    assertTrue((boolean) result);
  }

  @Test
  public void testComputeValue_leftGreaterThanRight_returnFalse() {
    Expression left = new TestExpression(5);
    Expression right = new TestExpression(3);
    Expression[] args = {left, right};
    CoreOperationRelationalExpression operation = new TestCoreOperationRelationalExpression(args);

    EvalContext context = new TestEvalContext();
    Object result = operation.computeValue(context);

    assertTrue(result instanceof Boolean);
    assertFalse((boolean) result);
  }

  @Test
  public void testComputeValue_invalidNonNumericValue_returnFalse() {
    Expression left = new TestExpression("five");
    Expression right = new TestExpression(3);
    Expression[] args = {left, right};
    CoreOperationRelationalExpression operation = new TestCoreOperationRelationalExpression(args);

    EvalContext context = new TestEvalContext();
    Object result = operation.computeValue(context);

    assertTrue(result instanceof Boolean);
    assertFalse((boolean) result);
  }

  @Test
  public void testComputeValue_invalidNaNValue_returnFalse() {
    Expression left = new TestExpression(Double.NaN);
    Expression right = new TestExpression(3);
    Expression[] args = {left, right};
    CoreOperationRelationalExpression operation = new TestCoreOperationRelationalExpression(args);

    EvalContext context = new TestEvalContext();
    Object result = operation.computeValue(context);

    assertTrue(result instanceof Boolean);
    assertFalse((boolean) result);
  }

  // Test helper classes

  private class TestExpression extends Expression {
    private Object value;

    public TestExpression(Object value) {
      this.value = value;
    }

    public Object computeValue(EvalContext context) {
      return value;
    }
  }

  private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
    public TestCoreOperationRelationalExpression(Expression[] args) {
      super(args);
    }

    protected boolean evaluateCompare(int compare) {
      return compare < 0;
    }
  }

  private class TestEvalContext extends EvalContext {
    public TestEvalContext() {
      super(null, null, null);
    }
  }
}