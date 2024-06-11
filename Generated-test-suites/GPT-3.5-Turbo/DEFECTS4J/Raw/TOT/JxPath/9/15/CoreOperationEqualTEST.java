import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {

        // Typical use cases
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext evalContext = new EvalContext();
        assertTrue(coreOperationEqual.computeValue(evalContext));

        // Edge cases
        Expression arg3 = new Expression();
        Expression arg4 = new Expression();
        CoreOperationEqual coreOperationEqual2 = new CoreOperationEqual(arg3, arg4);
        EvalContext evalContext2 = new EvalContext();
        assertTrue(coreOperationEqual2.computeValue(evalContext2));

        // Error scenarios
        Expression arg5 = null;
        Expression arg6 = null;
        CoreOperationEqual coreOperationEqual3 = new CoreOperationEqual(arg5, arg6);
        EvalContext evalContext3 = new EvalContext();
        assertFalse(coreOperationEqual3.computeValue(evalContext3));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}