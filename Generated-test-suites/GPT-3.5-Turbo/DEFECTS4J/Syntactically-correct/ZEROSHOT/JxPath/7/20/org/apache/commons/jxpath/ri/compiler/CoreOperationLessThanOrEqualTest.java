import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    private CoreOperationLessThanOrEqual operation;

    @Before
    public void setUp() {
        operation = new CoreOperationLessThanOrEqual(new ExpressionMock(), new ExpressionMock());
    }

    @Test
    public void testComputeValue_LessThanOrEqualReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setNodeSet(new Object[]{1, 2, 3, 4});

        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LessThanOrEqualReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setNodeSet(new Object[]{4, 3, 2, 1});

        Object result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();

        assertEquals("<=", symbol);
    }

    private class ExpressionMock extends Expression {
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}