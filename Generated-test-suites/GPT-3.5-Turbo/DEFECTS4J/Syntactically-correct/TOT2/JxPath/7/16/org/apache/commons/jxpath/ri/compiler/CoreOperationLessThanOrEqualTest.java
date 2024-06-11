import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_basicCase() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(2), new Constant(3));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_equalNumbers() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(2), new Constant(2));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_greaterLeft() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(3), new Constant(2));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_exceptionHandling() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant("not a number"), new Constant(2));
        EvalContext context = new EvalContext();
        try {
            operation.computeValue(context);
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Pass
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(2), new Constant(3));
        assertEquals("<=", operation.getSymbol());
    }
}