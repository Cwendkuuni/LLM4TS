import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValueReturnsTrue() {
        CoreOperationEqual coreOpEqual = new CoreOperationEqual(expression1, expression2);
        EvalContext context = new EvalContext();
        Object result = coreOpEqual.computeValue(context);
        assertEquals(result, Boolean.TRUE);
    }
    
    @Test
    public void testComputeValueReturnsFalse() {
        CoreOperationEqual coreOpEqual = new CoreOperationEqual(expression1, expression2);
        EvalContext context = new EvalContext();
        Object result = coreOpEqual.computeValue(context);
        assertEquals(result, Boolean.FALSE);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOpEqual = new CoreOperationEqual(expression1, expression2);
        String symbol = coreOpEqual.getSymbol();
        assertEquals(symbol, "=");
    }
}