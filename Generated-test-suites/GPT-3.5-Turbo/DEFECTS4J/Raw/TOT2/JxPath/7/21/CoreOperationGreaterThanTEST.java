import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan coreOperationGreaterThan;

    @Before
    public void setUp() {
        coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        assertEquals(">", coreOperationGreaterThan.getSymbol());
    }

    // Edge Cases
    @Test
    public void testComputeValueWithZeroAsArguments() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant(0), new Constant(0));
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse((Boolean) result);
    }

    // Error Scenario
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArguments() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(null, null);
        EvalContext context = new EvalContext();
        coreOperationGreaterThan.computeValue(context);
    }
}