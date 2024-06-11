import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    // Test for computeValue method
    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        // Typical use case
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        context.getVars().declareVariable("arg1", "value1");
        context.getVars().declareVariable("arg2", "value2");
        assertFalse(operation.computeValue(context));

        // Edge case - when arg1 is null and arg2 is null
        context.getVars().declareVariable("arg1", null);
        context.getVars().declareVariable("arg2", null);
        assertFalse(operation.computeValue(context));

        // Edge case - when arg1 is a different object and arg2 is null
        context.getVars().declareVariable("arg1", new Object());
        context.getVars().declareVariable("arg2", null);
        assertTrue(operation.computeValue(context));

        // Error scenario - when arg1 is not of type Expression
        context.getVars().declareVariable("arg1", "value1");
        context.getVars().declareVariable("arg2", "value2");
        try {
            operation.computeValue(context);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Arguments should be of type Expression", e.getMessage());
        }
    }

    // Test for getSymbol method
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        assertEquals("!=", operation.getSymbol());
    }
}