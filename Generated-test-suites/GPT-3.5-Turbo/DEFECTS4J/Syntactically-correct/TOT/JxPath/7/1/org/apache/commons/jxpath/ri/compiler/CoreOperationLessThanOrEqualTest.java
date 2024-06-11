import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_typicalCase() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new Expression(10), new Expression(20));
        EvalContext context = new EvalContext();
      
        Object result = operation.computeValue(context);
      
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
  
    @Test
    public void testComputeValue_equalCase() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new Expression(20), new Expression(20));
        EvalContext context = new EvalContext();
      
        Object result = operation.computeValue(context);
      
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
  
    @Test
    public void testComputeValue_notLessThanOrEqualCase() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new Expression(30.5), new Expression(20));
        EvalContext context = new EvalContext();
      
        Object result = operation.computeValue(context);
      
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
  
    @Test
    public void testComputeValue_errorCase() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new Expression("invalid"), new Expression(20));
        EvalContext context = new EvalContext();
      
        Object result = operation.computeValue(context);
      
        assertNull(result);
    }
  
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new Expression(10), new Expression(20));
      
        String symbol = operation.getSymbol();
      
        assertEquals("<=", symbol);
    }
}