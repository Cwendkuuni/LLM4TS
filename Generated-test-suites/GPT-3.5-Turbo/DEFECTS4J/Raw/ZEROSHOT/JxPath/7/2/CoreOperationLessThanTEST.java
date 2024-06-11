import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanTest {
    
    private CoreOperationLessThan operation;
    
    @Before
    public void setUp() {
        operation = new CoreOperationLessThan(null, null);
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.setContextNode(null);
        
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        
        assertEquals("<", symbol);
    }
}