import static org.junit.Assert.*;
import org.junit.*;

public class CoreOperationEqualTest {

  @Test
  public void testComputeValue() {
    CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);
    EvalContext context = new EvalContext();
    Object result = equal.computeValue(context);
    assertEquals(Boolean.TRUE, result);
  }

  @Test
  public void testGetSymbol() {
    CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);
    String symbol = equal.getSymbol();
    assertEquals("=", symbol);
  }

}