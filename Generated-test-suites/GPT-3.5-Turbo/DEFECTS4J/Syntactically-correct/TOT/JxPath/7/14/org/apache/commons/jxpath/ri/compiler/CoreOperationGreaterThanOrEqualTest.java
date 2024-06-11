import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operator;

    @Test
    public void testComputeValue() {
        // typical use case
        operator = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(3));
        assertEquals(true, operator.computeValue(null));

        // edge cases
        operator = new CoreOperationGreaterThanOrEqual(new ConstantExpression(Double.NaN), new ConstantExpression(3));
        assertEquals(false, operator.computeValue(null));

        operator = new CoreOperationGreaterThanOrEqual(new ConstantExpression(3), new ConstantExpression(Double.NEGATIVE_INFINITY));
        assertEquals(true, operator.computeValue(null));

        operator = new CoreOperationGreaterThanOrEqual(new ConstantExpression(Double.POSITIVE_INFINITY), new ConstantExpression(Double.POSITIVE_INFINITY));
        assertEquals(true, operator.computeValue(null));

        // error scenarios
        operator = new CoreOperationGreaterThanOrEqual(new ConstantExpression("5"), new ConstantExpression(3));
        assertThrows(ClassCastException.class, () -> {
            operator.computeValue(null);
        });

        operator = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(null));
        assertThrows(NullPointerException.class, () -> {
            operator.computeValue(null);
        });
    }

    @Test
    public void testGetSymbol() {
        operator = new CoreOperationGreaterThanOrEqual(null, null);
        assertEquals(">=", operator.getSymbol());
    }
}