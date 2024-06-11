import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(expression1, expression2);

        // Typical use cases
        assertTrue((Boolean) operation.computeValue(context1));
        assertTrue((Boolean) operation.computeValue(context2));
        assertFalse((Boolean) operation.computeValue(context3));

        // Edge cases
        assertTrue((Boolean) operation.computeValue(context4));
        assertFalse((Boolean) operation.computeValue(context5));
        assertFalse((Boolean) operation.computeValue(context6));

        // Error scenarios
        assertFalse((Boolean) operation.computeValue(context7));
        assertFalse((Boolean) operation.computeValue(context8));
        assertFalse((Boolean) operation.computeValue(context9));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(expression1, expression2);

        assertEquals("<=", operation.getSymbol());
    }

}