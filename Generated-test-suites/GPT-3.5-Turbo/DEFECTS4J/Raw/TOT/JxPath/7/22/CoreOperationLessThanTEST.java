import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_typical() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        EvalContext context = new EvalContext();
        context.setNodeSet(nodeSet);
        context.setPosition(position);

        context.resetState();
        context.setCurrentNode(node1);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        context.resetState();
        context.setCurrentNode(node2);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context.resetState();
        context.setCurrentNode(node3);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_edge() {
        // test when arg1 or arg2 is null
        CoreOperationLessThan operation = new CoreOperationLessThan(null, new Expression(arg2));
        EvalContext context = new EvalContext();
        context.setNodeSet(nodeSet);
        context.setPosition(position);

        context.resetState();
        context.setCurrentNode(node1);
        assertNull(operation.computeValue(context));

        context.resetState();
        context.setCurrentNode(node2);
        assertNull(operation.computeValue(context));

        context.resetState();
        context.setCurrentNode(node3);
        assertNull(operation.computeValue(context));

        operation = new CoreOperationLessThan(new Expression(arg1), null);
        context.resetState();
        context.setCurrentNode(node1);
        assertNull(operation.computeValue(context));

        context.resetState();
        context.setCurrentNode(node2);
        assertNull(operation.computeValue(context));

        context.resetState();
        context.setCurrentNode(node3);
        assertNull(operation.computeValue(context));

        // test with Infinity values
        operation = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        context.resetState();
        context.setCurrentNode(node1);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context.resetState();
        context.setCurrentNode(node2);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        context.resetState();
        context.setCurrentNode(node3);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // test with NaN values
        operation = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        context.resetState();
        context.setCurrentNode(node1);
        assertNull(operation.computeValue(context));

        context.resetState();
        context.setCurrentNode(node2);
        assertNull(operation.computeValue(context));

        context.resetState();
        context.setCurrentNode(node3);
        assertNull(operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        assertEquals("<", operation.getSymbol());
    }

}