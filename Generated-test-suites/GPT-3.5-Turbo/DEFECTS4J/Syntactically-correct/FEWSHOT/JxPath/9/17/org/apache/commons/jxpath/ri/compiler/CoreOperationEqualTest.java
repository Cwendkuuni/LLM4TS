import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        CoreOperationCompare arg1 = new CoreOperationCompare();
        CoreOperationCompare arg2 = new CoreOperationCompare();

        arg1.setValue(5);
        arg2.setValue(10);

        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);

        assertFalse(operationEqual.computeValue(context));

        arg2.setValue(5);

        assertTrue(operationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(null, null);

        assertEquals("=", operationEqual.getSymbol());
    }
}