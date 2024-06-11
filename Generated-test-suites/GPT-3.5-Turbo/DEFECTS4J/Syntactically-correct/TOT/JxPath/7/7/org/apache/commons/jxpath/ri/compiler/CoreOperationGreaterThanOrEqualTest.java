import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    EvalContext context = new EvalContext();

    @Test
    public void testComputeValueTypicalCase() {
        arg1 = new Expression(5);
        arg2 = new Expression(2);
        assertEquals(false, operation.computeValue(context));
    }

    @Test
    public void testComputeValueEdgeCase() {
        arg1 = new Expression(Double.MAX_VALUE);
        arg2 = new Expression(Double.MAX_VALUE);
        assertEquals(true, operation.computeValue(context));
    }

    @Test
    public void testComputeValueErrorCase() {
        arg1 = new Expression("abc");
        arg2 = new Expression(2);
        assertNull(operation.computeValue(context));
    }
}