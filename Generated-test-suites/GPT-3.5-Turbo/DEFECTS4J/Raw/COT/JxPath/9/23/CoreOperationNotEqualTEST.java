
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValueNotEqual() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());

        context.setValue("value1", "abc");
        context.setValue("value2", "def");
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);

        context.setValue("value1", null);
        context.setValue("value2", "def");
        result = operation.computeValue(context);
        assertTrue((Boolean) result);

        // Test with different object types
        context.setValue("value1", 123);
        context.setValue("value2", "456");
        result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueEqual() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());

        context.setValue("value1", "abc");
        context.setValue("value2", "abc");
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);

        context.setValue("value1", null);
        context.setValue("value2", null);
        result = operation.computeValue(context);
        assertFalse((Boolean) result);

        // Test with different object types
        context.setValue("value1", 123);
        context.setValue("value2", 123);
        result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        assertEquals("!=", operation.getSymbol());
    }
}
