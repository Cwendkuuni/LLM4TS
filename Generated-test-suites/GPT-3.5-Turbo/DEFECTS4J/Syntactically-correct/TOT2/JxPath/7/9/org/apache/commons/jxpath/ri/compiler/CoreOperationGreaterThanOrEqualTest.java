import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_SmallerValues_ShouldReturnFalse() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(10));
        assertFalse((Boolean) operation.computeValue(null));
    }

    @Test
    public void testComputeValue_EqualValues_ShouldReturnTrue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(10), new Constant(10));
        assertTrue((Boolean) operation.computeValue(null));
    }

    @Test
    public void testComputeValue_LargeValues_ShouldReturnTrue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(100), new Constant(50));
        assertTrue((Boolean) operation.computeValue(null));
    }

    @Test
    public void testGetSymbol_ShouldReturnGreaterOrEqualSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        assertEquals(">=", operation.getSymbol());
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullArgs_ShouldThrowNullPointerException() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        operation.computeValue(null);
    }
}