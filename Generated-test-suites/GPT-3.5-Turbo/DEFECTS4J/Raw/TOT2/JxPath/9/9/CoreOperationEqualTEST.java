import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValueEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(5), new Constant(5));
        assertTrue((Boolean) coreOperationEqual.computeValue(null));
    }

    @Test
    public void testComputeValueNotEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(5), new Constant(10));
        assertFalse((Boolean) coreOperationEqual.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(5), new Constant(5));
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    @Test
    public void testComputeValueNullArguments() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        assertFalse((Boolean) coreOperationEqual.computeValue(null));
    }

    @Test
    public void testComputeValueException() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Constant(5), null);
        assertThrows(NullPointerException.class, () -> coreOperationEqual.computeValue(null));
    }
}