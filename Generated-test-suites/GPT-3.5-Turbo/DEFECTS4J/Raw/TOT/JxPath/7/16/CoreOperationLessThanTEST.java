
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    // Test computeValue method
    @Test
    public void testComputeValue() {
        // Test with arguments that evaluate to less than comparison result
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        boolean result = (boolean) lessThan.computeValue(null);
        assertTrue(result);
        
        // Test with arguments that evaluate to equal comparison result
        Expression arg3 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        Expression arg4 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        CoreOperationLessThan equalTo = new CoreOperationLessThan(arg3, arg4);
        result = (boolean) equalTo.computeValue(null);
        assertFalse(result);
        
        // Test with arguments that evaluate to greater than comparison result
        Expression arg5 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 8;
            }
        };
        Expression arg6 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        CoreOperationLessThan greaterThan = new CoreOperationLessThan(arg5, arg6);
        result = (boolean) greaterThan.computeValue(null);
        assertFalse(result);
    }

    // Test getSymbol method
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        assertEquals("<", lessThan.getSymbol());
    }
    
}
