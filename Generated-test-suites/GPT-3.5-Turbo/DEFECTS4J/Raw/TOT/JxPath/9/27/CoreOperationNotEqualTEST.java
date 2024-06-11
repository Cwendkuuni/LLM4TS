import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Typical Use Case: Two arguments are not equal
        CoreOperationCompare operation = new CoreOperationCompare(new ExpressionImpl(2), new ExpressionImpl(3));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Typical Use Case: Two arguments are equal
        operation = new CoreOperationCompare(new ExpressionImpl(5), new ExpressionImpl(5));
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        // Edge Case: Both arguments are null
        operation = new CoreOperationCompare(new ExpressionImpl(null), new ExpressionImpl(null));
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        // Edge Case: One argument is null and the other is not
        operation = new CoreOperationCompare(new ExpressionImpl(null), new ExpressionImpl(5));
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        // Typical Use Case: Get the symbol of the operation
        CoreOperationCompare operation = new CoreOperationCompare(new ExpressionImpl(2), new ExpressionImpl(3));
        assertEquals("!=", operation.getSymbol());
    }

}