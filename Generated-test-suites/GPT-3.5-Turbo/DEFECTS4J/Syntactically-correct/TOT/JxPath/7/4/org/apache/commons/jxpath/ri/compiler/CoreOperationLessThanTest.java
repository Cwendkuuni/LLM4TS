import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // typical use case
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(10), new Expression(20));
        EvalContext context = new EvalContext();
        assertTrue((Boolean) operation.computeValue(context));

        // edge case - same values
        operation = new CoreOperationLessThan(new Expression(10), new Expression(10));
        assertFalse((Boolean) operation.computeValue(context));

        // edge case - negative values
        operation = new CoreOperationLessThan(new Expression(-5), new Expression(5));
        assertTrue((Boolean) operation.computeValue(context));

        // error scenario - null values
        operation = new CoreOperationLessThan(null, new Expression(10));
        try {
            operation.computeValue(context);
        } catch (NullPointerException e) {
            assertEquals("Args cannot be null", e.getMessage());
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(10), new Expression(20));
        assertEquals("<", operation.getSymbol());
    }
}