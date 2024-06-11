import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_basic() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_edgeCase() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new Constant(0), new Constant(0));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_errorScenario() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new Constant("invalid"), new Constant(10));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol_basic() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new Constant(5), new Constant(10));
        assertEquals("<=", operation.getSymbol());
    }

    @Test
    public void testGetSymbol_edgeCase() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new Constant(-5), new Constant(-10));
        assertEquals("<=", operation.getSymbol());
    }

    @Test
    public void testGetSymbol_nullCheck() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new Constant(null), new Constant(10));
        assertNull(operation.getSymbol());
    }
}