import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;

    @Test
    public void testComputeValue_equalValues() {
        coreOperationEqual = new CoreOperationEqual(new Constant(5), new Constant(5));
        EvalContext context = new EvalContext();
        assertTrue((Boolean) coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_notEqualValues() {
        coreOperationEqual = new CoreOperationEqual(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertFalse((Boolean) coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        coreOperationEqual = new CoreOperationEqual(new Constant(5), new Constant(5));
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    @Test
    public void testComputeValue_nullArguments() {
        coreOperationEqual = new CoreOperationEqual(null, null);
        EvalContext context = new EvalContext();
        assertFalse((Boolean) coreOperationEqual.computeValue(context));
    }

}