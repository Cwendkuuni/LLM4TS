import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    // Instantiate CoreOperationLessThanOrEqual object for testing
    CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(null, null);

    // Test computeValue method with simple case where arg1 is less than arg2
    @Test
    public void testComputeValueLessThan() {
        EvalContext context = new EvalContext();
        Double arg1 = 10.0;
        Double arg2 = 20.0;
        context.setExpressionContext(arg1, arg2);
        assertEquals(coreOperation.computeValue(context), Boolean.TRUE);
    }

    // Test computeValue method with simple case where arg1 is equal to arg2
    @Test
    public void testComputeValueEqualTo() {
        EvalContext context = new EvalContext();
        Double arg1 = 10.0;
        Double arg2 = 10.0;
        context.setExpressionContext(arg1, arg2);
        assertEquals(coreOperation.computeValue(context), Boolean.TRUE);
    }

    // Test computeValue method with simple case where arg1 is greater than arg2
    @Test
    public void testComputeValueGreaterThan() {
        EvalContext context = new EvalContext();
        Double arg1 = 20.0;
        Double arg2 = 10.0;
        context.setExpressionContext(arg1, arg2);
        assertEquals(coreOperation.computeValue(context), Boolean.FALSE);
    }

    // Test getSymbol method to check if it returns the correct symbol
    @Test
    public void testGetSymbol() {
        assertEquals(coreOperation.getSymbol(), "<=");
    }

    // Test computeValue method with edge case where double values are at the precision limit
    @Test
    public void testComputeValuePrecisionLimit() {
        EvalContext context = new EvalContext();
        Double arg1 = Double.MAX_VALUE;
        Double arg2 = Double.MAX_VALUE;
        context.setExpressionContext(arg1, arg2);
        assertEquals(coreOperation.computeValue(context), Boolean.TRUE);
    }

    // Test computeValue method with error case where arguments are null
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArgs() {
        EvalContext context = new EvalContext();
        context.setExpressionContext(null, null);
        coreOperation.computeValue(context);
    }

}