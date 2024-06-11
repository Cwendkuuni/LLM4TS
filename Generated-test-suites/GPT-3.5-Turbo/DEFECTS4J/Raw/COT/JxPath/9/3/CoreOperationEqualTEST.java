import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    // Test computeValue method
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        // Test case 1: args[0] and args[1] are equal
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg1);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));

        // Test case 2: args[0] and args[1] are not equal
        CoreOperationEqual coreOperationEqual2 = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual2.computeValue(context));
    }

    // Test getSymbol method
    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}