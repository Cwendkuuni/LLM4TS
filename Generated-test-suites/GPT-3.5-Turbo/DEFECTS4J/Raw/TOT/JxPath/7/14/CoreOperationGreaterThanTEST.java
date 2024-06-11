import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {
    
    private CoreOperationGreaterThan greaterThan;
    
    @Before
    public void setUp() {
        greaterThan = new CoreOperationGreaterThan(new Expression("2"), new Expression("1"));
    }
    
    // Test typical use cases
    @Test
    public void testComputeValue_typicalUseCase() {
        EvalContext context = new EvalContext();
        Object result = greaterThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    // Test edge cases
    @Test
    public void testComputeValue_edgeCase() {
        EvalContext context = new EvalContext();
        Object result = greaterThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    // Test error scenarios
    @Test
    public void testComputeValue_nullContext() {
        EvalContext context = null;
        Object result = greaterThan.computeValue(context);
        assertNull(result);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = greaterThan.getSymbol();
        assertEquals(">", symbol);
    }
}