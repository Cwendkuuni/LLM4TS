import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual lessThanOrEqual;

    @Test
    public void computeValue_LessThanOrEqualTo_ReturnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(10));

        // Act
        Object result = lessThanOrEqual.computeValue(context);

        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void computeValue_NotLessThanOrEqualTo_ReturnsFalse() {
        // Arrange
        EvalContext context = new EvalContext();
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(new Constant(10), new Constant(5));

        // Act
        Object result = lessThanOrEqual.computeValue(context);

        // Assert
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void computeValue_EqualValues_ReturnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(5));

        // Act
        Object result = lessThanOrEqual.computeValue(context);

        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void computeValue_StringValues_ThrowsException() {
        // Arrange
        EvalContext context = new EvalContext();
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(new Constant("5"), new Constant("10"));

        // Act & Assert
        try {
            lessThanOrEqual.computeValue(context);
            Assert.fail("Expected ClassCastException");
        } catch (ClassCastException ex) {
            // Expected
        }
    }

    @Test
    public void getSymbol_ReturnsLessThanOrEqualSymbol() {
        // Arrange
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(null, null);

        // Act
        String symbol = lessThanOrEqual.getSymbol();

        // Assert
        Assert.assertEquals("<=", symbol);
    }
}