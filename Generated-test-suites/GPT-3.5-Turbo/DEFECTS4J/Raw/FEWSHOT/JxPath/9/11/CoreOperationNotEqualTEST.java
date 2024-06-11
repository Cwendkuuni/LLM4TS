import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    @Test
    public void testComputeValue() {
        // Test when arg1 and arg2 are equal
        CoreOperationNotEqual operation1 = new CoreOperationNotEqual(arg1, arg2);
        assertFalse((Boolean)operation1.computeValue(context));

        // Test when arg1 and arg2 are not equal
        CoreOperationNotEqual operation2 = new CoreOperationNotEqual(arg1, arg2);
        assertTrue((Boolean)operation2.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("!=", operation.getSymbol());
    }
}