
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CoreOperationLessThanTest {

  @Test
  public void testComputeValueWithLessThan() {
    Expression arg1 = new ExpressionMock(5);
    Expression arg2 = new ExpressionMock(10);
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    EvalContext context = new EvalContextMock();
    assertEquals(Boolean.TRUE, operation.computeValue(context));
  }

  @Test
  public void testComputeValueWithNotLessThan() {
    Expression arg1 = new ExpressionMock(10);
    Expression arg2 = new ExpressionMock(5);
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    EvalContext context = new EvalContextMock();
    assertEquals(Boolean.FALSE, operation.computeValue(context));
  }

  @Test
  public void testGetSymbol() {
    Expression arg1 = new ExpressionMock(5);
    Expression arg2 = new ExpressionMock(10);
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    assertEquals("<", operation.getSymbol());
  }

  @Test
  public void testComputeValueWithLowercaseTrueAsString() {
    Expression arg1 = new ExpressionMock("true");
    Expression arg2 = new ExpressionMock(5);
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    EvalContext context = new EvalContextMock();
    assertEquals(Boolean.TRUE, operation.computeValue(context));
  }

  @Test
  public void testComputeValueWithUppercaseTrueAsString() {
    Expression arg1 = new ExpressionMock("TRUE");
    Expression arg2 = new ExpressionMock(5);
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    EvalContext context = new EvalContextMock();
    assertEquals(Boolean.TRUE, operation.computeValue(context));
  }

  @Test
  public void testComputeValueWithFalseAsString() {
    Expression arg1 = new ExpressionMock("false");
    Expression arg2 = new ExpressionMock(5);
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    EvalContext context = new EvalContextMock();
    assertEquals(Boolean.FALSE, operation.computeValue(context));
  }

  @Test
  public void testComputeValueWithNullArg1() {
    Expression arg1 = null;
    Expression arg2 = new ExpressionMock(5);
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    EvalContext context = new EvalContextMock();
    assertEquals(Boolean.FALSE, operation.computeValue(context));
  }

  @Test
  public void testComputeValueWithNullArg2() {
    Expression arg1 = new ExpressionMock(5);
    Expression arg2 = null;
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    EvalContext context = new EvalContextMock();
    assertEquals(Boolean.FALSE, operation.computeValue(context));
  }

  @Test
  public void testComputeValueWithNullContext() {
    Expression arg1 = new ExpressionMock(5);
    Expression arg2 = new ExpressionMock(10);
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    assertEquals(Boolean.FALSE, operation.computeValue(null));
  }

  @Test
  public void testComputeValueWithNaN() {
    Expression arg1 = new ExpressionMock(Double.NaN);
    Expression arg2 = new ExpressionMock(10);
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    EvalContext context = new EvalContextMock();
    assertFalse((Boolean) operation.computeValue(context));
  }

  @Test
  public void testComputeValueWithNegativeInfinity() {
    Expression arg1 = new ExpressionMock(Double.NEGATIVE_INFINITY);
    Expression arg2 = new ExpressionMock(-10);
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    EvalContext context = new EvalContextMock();
    assertFalse((Boolean) operation.computeValue(context));
  }

  @Test
  public void testComputeValueWithPositiveInfinity() {
    Expression arg1 = new ExpressionMock(Double.POSITIVE_INFINITY);
    Expression arg2 = new ExpressionMock(10);
    CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

    EvalContext context = new EvalContextMock();
    assertTrue((Boolean) operation.computeValue(context));
  }

  private class ExpressionMock implements Expression {
    private Object value;

    public ExpressionMock(Object value) {
      this.value = value;
    }

    @Override
    public Object computeValue(EvalContext context) {
      return value;
    }

    @Override
    public String toString() {
      return value.toString();
    }
  }

  private class EvalContextMock implements EvalContext {
    @Override
    public Object getValue() {
      return null;
    }

    @Override
    public void setValue(Object value) {}

    @Override
    public String toString() {
      return "EvalContextMock";
    }
  }
}
