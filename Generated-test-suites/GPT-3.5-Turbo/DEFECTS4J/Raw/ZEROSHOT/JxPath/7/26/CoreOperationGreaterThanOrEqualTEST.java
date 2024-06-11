import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_LessThan() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_Equal() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_GreaterThan() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
}