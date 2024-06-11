import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Typical use case
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);

        // Edge case
        arg1 = new Constant(10);
        arg2 = new Constant(5);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertFalse((Boolean) result);

        // Error scenario - NullPointerException
        try {
            operation = new CoreOperationLessThanOrEqual(null, arg2);
            operation.computeValue(context);
            fail("Expected NullPointerException did not occur");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        // Typical use case
        String symbol = operation.getSymbol();
        assertEquals("<=", symbol);
    }
}