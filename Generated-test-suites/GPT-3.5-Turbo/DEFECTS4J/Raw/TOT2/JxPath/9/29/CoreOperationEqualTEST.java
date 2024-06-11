import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;

    @Before
    public void setUp() {
        coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
    }

    @Test
    public void testComputeValue_True() {
        EvalContext context = new EvalContext();
        context.setVariable("a", 5);
        context.setVariable("b", 5);

        boolean result = (boolean) coreOperationEqual.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_False() {
        EvalContext context = new EvalContext();
        context.setVariable("a", 5);
        context.setVariable("b", 10);

        boolean result = (boolean) coreOperationEqual.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testGetSymbol() {
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    // Edge Case 1: Both arguments are null
    @Test
    public void testComputeValue_NullArguments() {
        EvalContext context = new EvalContext();
        context.setVariable("a", null);
        context.setVariable("b", null);

        boolean result = (boolean) coreOperationEqual.computeValue(context);

        assertTrue(result);
    }

    // Edge Case 2: Different data types comparison
    @Test
    public void testComputeValue_DifferentDataTypes() {
        EvalContext context = new EvalContext();
        context.setVariable("a", 5);
        context.setVariable("b", "5");

        boolean result = (boolean) coreOperationEqual.computeValue(context);

        assertFalse(result);
    }

    // Exception Handling Scenario: Null Context
    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullContext() {
        coreOperationEqual.computeValue(null);
    }
}