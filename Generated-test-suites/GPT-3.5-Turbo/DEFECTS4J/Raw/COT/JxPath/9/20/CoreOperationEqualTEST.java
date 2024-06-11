import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual operation;

    @Before
    public void setUp() {
        operation = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_equalValues() {
        EvalContext context = new EvalContext();
        Object arg1 = 10;
        Object arg2 = 10;
        
        boolean result = (Boolean) operation.computeValue(context);
        
        assertTrue(result);
    }

    @Test
    public void testComputeValue_notEqualValues() {
        EvalContext context = new EvalContext();
        Object arg1 = 10;
        Object arg2 = 20;
        
        boolean result = (Boolean) operation.computeValue(context);
        
        assertFalse(result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        
        assertEquals("=", symbol);
    }

    @Test
    public void testCustomEdgeCase1() {
        // TODO: Add specific edge case test
    }

    @Test
    public void testCustomEdgeCase2() {
        // TODO: Add specific edge case test
    }

    @Test
    public void testCustomExceptionHandling1() {
        // TODO: Add specific exception handling test
    }

    @Test
    public void testCustomExceptionHandling2() {
        // TODO: Add specific exception handling test
    }
}

