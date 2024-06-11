import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueEqual() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new Constant(5), new Constant(5));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        assertEquals("<", coreOperationLessThan.getSymbol());
    }
}