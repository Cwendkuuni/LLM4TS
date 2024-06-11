import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Test case 1: arg1 >= arg2, both arg1 and arg2 are numeric values
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new NumericValue(5), new NumericValue(3));
        EvalContext context = new EvalContext();
        assertTrue((Boolean) operation.computeValue(context));

        // Test case 2: arg1 < arg2, both arg1 and arg2 are numeric values
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new NumericValue(3), new NumericValue(5));
        EvalContext context = new EvalContext();
        assertFalse((Boolean) operation.computeValue(context));

        // Test case 3: arg1 >= arg2, arg1 is a numeric value and arg2 is a string
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new NumericValue(5), new StringValue("3"));
        EvalContext context = new EvalContext();
        assertTrue((Boolean) operation.computeValue(context));

        // Test case 4: arg1 < arg2, arg1 is a numeric value and arg2 is a string
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new NumericValue(3), new StringValue("5"));
        EvalContext context = new EvalContext();
        assertFalse((Boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new NumericValue(5), new NumericValue(3));
        assertEquals(">=", operation.getSymbol());
    }

}