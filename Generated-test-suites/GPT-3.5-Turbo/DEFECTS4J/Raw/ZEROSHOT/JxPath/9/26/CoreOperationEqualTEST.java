import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

  @Test
  public void testComputeValue() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

    EvalContext context = new EvalContext();
    context.setVariable("arg1", "test");
    context.setVariable("arg2", "test");

    Object result = coreOperationEqual.computeValue(context);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
  public void testGetSymbol() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

    String symbol = coreOperationEqual.getSymbol();

    assertEquals("=", symbol);
  }

  private class Expression implements org.apache.commons.jxpath.ri.compiler.Expression {
    // Implement required methods for testing
  }
}