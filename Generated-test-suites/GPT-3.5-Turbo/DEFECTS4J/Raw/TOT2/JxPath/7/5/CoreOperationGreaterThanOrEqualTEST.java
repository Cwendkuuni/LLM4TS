import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual coreOperation;

    @Before
    public void setUp() {
        coreOperation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(3));
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = coreOperation.getSymbol();
        assertEquals(">=", symbol);
    }

    @Test
    public void testComputeValueEdgeCases() {
        // Testing when both values are equal
        CoreOperationGreaterThanOrEqual equalValuesOperation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(5));
        EvalContext context = new EvalContext();
        Object result = equalValuesOperation.computeValue(context);
        assertTrue((Boolean) result);

        // Testing when the first value is greater
        CoreOperationGreaterThanOrEqual firstGreaterOperation = new CoreOperationGreaterThanOrEqual(new Constant(7), new Constant(5));
        context = new EvalContext();
        result = firstGreaterOperation.computeValue(context);
        assertTrue((Boolean) result);

        // Testing when the second value is greater
        CoreOperationGreaterThanOrEqual secondGreaterOperation = new CoreOperationGreaterThanOrEqual(new Constant(3), new Constant(5));
        context = new EvalContext();
        result = secondGreaterOperation.computeValue(context);
        assertFalse((Boolean) result);
    }

}