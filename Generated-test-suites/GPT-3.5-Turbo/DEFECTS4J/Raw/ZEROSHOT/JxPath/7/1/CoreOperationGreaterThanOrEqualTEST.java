import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setUp() {
        expression = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
    }

    @Test
    public void testComputeValue() {
        // Test for l >= r
        EvalContext context = new EvalContext();
        context.setNodeSet(new Object[]{10, 5});
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);

        // Test for l < r
        context.setNodeSet(new Object[]{5, 10});
        result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
}