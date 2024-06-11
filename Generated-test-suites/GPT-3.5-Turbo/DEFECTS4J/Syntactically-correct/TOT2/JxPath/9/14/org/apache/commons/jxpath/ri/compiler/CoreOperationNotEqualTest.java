import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testConstructor() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(expression1, expression2);
        assertNotNull(coreOperation);
    }

    @Test
    public void testComputeValueNotEqual() {
        EvalContext context = new EvalContext();
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(expression1, expression2);
        context.setContextNodePointer(nodePointer);
        boolean result = (Boolean) coreOperation.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testComputeValueEqual() {
        EvalContext context = new EvalContext();
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(expression1, expression1); // Same expression 1 to test equal case
        context.setContextNodePointer(nodePointer);
        boolean result = (Boolean) coreOperation.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(expression1, expression2);
        assertEquals("!=", coreOperation.getSymbol());
    }
}