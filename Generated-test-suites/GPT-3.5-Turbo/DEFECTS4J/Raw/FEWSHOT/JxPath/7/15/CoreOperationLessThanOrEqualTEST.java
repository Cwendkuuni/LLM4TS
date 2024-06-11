import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual operation;
    private EvalContext context;

    @Before
    public void setup() {
        operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        context = new EvalContext();
    }

    @Test
    public void testComputeValue() {
        context.setVariable("arg1", 10);
        context.setVariable("arg2", 20);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        context.setVariable("arg1", 20);
        context.setVariable("arg2", 10);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context.setVariable("arg1", 10);
        context.setVariable("arg2", 10);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        assertEquals("<=", operation.getSymbol());
    }
}