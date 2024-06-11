import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Typical case
        context.setContextNodePosition(2);
        context.setContextNodeSetIndex(3);
        context.setExpressionContextNodeIndex(4);
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Edge case: Both arguments are equal
        context.setContextNodePosition(5);
        context.setContextNodeSetIndex(5);
        context.setExpressionContextNodeIndex(5);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue((Boolean) result);

        // Edge case: Left argument is greater than right
        context.setContextNodePosition(8);
        context.setContextNodeSetIndex(7);
        context.setExpressionContextNodeIndex(6);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue((Boolean) result);

        // Error scenario: Invalid context
        context.setContextNodePosition(null);
        context.setContextNodeSetIndex(null);
        context.setExpressionContextNodeIndex(null);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse((Boolean) result);

    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new Expression(), new Expression());

        // Typical case
        assertEquals("<=", coreOperationLessThanOrEqual.getSymbol());

    }
}