import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationLessThanTest {

    private CoreOperationLessThan coreOperationLessThan;

    @Before
    public void setup() {
        // Create instance of CoreOperationLessThan for testing
        coreOperationLessThan = new CoreOperationLessThan(new ExpressionMock(), new ExpressionMock());
    }

    @Test
    public void testComputeValueReturnsTrueWhenFirstArgumentIsLessThanSecondArgument() {
        // Arrange
        double leftArg = 10.5;
        double rightArg = 20.8;
        EvalContext evalContext = new EvalContext();

        // Act
        Object result = coreOperationLessThan.computeValue(evalContext);

        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueReturnsFalseWhenFirstArgumentIsEqualOrGreaterThanSecondArgument() {
        // Arrange
        double leftArg = 10.5;
        double rightArg = 10.5;
        EvalContext evalContext = new EvalContext();

        // Act
        Object result = coreOperationLessThan.computeValue(evalContext);

        // Assert
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueReturnsFalseWhenFirstArgumentIsGreaterThanSecondArgument() {
        // Arrange
        double leftArg = 20.8;
        double rightArg = 10.5;
        EvalContext evalContext = new EvalContext();

        // Act
        Object result = coreOperationLessThan.computeValue(evalContext);

        // Assert
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbolReturnsLessThanSymbol() {
        // Arrange

        // Act
        String symbol = coreOperationLessThan.getSymbol();

        // Assert
        Assert.assertEquals("<", symbol);
    }

    // Add more test cases to cover edge cases and error scenarios

}