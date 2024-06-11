import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    CoreOperationEqual coreOperationEqual;

    @Test
    public void testComputeValue() {
        // Typical use case
        coreOperationEqual = new CoreOperationEqual(new Constant(5), new Constant(5));
        assertTrue((Boolean) coreOperationEqual.computeValue(null));

        // Edge case
        coreOperationEqual = new CoreOperationEqual(new Constant(null), new Constant(null));
        assertTrue((Boolean) coreOperationEqual.computeValue(null));

        // Error scenario
        coreOperationEqual = new CoreOperationEqual(new Constant("abc"), new Constant("def"));
        assertFalse((Boolean) coreOperationEqual.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        // Typical use case
        coreOperationEqual = new CoreOperationEqual(new Constant(10), new Constant(10));
        assertEquals("=", coreOperationEqual.getSymbol());

        // Edge case
        coreOperationEqual = new CoreOperationEqual(new Constant(null), new Constant(null));
        assertEquals("=", coreOperationEqual.getSymbol());

        // Error scenario
        coreOperationEqual = new CoreOperationEqual(new Constant("abc"), new Constant("def"));
        assertEquals("=", coreOperationEqual.getSymbol());
    }

}