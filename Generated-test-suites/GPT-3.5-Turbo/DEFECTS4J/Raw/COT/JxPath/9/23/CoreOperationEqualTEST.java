import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        // Test case 1: arg1 = arg2
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Test case 2: arg1 != arg2
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        // Test case 1: Verify symbol is "="
        assertEquals("=", operation.getSymbol());
    }
}