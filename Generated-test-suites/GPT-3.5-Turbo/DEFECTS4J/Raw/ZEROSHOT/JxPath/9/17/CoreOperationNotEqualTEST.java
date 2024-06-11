import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationNotEqualTest {

  private CoreOperationNotEqual operation;

  @Before
  public void setUp() {
    operation = new CoreOperationNotEqual(null, null);
  }

  @Test
  public void testComputeValueEqualArgs() {
    EvalContext context = new EvalContext();
    Object arg1 = new Object();
    Object arg2 = new Object();
    operation.setArgs(arg1, arg2);

    Object result = operation.computeValue(context);

    assertEquals(Boolean.FALSE, result);
  }

  @Test
  public void testComputeValueNotEqualArgs() {
    EvalContext context = new EvalContext();
    Object arg1 = new Object();
    Object arg2 = new Object();
    operation.setArgs(arg1, arg2);

    Object result = operation.computeValue(context);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
  public void testGetSymbol() {
    String symbol = operation.getSymbol();

    assertEquals("!=", symbol);
  }
}