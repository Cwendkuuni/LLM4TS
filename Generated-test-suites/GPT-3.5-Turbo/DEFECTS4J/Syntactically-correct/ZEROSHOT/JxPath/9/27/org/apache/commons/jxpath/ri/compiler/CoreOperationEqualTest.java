import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = coreOperationEqual.computeValue(context);
        
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        
        String symbol = coreOperationEqual.getSymbol();
        
        assertNotNull(symbol);
        assertTrue(symbol.equals("="));
    }
}