import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue_LessThan() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        context.setCurrentNode(new Node());

        Object result = operation.computeValue(context);

        assertEquals(result, Boolean.FALSE);
    }

    @Test
    public void testComputeValue_GreaterThan() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        context.setCurrentNode(new Node());

        Object result = operation.computeValue(context);

        assertEquals(result, Boolean.TRUE);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());

        String symbol = operation.getSymbol();

        assertEquals(symbol, ">");
    }
}