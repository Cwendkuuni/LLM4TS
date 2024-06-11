
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_ReturnsTrue_WhenLeftValueIsGreaterThanOrEqualToRightValue() {
        // Arrange
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();

        // Act
        boolean result = (boolean) operation.computeValue(context);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testComputeValue_ReturnsFalse_WhenLeftValueIsLessThanRightValue() {
        // Arrange
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();

        // Act
        boolean result = (boolean) operation.computeValue(context);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanOrEqualSymbol() {
        // Arrange
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());

        // Act
        String symbol = operation.getSymbol();

        // Assert
        Assert.assertEquals(">=", symbol);
    }
}
