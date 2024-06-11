import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        
        EvalContext context = new EvalContext();
        boolean result = (boolean) coreOperationEqual.computeValue(context);
        assertEquals(false, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
    
}