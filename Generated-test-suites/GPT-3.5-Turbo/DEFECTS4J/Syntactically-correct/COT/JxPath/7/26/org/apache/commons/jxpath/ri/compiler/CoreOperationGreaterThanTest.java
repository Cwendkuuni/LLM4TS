import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(arg1, arg2);
        
        // Test case 1: arg1 > arg2
        EvalContext context1 = new EvalContext();
        context1.setContextNode(new Node());
        context1.setGetContextNodeList(Expression.NODESET_TYPE_MISMATCHED);
        Object result1 = greaterThan.computeValue(context1);
        assertTrue(result1 instanceof Boolean);
        assertTrue((Boolean) result1);

        // Test case 2: arg1 <= arg2
        EvalContext context2 = new EvalContext();
        context2.setContextNode(new Node());
        context2.setGetContextNodeList(Expression.NODESET_TYPE_MISMATCHED);
        Object result2 = greaterThan.computeValue(context2);
        assertTrue(result2 instanceof Boolean);
        assertFalse((Boolean) result2);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", greaterThan.getSymbol());
    }
}