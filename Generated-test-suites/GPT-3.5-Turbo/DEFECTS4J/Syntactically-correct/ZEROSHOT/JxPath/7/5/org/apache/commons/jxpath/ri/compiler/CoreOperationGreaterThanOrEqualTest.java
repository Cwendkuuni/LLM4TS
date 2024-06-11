import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;

    @Test
    public void testComputeValue_LessThan() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());

        arg1.setValue(10);
        arg2.setValue(20);
        boolean result = (boolean) operation.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testComputeValue_Equal() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());

        arg1.setValue(10);
        arg2.setValue(10);
        boolean result = (boolean) operation.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testComputeValue_GreaterThan() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());

        arg1.setValue(20);
        arg2.setValue(10);
        boolean result = (boolean) operation.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testGetSymbol() {
        operation = new CoreOperationGreaterThanOrEqual(null, null);
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
}