import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan operation;

    @Before
    public void setUp() {
        operation = new CoreOperationGreaterThan(null, null);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }

    @Test
    public void testComputeValue_withGreaterValue_shouldReturnTrue() {
        // Arrange
        EvalContext context = null;
        double arg1 = 5.0;
        double arg2 = 3.0;

        // Act
        operation.args[0] = new ExpressionMock(arg1);
        operation.args[1] = new ExpressionMock(arg2);
        Object result = operation.computeValue(context);

        // Assert
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_withEqualValues_shouldReturnFalse() {
        // Arrange
        EvalContext context = null;
        double arg1 = 5.0;
        double arg2 = 5.0;

        // Act
        operation.args[0] = new ExpressionMock(arg1);
        operation.args[1] = new ExpressionMock(arg2);
        Object result = operation.computeValue(context);

        // Assert
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_withSmallerValue_shouldReturnFalse() {
        // Arrange
        EvalContext context = null;
        double arg1 = 3.0;
        double arg2 = 5.0;

        // Act
        operation.args[0] = new ExpressionMock(arg1);
        operation.args[1] = new ExpressionMock(arg2);
        Object result = operation.computeValue(context);

        // Assert
        assertFalse((Boolean) result);
    }

    private class ExpressionMock extends Expression {
        private double value;

        public ExpressionMock(double value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}