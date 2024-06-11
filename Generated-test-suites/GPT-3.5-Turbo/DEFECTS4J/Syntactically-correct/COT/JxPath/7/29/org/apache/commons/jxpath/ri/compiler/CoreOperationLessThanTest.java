
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_lessThan() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());

        // Test case with a left value less than the right value
        NodeSet nodeSet1 = new NodeSet();
        nodeSet1.add(new Node("value", 2));
        context.setNodeSet(nodeSet1);
        Object result1 = expression.computeValue(context);
        assertTrue(result1 instanceof Boolean);
        assertTrue((Boolean) result1);

        // Test case with a left value equal to the right value
        NodeSet nodeSet2 = new NodeSet();
        nodeSet2.add(new Node("value", 2));
        context.setNodeSet(nodeSet2);
        Object result2 = expression.computeValue(context);
        assertTrue(result2 instanceof Boolean);
        assertFalse((Boolean) result2);

        // Test case with a left value greater than the right value
        NodeSet nodeSet3 = new NodeSet();
        nodeSet3.add(new Node("value", 3));
        context.setNodeSet(nodeSet3);
        Object result3 = expression.computeValue(context);
        assertTrue(result3 instanceof Boolean);
        assertFalse((Boolean) result3);
    }
}
