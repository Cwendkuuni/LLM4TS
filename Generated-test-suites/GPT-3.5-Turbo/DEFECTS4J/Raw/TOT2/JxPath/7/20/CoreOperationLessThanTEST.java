import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, lessThan.computeValue(context));

        CoreOperationLessThan lessThan2 = new CoreOperationLessThan(new Constant(10), new Constant(5));
        assertEquals(Boolean.FALSE, lessThan2.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        assertEquals("<", lessThan.getSymbol());
    }

    @Test
    public void testComputeValueWithNullArguments() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(null), new Constant(null));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, lessThan.computeValue(context));
    }

    @Test
    public void testComputeValueWithNonNumericArguments() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant("5"), new Constant("10"));
        EvalContext context = new EvalContext();
        try {
            lessThan.computeValue(context);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Pass
        }
    }
}