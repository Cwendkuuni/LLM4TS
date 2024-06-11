import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression("arg1"), new Expression("arg2"));

        // Typical use case
        EvalContext context = new EvalContext();
        context.setVariable("arg1", "value1");
        context.setVariable("arg2", "value1");
        assertEquals(true, coreOperationEqual.computeValue(context));

        // Edge case - different values
        context.setVariable("arg2", "value2");
        assertEquals(false, coreOperationEqual.computeValue(context));

    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression("arg1"), new Expression("arg2"));

        // Typical use case
        assertEquals("=", coreOperationEqual.getSymbol());

        // Edge case - symbol length
        assertNotEquals("==", coreOperationEqual.getSymbol());

    }
}