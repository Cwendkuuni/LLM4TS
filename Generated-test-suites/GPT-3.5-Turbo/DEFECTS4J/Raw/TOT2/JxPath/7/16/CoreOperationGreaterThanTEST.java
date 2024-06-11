import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression arg1 = new Literal(5); // value 5
        Expression arg2 = new Literal(3); // value 3
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result); // 5 > 3
    }

    @Test
    public void testGetSymbol() {
        // Typical use case
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null); // Dummy arguments
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }

    @Test
    public void testComputeValueEdgeCases() {
        // Edge case: arg1 and arg2 are equal
        Expression arg1 = new Literal(3);
        Expression arg2 = new Literal(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result); // 3 !> 3

        // Edge case: negative values
        arg1 = new Literal(-5);
        arg2 = new Literal(-3);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result); // -5 !> -3
    }

    @Test
    public void testComputeValueExceptionHandling() {
        // Exception handling: non-numeric values
        Expression arg1 = new Literal("test"); // Non-numeric value
        Expression arg2 = new Literal(5); // Numeric value
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        try {
            Object result = operation.computeValue(context);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            // Expected exception caught
        }
    }
}