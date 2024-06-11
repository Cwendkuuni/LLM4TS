import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanTest {
    private CoreOperationLessThan coreOperationLessThan;

    @Before
    public void setUp() {
        coreOperationLessThan = new CoreOperationLessThan(new Expression(), new Expression());
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.setVariable("x", 5);
        context.setVariable("y", 10);
        coreOperationLessThan.args[0] = new Expression("x");
        coreOperationLessThan.args[1] = new Expression("y");
        
        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test
    public void testGetSymbol() {
        assertEquals("<", coreOperationLessThan.getSymbol());
    }
}