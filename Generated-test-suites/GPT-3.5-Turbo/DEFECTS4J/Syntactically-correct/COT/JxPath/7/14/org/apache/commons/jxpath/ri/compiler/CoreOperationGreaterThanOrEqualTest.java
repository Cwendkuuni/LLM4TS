import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // Test case to check if l >= r returns true
        EvalContext evalContext1 = createEvalContext();
        Object result1 = coreOp.computeValue(evalContext1);
        assertTrue((Boolean) result1);

        // Test case to check if l >= r returns false
        EvalContext evalContext2 = createEvalContext();
        Object result2 = coreOp.computeValue(evalContext2);
        assertFalse((Boolean) result2);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // Test case to check if getSymbol() returns ">="
        String symbol = coreOp.getSymbol();
        assertEquals(">=", symbol);
    }

    private EvalContext createEvalContext() {
        // Create and return an instance of EvalContext
        return new EvalContext();
    }
}