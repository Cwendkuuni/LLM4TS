import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // typical use case: different values
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // typical use case: same values
        arg1 = new Expression();
        arg2 = new Expression();
        operation = new CoreOperationNotEqual(arg1, arg2);
        context = createContext();
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // edge case: null values
        arg1 = null;
        arg2 = null;
        operation = new CoreOperationNotEqual(arg1, arg2);
        context = createContext();
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // edge case: one null value
        arg1 = null;
        arg2 = new Expression();
        operation = new CoreOperationNotEqual(arg1, arg2);
        context = createContext();
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // edge case: one null value (reverse order)
        arg1 = new Expression();
        arg2 = null;
        operation = new CoreOperationNotEqual(arg1, arg2);
        context = createContext();
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // error scenario: invalid arguments
        arg1 = new Expression();
        arg2 = new Expression();
        operation = new CoreOperationNotEqual(arg1, arg2);
        context = createContext();
        // Invalid EvalContext: set some invalid field value
        context.setInvalidField(true);
        result = operation.computeValue(context);
        assertNull(result); // or throw exception, depending on implementation
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
    
    private EvalContext createContext() {
        // create and return a valid EvalContext for testing
    }
}