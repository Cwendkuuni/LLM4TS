import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Typical case where left is less than right
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertTrue((Boolean) coreOperationLessThan.computeValue(context));

        // Case where left is greater than right
        CoreOperationLessThan coreOperationLessThan2 = new CoreOperationLessThan(new Constant(20), new Constant(10));
        assertFalse((Boolean) coreOperationLessThan2.computeValue(context));

        // Case where left is equal to right
        CoreOperationLessThan coreOperationLessThan3 = new CoreOperationLessThan(new Constant(15), new Constant(15));
        assertFalse((Boolean) coreOperationLessThan3.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        assertEquals("<", coreOperationLessThan.getSymbol());
    }
}