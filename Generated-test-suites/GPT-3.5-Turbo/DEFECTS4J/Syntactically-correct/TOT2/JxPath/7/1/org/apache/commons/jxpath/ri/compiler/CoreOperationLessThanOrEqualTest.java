import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValueBasic() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
            new Constant(3), new Constant(5));
        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValueEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
            new Constant(3), new Constant(3));
        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullArgument() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
            null, new Constant(5));
        EvalContext context = new EvalContext(null);
        operation.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
            new Constant(3), new Constant(5));
        assertEquals("<=", operation.getSymbol());
    }
}