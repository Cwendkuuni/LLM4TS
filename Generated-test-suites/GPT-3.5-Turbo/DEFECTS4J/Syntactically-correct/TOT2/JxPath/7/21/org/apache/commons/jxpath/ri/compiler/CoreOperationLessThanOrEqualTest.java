import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual coreOperationLessThanOrEqual;

    @Before
    public void setUp() {
        // Initialize CoreOperationLessThanOrEqual object with dummy arguments
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new DummyExpression(), new DummyExpression());
    }

    @Test
    public void testComputeValue_LessThanOrEqual() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 10);
        context.setVariable("arg2", 20);
        
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_NotLessThanOrEqual() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 20);
        context.setVariable("arg2", 10);
        
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = coreOperationLessThanOrEqual.getSymbol();
        
        assertEquals("<=", symbol);
    }

    // Other potential test cases for edge cases and error scenarios could be added here

}