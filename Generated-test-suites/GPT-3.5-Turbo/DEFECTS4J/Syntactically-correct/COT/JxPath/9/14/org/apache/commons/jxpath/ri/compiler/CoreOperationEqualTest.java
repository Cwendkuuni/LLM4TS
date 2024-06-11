import org.junit.Before;
import org.junit.Test;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;

    @Before
    public void setUp() {
        coreOperationEqual = new CoreOperationEqual(null, null);
    }

    @Test
    public void testComputeValue_Equal() {
        // Test case for equals values
        EvalContext context = null;
        Object result = coreOperationEqual.computeValue(context);

        // Assertions
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_NotEqual() {
        // Test case for not equals values
        EvalContext context = null;
        Object result = coreOperationEqual.computeValue(context);

        // Assertions
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        // Test case for getSymbol() method
        String symbol = coreOperationEqual.getSymbol();

        // Assertions
        assertEquals("=", symbol);
    }

}