
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    // Test computeValue() method
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        // Test for arg1 > arg2
        EvalContext context1 = new EvalContext();
        context1.setContextNodeSet(new NodeSet());
        context1.setExpressionContext(ExpressionContext.newContext(context1.getContextNodeSet()));
        context1.setValue("a", 5);
        context1.setValue("b", 2);
        assertTrue(operation.computeValue(context1));

        // Test for arg1 <= arg2
        EvalContext context2 = new EvalContext();
        context2.setContextNodeSet(new NodeSet());
        context2.setExpressionContext(ExpressionContext.newContext(context2.getContextNodeSet()));
        context2.setValue("x", 5);
        context2.setValue("y", 5);
        assertFalse(operation.computeValue(context2));
    }

    // Test getSymbol() method
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", operation.getSymbol());
    }
}
