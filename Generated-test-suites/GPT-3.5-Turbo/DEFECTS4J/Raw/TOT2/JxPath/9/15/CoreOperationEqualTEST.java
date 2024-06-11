import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_EqualArgs_ReturnsTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(5), new Constant(5));
        EvalContext context = new EvalContext();
        assertTrue((Boolean) coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_NotEqualArgs_ReturnsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertFalse((Boolean) coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol_ReturnsEqualSign() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(5), new Constant(5));
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    @Test
    public void testEqual_NullArgs_ReturnsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        EvalContext context = new EvalContext();
        assertFalse((Boolean) coreOperationEqual.computeValue(context));
    }

    @Test(expected = Exception.class)
    public void testComputeValue_ExceptionThrown() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(5), new Constant(5));
        EvalContext context = null;
        coreOperationEqual.computeValue(context);
    }
}