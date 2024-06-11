import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case: Two equal values
        Expression arg1 = new Expression("value1");
        Expression arg2 = new Expression("value1");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(operation.computeValue(null));
        
        // Typical use case: Two different values
        arg1 = new Expression("value1");
        arg2 = new Expression("value2");
        operation = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(operation.computeValue(null));
        
        // Edge case: One value is null
        arg1 = new Expression(null);
        arg2 = new Expression("value");
        operation = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(operation.computeValue(null));
        
        // Edge case: Both values are null
        arg1 = new Expression(null);
        arg2 = new Expression(null);
        operation = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(operation.computeValue(null));
        
        // Error scenario: Invalid argument type
        arg1 = new Expression(10);
        arg2 = new Expression("20");
        operation = new CoreOperationNotEqual(arg1, arg2);
        try {
            operation.computeValue(null);
            fail("Exception not thrown for invalid argument type");
        } catch (RuntimeException e) {
            assertEquals("Invalid argument type: expected Expression, found Integer", e.getMessage());
        }
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        assertEquals("!=", operation.getSymbol());
    }
}