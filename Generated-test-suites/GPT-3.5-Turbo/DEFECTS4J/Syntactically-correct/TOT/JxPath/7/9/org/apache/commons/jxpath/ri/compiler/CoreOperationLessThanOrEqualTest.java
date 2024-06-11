import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual operation;

    @Before
    public void setup() {
        operation = new CoreOperationLessThanOrEqual(null, null);
    }

    @Test
    public void testComputeValue_IntegerLessThanInteger_ReturnsTrue() {
        // Arrange
        EvalContext context = null;
        operation.setArgs(new Expression[]{new IntegerExpression(5), new IntegerExpression(10)});

        // Act
        Object result = operation.computeValue(context);

        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_IntegerGreaterThanInteger_ReturnsFalse() {
        // Arrange
        EvalContext context = null;
        operation.setArgs(new Expression[]{new IntegerExpression(10), new IntegerExpression(5)});

        // Act
        Object result = operation.computeValue(context);

        // Assert
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_IntegerEqualsToInteger_ReturnsTrue() {
        // Arrange
        EvalContext context = null;
        operation.setArgs(new Expression[]{new IntegerExpression(10), new IntegerExpression(10)});

        // Act
        Object result = operation.computeValue(context);

        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_DoubleLessThanDouble_ReturnsTrue() {
        // Arrange
        EvalContext context = null;
        operation.setArgs(new Expression[]{new DoubleExpression(2.5), new DoubleExpression(5.3)});

        // Act
        Object result = operation.computeValue(context);

        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_DoubleGreaterThanDouble_ReturnsFalse() {
        // Arrange
        EvalContext context = null;
        operation.setArgs(new Expression[]{new DoubleExpression(5.3), new DoubleExpression(2.5)});

        // Act
        Object result = operation.computeValue(context);

        // Assert
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_DoubleEqualsToDouble_ReturnsTrue() {
        // Arrange
        EvalContext context = null;
        operation.setArgs(new Expression[]{new DoubleExpression(5.3), new DoubleExpression(5.3)});

        // Act
        Object result = operation.computeValue(context);

        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testGetSymbol() {
        // Arrange

        // Act
        String symbol = operation.getSymbol();

        // Assert
        Assert.assertEquals("<=", symbol);
    }
}