import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {
    
    @Test
    public void testComputeValueTypical() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());

        // Typical use case: l < r, return true
        EvalContext context = new EvalContext();
        context.setVariable("l", 5);
        context.setVariable("r", 10);
        assertEquals(Boolean.TRUE, lessThan.computeValue(context));
    }
    
    @Test
    public void testComputeValueEqual() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());

        // Edge case: l = r, return false
        EvalContext context = new EvalContext();
        context.setVariable("l", 5);
        context.setVariable("r", 5);
        assertEquals(Boolean.FALSE, lessThan.computeValue(context));
    }
    
    @Test
    public void testComputeValueNotLessThan() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());

        // Edge case: l > r, return false
        EvalContext context = new EvalContext();
        context.setVariable("l", 10);
        context.setVariable("r", 5);
        assertEquals(Boolean.FALSE, lessThan.computeValue(context));
    }
    
    @Test
    public void testComputeValueError() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());

        // Error scenario: l and r are not numeric
        EvalContext context = new EvalContext();
        context.setVariable("l", "five");
        context.setVariable("r", "ten");
        assertNull(lessThan.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());

        // Symbol for less than should be "<"
        assertEquals("<", lessThan.getSymbol());
    }
}