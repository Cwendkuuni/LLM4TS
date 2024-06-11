import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_sameObject_shouldReturnFalse() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg1);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_differentObjects_shouldReturnTrue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol_shouldReturnNotEqualSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}