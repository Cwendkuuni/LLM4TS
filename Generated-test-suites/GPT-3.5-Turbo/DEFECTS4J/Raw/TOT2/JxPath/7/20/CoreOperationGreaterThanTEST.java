import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
            new Constant(5), new Constant(3));
        
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        assertTrue((Boolean)result);
    }
    
    // Edge case - arg1 and arg2 are equal
    @Test
    public void testComputeValueEqualValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
            new Constant(2), new Constant(2));
        
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        assertFalse((Boolean)result);
    }
    
    // Error scenario - arg1 or arg2 is null
    @Test(expected = NullPointerException.class)
    public void testComputeValueNullArgument() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
            new Constant(10), null);
        
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
            new Constant(8), new Constant(6));
        
        assertEquals(">", operation.getSymbol());
    }
}