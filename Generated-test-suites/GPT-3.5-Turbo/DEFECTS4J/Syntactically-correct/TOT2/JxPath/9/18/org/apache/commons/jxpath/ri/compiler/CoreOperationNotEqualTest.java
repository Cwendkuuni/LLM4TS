import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();

        // Typical use case
        context.setContextNodePointer(new NodePointer());
        context.setNodeSet(new NodeSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Edge case: arg1 and arg2 are equal
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue("value");
        arg2.setValue("value");
        operation = new CoreOperationNotEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Exception handling scenario
        try {
            context.setContextNodePointer(null);
            context.setNodeSet(null);
            operation.computeValue(context);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());

        // Typical use case
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);

        // Edge case: no args provided
        operation = new CoreOperationNotEqual(null, null);
        symbol = operation.getSymbol();
        assertEquals("!=", symbol);

        // Exception handling scenario
        try {
            operation = new CoreOperationNotEqual(null, null);
            operation.getSymbol();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}