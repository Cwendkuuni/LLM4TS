import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

import static org.junit.Assert.*;

public class CoreOperationEqualTest {

  private CoreOperationEqual coreOperationEqual;

  @Before
  public void setup() {
    Expression arg1 = new CoreVariable("var1");
    Expression arg2 = new CoreVariable("var2");
    coreOperationEqual = new CoreOperationEqual(arg1, arg2);
  }

  @Test
  public void testComputeValue() {
    EvalContext context = new EvalContext();
    context.setVariable("var1", "test");
    context.setVariable("var2", "test");

    Object result = coreOperationEqual.computeValue(context);

    assertTrue(result instanceof Boolean);
    assertTrue((Boolean) result);
  }

  @Test
  public void testComputeValueDifferentStrings() {
    EvalContext context = new EvalContext();
    context.setVariable("var1", "test1");
    context.setVariable("var2", "test2");

    Object result = coreOperationEqual.computeValue(context);

    assertTrue(result instanceof Boolean);
    assertFalse((Boolean) result);
  }

  @Test
  public void testComputeValueNullValue() {
    EvalContext context = new EvalContext();
    context.setVariable("var1", null);
    context.setVariable("var2", "test");

    Object result = coreOperationEqual.computeValue(context);

    assertTrue(result instanceof Boolean);
    assertFalse((Boolean) result);
  }

  @Test
  public void testComputeValueNullVariables() {
    EvalContext context = new EvalContext();

    Object result = coreOperationEqual.computeValue(context);

    assertTrue(result instanceof Boolean);
    assertFalse((Boolean) result);
  }

  @Test
  public void testGetSymbol() {
    String symbol = coreOperationEqual.getSymbol();

    assertEquals("=", symbol);
  }
}