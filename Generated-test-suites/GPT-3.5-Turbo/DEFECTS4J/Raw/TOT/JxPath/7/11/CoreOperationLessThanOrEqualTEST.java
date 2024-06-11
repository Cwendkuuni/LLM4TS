import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationLessThanOrEqualTest {

  private CoreOperationLessThanOrEqual operation;

  @Before
  public void setup() {
    Expression arg1 = new ExpressionMock();
    Expression arg2 = new ExpressionMock();
    operation = new CoreOperationLessThanOrEqual(arg1, arg2);
  }

  @Test
  public void testComputeValue_LessThanOrEqualTrue() {
    EvalContext context = new EvalContext();
    context.setReturnValue(5.0);

    double l = 5.0;
    double r = 10.0;

    boolean expected = true;
    boolean actual = (boolean) operation.computeValue(context);

    assertEquals(expected, actual);
  }

  @Test
  public void testComputeValue_LessThanOrEqualFalse() {
    EvalContext context = new EvalContext();
    context.setReturnValue(10.0);

    double l = 15.0;
    double r = 10.0;

    boolean expected = false;
    boolean actual = (boolean) operation.computeValue(context);

    assertEquals(expected, actual);
  }

  @Test
  public void testComputeValue_EqualTrue() {
    EvalContext context = new EvalContext();
    context.setReturnValue(8.0);

    double l = 8.0;
    double r = 8.0;

    boolean expected = true;
    boolean actual = (boolean) operation.computeValue(context);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSymbol() {
    String expected = "<=";
    String actual = operation.getSymbol();

    assertEquals(expected, actual);
  }

  private class ExpressionMock implements Expression {
    @Override
    public Object computeValue(EvalContext context) {
      return context.getReturnValue();
    }
  }
}