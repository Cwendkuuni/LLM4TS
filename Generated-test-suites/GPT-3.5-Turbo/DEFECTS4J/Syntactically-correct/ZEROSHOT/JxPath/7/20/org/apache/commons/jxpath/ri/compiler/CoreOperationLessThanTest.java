import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {
  
  private CoreOperationLessThan coreOperationLessThan;
  
  @Before
  public void setUp() {
    coreOperationLessThan = new CoreOperationLessThan(new Expression(), new Expression());
  }
  
  @Test
  public void testComputeValueLessThan() {
    EvalContext context = new EvalContext();
    context.setContextNode(new Node());
    
    Object result = coreOperationLessThan.computeValue(context);
    
    assertTrue(result instanceof Boolean);
    assertTrue((boolean) result);
  }
  
  @Test
  public void testComputeValueNotLessThan() {
    EvalContext context = new EvalContext();
    context.setContextNode(new Node());
    context.setVariable("someVar", 10);
    
    Object result = coreOperationLessThan.computeValue(context);
    
    assertTrue(result instanceof Boolean);
    assertFalse((boolean) result);
  }
  
  @Test
  public void testGetSymbol() {
    String symbol = coreOperationLessThan.getSymbol();
    
    assertNotNull(symbol);
    assertEquals("<", symbol);
  }
  
}