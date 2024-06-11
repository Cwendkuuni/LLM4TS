import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValueWithEqualArguments() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(arg1, arg1);
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertTrue("Expected result is Boolean.FALSE", result.equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValueWithDifferentArguments() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertTrue("Expected result is Boolean.TRUE", result.equals(Boolean.TRUE));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(arg1, arg2);
        String symbol = coreOperation.getSymbol();
        assertNotNull("Expected symbol is not null", symbol);
        assertEquals("Expected symbol is '!='", "!=", symbol);
    }

}