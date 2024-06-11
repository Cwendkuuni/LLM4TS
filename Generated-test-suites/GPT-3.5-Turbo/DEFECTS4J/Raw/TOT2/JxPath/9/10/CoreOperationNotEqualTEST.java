import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Constant(5), new Constant(10));
        assertFalse((Boolean) operation.computeValue(null));
        
        // Edge cases
        // Test when both arguments are null
        operation = new CoreOperationNotEqual(null, null);
        assertTrue((Boolean) operation.computeValue(null));
        
        // Test when one argument is null
        operation = new CoreOperationNotEqual(new Constant("test"), null);
        assertTrue((Boolean) operation.computeValue(null));
        operation = new CoreOperationNotEqual(null, new Constant("test"));
        assertTrue((Boolean) operation.computeValue(null));
        
        // Error scenarios
        // Test when arguments are of different types
        operation = new CoreOperationNotEqual(new Constant("5"), new Constant(5));
        assertTrue((Boolean) operation.computeValue(null));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        assertEquals("!=", operation.getSymbol());
    }
}