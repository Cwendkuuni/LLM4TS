import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_equalValues_returnTrue() {
        CoreOperationEqual operation = new CoreOperationEqual(new Constant(5), new Constant(5));
        EvalContext context = new EvalContext();
        assertTrue((Boolean) operation.computeValue(context));
    }

    @Test
    public void testComputeValue_notEqualValues_returnFalse() {
        CoreOperationEqual operation = new CoreOperationEqual(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertFalse((Boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol_correctSymbol_returnEqualSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(new Constant(5), new Constant(5));
        assertEquals("=", operation.getSymbol());
    }

    // Edge cases
    @Test
    public void testComputeValue_nullValues_returnFalse() {
        CoreOperationEqual operation = new CoreOperationEqual(null, null);
        EvalContext context = new EvalContext();
        assertFalse((Boolean) operation.computeValue(context));
    }

    // Exception handling scenarios
    @Test(expected = NullPointerException.class)
    public void testComputeValue_nullContext_throwNullPointerException() {
        CoreOperationEqual operation = new CoreOperationEqual(new Constant(5), new Constant(5));
        operation.computeValue(null);
    }
}