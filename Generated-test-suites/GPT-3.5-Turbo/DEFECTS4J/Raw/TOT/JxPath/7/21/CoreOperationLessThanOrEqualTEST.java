import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    
    // Test case for computeValue() method
    @Test
    public void testComputeValue() {
        // Typical use case: l <= r, l and r are positive integers
        EvalContext context1 = new EvalContext();
        Expression arg1 = new Expression();
        arg1.setValue(5);
        Expression arg2 = new Expression();
        arg2.setValue(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, operation.computeValue(context1));
        
        // Typical use case: l <= r, l and r are negative integers
        EvalContext context2 = new EvalContext();
        Expression arg3 = new Expression();
        arg3.setValue(-5);
        Expression arg4 = new Expression();
        arg4.setValue(-10);
        CoreOperationLessThanOrEqual operation2 = new CoreOperationLessThanOrEqual(arg3, arg4);
        assertEquals(Boolean.FALSE, operation2.computeValue(context2));
        
        // Edge case: l = r, l and r are positive integers
        EvalContext context3 = new EvalContext();
        Expression arg5 = new Expression();
        arg5.setValue(5);
        Expression arg6 = new Expression();
        arg6.setValue(5);
        CoreOperationLessThanOrEqual operation3 = new CoreOperationLessThanOrEqual(arg5, arg6);
        assertEquals(Boolean.TRUE, operation3.computeValue(context3));
        
        // Edge case: l = r, l and r are negative integers
        EvalContext context4 = new EvalContext();
        Expression arg7 = new Expression();
        arg7.setValue(-5);
        Expression arg8 = new Expression();
        arg8.setValue(-5);
        CoreOperationLessThanOrEqual operation4 = new CoreOperationLessThanOrEqual(arg7, arg8);
        assertEquals(Boolean.TRUE, operation4.computeValue(context4));
        
        // Edge case: l < r
        EvalContext context5 = new EvalContext();
        Expression arg9 = new Expression();
        arg9.setValue(5);
        Expression arg10 = new Expression();
        arg10.setValue(10);
        CoreOperationLessThanOrEqual operation5 = new CoreOperationLessThanOrEqual(arg9, arg10);
        assertEquals(Boolean.TRUE, operation5.computeValue(context5));
        
        // Edge case: l > r
        EvalContext context6 = new EvalContext();
        Expression arg11 = new Expression();
        arg11.setValue(10);
        Expression arg12 = new Expression();
        arg12.setValue(5);
        CoreOperationLessThanOrEqual operation6 = new CoreOperationLessThanOrEqual(arg11, arg12);
        assertEquals(Boolean.FALSE, operation6.computeValue(context6));
    }
    
    // Test case for getSymbol() method
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        assertEquals("<=", operation.getSymbol());
    }
}