import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {
    
    // Test case for computeValue method: 
    // When arg1 is less than arg2, it should return true
    @Test
    public void testComputeValue_Arg1LessThanArg2_ReturnsTrue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(1), new Constant(2));
        EvalContext context = new EvalContext();
        assertTrue((boolean) operation.computeValue(context));
    }
    
    // Test case for computeValue method: 
    // When arg1 is equal to arg2, it should return false
    @Test
    public void testComputeValue_Arg1EqualsArg2_ReturnsFalse() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(2), new Constant(2));
        EvalContext context = new EvalContext();
        assertFalse((boolean) operation.computeValue(context));
    }
    
    // Test case for computeValue method: 
    // When arg1 is greater than arg2, it should return false
    @Test
    public void testComputeValue_Arg1GreaterThanArg2_ReturnsFalse() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(3), new Constant(2));
        EvalContext context = new EvalContext();
        assertFalse((boolean) operation.computeValue(context));
    }
    
    // Test case for getSymbol method: 
    // It should return "<" as the symbol
    @Test
    public void testGetSymbol_ReturnsLessThanSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(1), new Constant(2));
        assertEquals("<", operation.getSymbol());
    }
}