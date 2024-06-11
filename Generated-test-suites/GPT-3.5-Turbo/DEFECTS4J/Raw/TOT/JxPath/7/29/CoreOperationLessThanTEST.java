import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    // Test cases for computeValue method

    @Test
    public void testComputeValueTypicalCase() {
        EvalContext context = new EvalContext();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(3), new Expression(5));
        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValueEqualValues() {
        EvalContext context = new EvalContext();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(4), new Expression(4));
        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueFloatValues() {
        EvalContext context = new EvalContext();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(1.5F), new Expression(2.5F));
        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    // Test cases for getSymbol method

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(3), new Expression(5));
        String symbol = lessThan.getSymbol();
        assertEquals("<", symbol);
    }

}