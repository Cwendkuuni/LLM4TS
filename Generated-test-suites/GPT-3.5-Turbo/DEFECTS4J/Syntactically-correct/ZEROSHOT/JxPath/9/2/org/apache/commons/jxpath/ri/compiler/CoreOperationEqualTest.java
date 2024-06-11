import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual operationEqual;

    @Before
    public void setUp() {
        operationEqual = new CoreOperationEqual(null, null);
    }

    @Test
    public void testComputeValue_equalValues_returnsTrue() {
        EvalContext context = new EvalContext();

        Object result = operationEqual.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_notEqualValues_returnsFalse() {
        EvalContext context = new EvalContext();

        Object result = operationEqual.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol_returnsEqualSymbol() {
        String symbol = operationEqual.getSymbol();

        assertEquals("=", symbol);
    }
}