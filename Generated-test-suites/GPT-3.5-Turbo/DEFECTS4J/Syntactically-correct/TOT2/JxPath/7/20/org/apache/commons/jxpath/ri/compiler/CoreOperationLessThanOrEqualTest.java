import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical case
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(10));
        assertEquals(Boolean.TRUE, operation.computeValue(null));
        
        // Edge case
        operation = new CoreOperationLessThanOrEqual(new Constant(10), new Constant(5));
        assertEquals(Boolean.FALSE, operation.computeValue(null));
        
        // Error scenario - Null value
        operation = new CoreOperationLessThanOrEqual(new Constant(null), new Constant(5));
        assertEquals(Boolean.FALSE, operation.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(10));
        assertEquals("<=", operation.getSymbol());
    }

}