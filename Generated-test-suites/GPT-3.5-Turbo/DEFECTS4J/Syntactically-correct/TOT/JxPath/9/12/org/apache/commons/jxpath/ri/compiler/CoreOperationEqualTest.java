import org.junit.BeforeClass;
import org.junit.Test;

public class CoreOperationEqualTest {

    private static CoreOperationEqual coreOperationEqual;

    @BeforeClass
    public static void setUp() {
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }
    
    // Test for computeValue method
    @Test
    public void testComputeValue() {
        // Typical use case: The two arguments are equal
        EvalContext context = new EvalContext();
        context.setNodeSet(nodeSet);
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Typical use case: The two arguments are not equal
        context = new EvalContext();
        context.setNodeSet(nodeSet);
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    // Test for getSymbol method
    @Test
    public void testGetSymbol() {
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }

    // Edge case: arg1 and arg2 are null
    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullArguments() {
        EvalContext context = new EvalContext();
        coreOperationEqual = new CoreOperationEqual(null, null);
        Object result = coreOperationEqual.computeValue(context);
    }

    // Error scenario: arg1 or arg2 is null
    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullArgument() {
        EvalContext context = new EvalContext();
        context.setNodeSet(nodeSet);

        // When arg1 is null
        coreOperationEqual = new CoreOperationEqual(null, arg2);
        Object result1 = coreOperationEqual.computeValue(context);

        // When arg2 is null
        coreOperationEqual = new CoreOperationEqual(arg1, null);
        Object result2 = coreOperationEqual.computeValue(context);
    }
}