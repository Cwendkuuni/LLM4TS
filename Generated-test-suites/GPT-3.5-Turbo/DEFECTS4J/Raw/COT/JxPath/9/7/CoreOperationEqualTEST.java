import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

  @Test
  public void testComputeValue() {
    Expression arg1 = null;
    Expression arg2 = null;
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    EvalContext context = null;
    Object result = coreOperationEqual.computeValue(context);
    Assert.assertEquals(Boolean.FALSE, result);
  }

  @Test
  public void testGetSymbol() {
    Expression arg1 = null;
    Expression arg2 = null;
    CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    String symbol = coreOperationEqual.getSymbol();
    Assert.assertEquals("=", symbol);
  }
}