import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
    private EvalContext context = new EvalContext();
    
    @Test
    public void testComputeValue() {
        // Basic test case
        double l = 5.0;
        double r = 3.0;
        context.setValue(args[0], l);
        context.setValue(args[1], r);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }
    
    // Additional test cases for identified edge cases and exceptions
    @Test
    public void testComputeValueWithEqualValues() {
        // Edge case: l = r
        double l = 3.0;
        double r = 3.0;
        context.setValue(args[0], l);
        context.setValue(args[1], r);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testComputeValueWithNaN() {
        // Exception handling: NaN values
        double l = Double.NaN;
        double r = 3.0;
        context.setValue(args[0], l);
        context.setValue(args[1], r);
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testGetSymbol() {
        String expected = ">=";
        String result = coreOperationGreaterThanOrEqual.getSymbol();
        assertEquals(expected, result);
    }
}