import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use cases
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        
        arg1.computeValue(context);
        arg2.computeValue(context);
        
        assertTrue((double)arg1.computeValue(context) <= (double)arg2.computeValue(context));
        
        // Edge cases
        // arg1 and arg2 are both large numbers
        Expression arg1LargeNum = new Expression(1.7976931348623157E308); // Max double value
        Expression arg2LargeNum = new Expression(1.7976931348623157E308); // Max double value
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqualLargeNum = new CoreOperationLessThanOrEqual(arg1LargeNum, arg2LargeNum);
        
        EvalContext contextLargeNum = new EvalContext();
        
        arg1LargeNum.computeValue(contextLargeNum);
        arg2LargeNum.computeValue(contextLargeNum);
        
        assertTrue((double)arg1LargeNum.computeValue(contextLargeNum) <= (double)arg2LargeNum.computeValue(contextLargeNum));
        
        // arg1 and arg2 are both small numbers
        Expression arg1SmallNum = new Expression(2.2250738585072014E-308); // Min double value
        Expression arg2SmallNum = new Expression(2.2250738585072014E-308); // Min double value
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqualSmallNum = new CoreOperationLessThanOrEqual(arg1SmallNum, arg2SmallNum);
        
        EvalContext contextSmallNum = new EvalContext();
        
        arg1SmallNum.computeValue(contextSmallNum);
        arg2SmallNum.computeValue(contextSmallNum);
        
        assertTrue((double)arg1SmallNum.computeValue(contextSmallNum) <= (double)arg2SmallNum.computeValue(contextSmallNum));
        
        // Error scenarios
        // arg1 is null
        Expression arg1Null = null;
        Expression arg2Error = new Expression();
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqualNull1 = new CoreOperationLessThanOrEqual(arg1Null, arg2Error);
        
        EvalContext contextNull1 = new EvalContext();
        
        try {
            arg2Error.computeValue(contextNull1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
        
        // arg2 is null
        Expression arg1Error = new Expression();
        Expression arg2Null = null;
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqualNull2 = new CoreOperationLessThanOrEqual(arg1Error, arg2Null);
        
        EvalContext contextNull2 = new EvalContext();
        
        try {
            arg1Error.computeValue(contextNull2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
        
    }

    @Test
    public void testGetSymbol() {
        // Test the getSymbol method
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertEquals("<=", coreOperationLessThanOrEqual.getSymbol());
    }

}