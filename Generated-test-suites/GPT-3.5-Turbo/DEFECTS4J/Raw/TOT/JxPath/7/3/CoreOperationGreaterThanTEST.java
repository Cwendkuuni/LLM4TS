import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {
    
    @Test
    public void testComputeValue_typicalCase() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("2"), new Expression("1"));
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_equalValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("2"), new Expression("2"));
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertFalse(result);
    }
    
    @Test
    public void testComputeValue_negativeValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("-2"), new Expression("-1"));
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertFalse(result);
    }
    
    @Test
    public void testComputeValue_decimalValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("2.5"), new Expression("1.5"));
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_nullValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(null), new Expression("1"));
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertFalse(result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testComputeValue_nullContext() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("2"), new Expression("1"));
        
        Object result = operation.computeValue(null);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("2"), new Expression("1"));
        
        String symbol = operation.getSymbol();
        
        assertEquals(">", symbol);
    }
}