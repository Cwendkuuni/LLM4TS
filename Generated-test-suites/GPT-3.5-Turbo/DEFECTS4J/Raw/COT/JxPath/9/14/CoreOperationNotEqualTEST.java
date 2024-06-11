
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual operation;

    @Test
    public void testComputeValue_returnsTrueWhenExpressionsAreNotEqual() {
        // Arrange
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationNotEqual(arg1, arg2);

        // Act
        Object result = operation.computeValue(new EvalContext());

        // Assert
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_returnsFalseWhenExpressionsAreEqual() {
        // Arrange
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue("value");
        arg2.setValue("value");
        operation = new CoreOperationNotEqual(arg1, arg2);

        // Act
        Object result = operation.computeValue(new EvalContext());

        // Assert
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol_returnsNotEqualSymbol() {
        // Arrange
        operation = new CoreOperationNotEqual(null, null);

        // Act
        String symbol = operation.getSymbol();

        // Assert
        assertEquals("!=", symbol);
    }
}