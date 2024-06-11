
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual operation;

    @Test
    public void testComputeValueWithLessThanOrEqualValues() {
        // Arrange
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(10), new ExpressionMock(20));
        EvalContext context = new EvalContextMock();

        // Act
        Object result = operation.computeValue(context);

        // Assert
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithGreaterThanValues() {
        // Arrange
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(30), new ExpressionMock(20));
        EvalContext context = new EvalContextMock();

        // Act
        Object result = operation.computeValue(context);

        // Assert
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithEqualValues() {
        // Arrange
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(20), new ExpressionMock(20));
        EvalContext context = new EvalContextMock();

        // Act
        Object result = operation.computeValue(context);

        // Assert
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        // Arrange
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(10), new ExpressionMock(20));

        // Act
        String symbol = operation.getSymbol();

        // Assert
        assertEquals("<=", symbol);
    }

    private class ExpressionMock extends Expression {

        private Object value;

        public ExpressionMock(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }

    }

    private class EvalContextMock extends EvalContext {

        public double doubleValue(Object object) {
            if (object instanceof Integer) {
                return ((Integer) object).doubleValue();
            }
            return super.doubleValue(object);
        }

    }
}