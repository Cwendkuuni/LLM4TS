import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {
  
  private CoreOperationEqual coreOperationEqual;
  
  @Before
  public void setUp() {
    coreOperationEqual = new CoreOperationEqual(arg1, arg2);
  }
  
  @Test
  public void testComputeValue_equalToTrue() {
    EvalContext context = new EvalContext();
    Object result = coreOperationEqual.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertEquals(true, result);
  }
  
  @Test
  public void testComputeValue_equalToFalse() {
    EvalContext context = new EvalContext();
    Object result = coreOperationEqual.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertEquals(false, result);
  }
  
  @Test
  public void testGetSymbol() {
    String symbol = coreOperationEqual.getSymbol();
    assertNotNull(symbol);
    assertEquals("=", symbol);
  }
}