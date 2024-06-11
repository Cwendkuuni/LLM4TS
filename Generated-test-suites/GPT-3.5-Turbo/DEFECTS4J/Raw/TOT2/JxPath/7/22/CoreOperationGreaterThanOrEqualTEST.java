import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_WhenLeftIsGreater_ReturnsTrue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Constant(5),
                new Constant(3)
        );
        EvalContext context = new EvalContext();

        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_WhenRightIsGreater_ReturnsFalse() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Constant(3),
                new Constant(5)
        );
        EvalContext context = new EvalContext();

        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_WhenEqual_ReturnsTrue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Constant(5),
                new Constant(5)
        );
        EvalContext context = new EvalContext();

        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanOrEqualSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Constant(3),
                new Constant(5)
        );

        assertEquals(">=", operation.getSymbol());
    }

    // Edge cases and exception handling scenarios

    @Test(expected = NullPointerException.class)
    public void testConstructor_WithNullArguments_ThrowsNullPointerException() {
        new CoreOperationGreaterThanOrEqual(null, null);
    }

    @Test
    public void testConstructor_WithNonNullArguments_ConstructsObject() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Constant(3),
                new Constant(5)
        );

        assertNotNull(operation);
    }

}