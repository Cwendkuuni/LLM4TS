import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Basic test case
        Object result = op.computeValue(context);
        assert(result instanceof Boolean);
        
        // Edge case: Comparing two equal values
        context.setValues(new Object[]{10});
        Object result2 = op.computeValue(context);
        assert(result2 instanceof Boolean);
        assert((boolean)result2 == true);
        
        // Edge case: Comparing two different values
        context.setValues(new Object[]{10, 5});
        Object result3 = op.computeValue(context);
        assert(result3 instanceof Boolean);
        assert((boolean)result3 == false);
        
        // Edge case: Null values
        context.setValues(new Object[]{null, null});
        Object result4 = op.computeValue(context);
        assert(result4 instanceof Boolean);
        assert((boolean)result4 == true);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        // Basic test case
        String symbol = op.getSymbol();
        assert(symbol.equals("<="));
    }
}