import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValue() {
        // Typical use case: evaluating two different arguments should return true
        CoreOperationNotEqual operation = new CoreOperationNotEqual(
            new Literal("'hello'"),
            new Literal("'world'")
        );
        assertTrue(operation.computeValue(null).equals(Boolean.TRUE));
        
        // Edge case: evaluating two identical arguments should return false
        CoreOperationNotEqual operation2 = new CoreOperationNotEqual(
            new Literal("10"),
            new Literal("10")
        );
        assertTrue(operation2.computeValue(null).equals(Boolean.FALSE));
    }
    
    @Test
    public void testGetSymbol() {
        // Typical use case: getSymbol() should return "!="
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        assertEquals("!=", operation.getSymbol());
    }
}