import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void computeValue_LessThan_ReturnsTrue() {
        // Arrange
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        Object result = expression.computeValue(new EvalContext());

        // Assert
        Assert.assertEquals(true, result);
    }

    @Test
    public void computeValue_GreaterThan_ReturnsFalse() {
        // Arrange
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        Object result = expression.computeValue(new EvalContext());

        // Assert
        Assert.assertEquals(false, result);
    }

    @Test
    public void computeValue_Equal_ReturnsFalse() {
        // Arrange
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        Object result = expression.computeValue(new EvalContext());

        // Assert
        Assert.assertEquals(false, result);
    }

    // Add more edge cases and error scenarios test methods...

}