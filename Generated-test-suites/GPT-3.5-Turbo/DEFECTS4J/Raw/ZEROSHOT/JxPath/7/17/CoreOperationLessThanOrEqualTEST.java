import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual coreOperationLessThanOrEqual;

    @Before
    public void setUp() {
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(null, null);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetSymbol() {
        String symbol = coreOperationLessThanOrEqual.getSymbol();
        assertNotNull(symbol);
        assertEquals("<=", symbol);
    }
}