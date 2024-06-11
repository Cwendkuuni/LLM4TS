import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setUp() {
        operation = new CoreOperationGreaterThanOrEqual(new ExpressionMock(), new ExpressionMock());
    }

    @Test
    public void testComputeValue_LessThan_ReturnsFalse() {
        // Arrange
        double l = 4.5;
        double r = 7.2;
        ExpressionMock arg1 = new ExpressionMock(l);
        ExpressionMock arg2 = new ExpressionMock(r);
        operation.setArgs(new Expression[] { arg1, arg2 });

        // Act
        Object result = operation.computeValue(new EvalContextMock());

        // Assert
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_GreaterThanOrEqual_ReturnsTrue() {
        // Arrange
        double l = 7.2;
        double r = 4.5;
        ExpressionMock arg1 = new ExpressionMock(l);
        ExpressionMock arg2 = new ExpressionMock(r);
        operation.setArgs(new Expression[] { arg1, arg2 });

        // Act
        Object result = operation.computeValue(new EvalContextMock());

        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_Equal_ReturnsTrue() {
        // Arrange
        double l = 5.6;
        double r = 5.6;
        ExpressionMock arg1 = new ExpressionMock(l);
        ExpressionMock arg2 = new ExpressionMock(r);
        operation.setArgs(new Expression[] { arg1, arg2 });

        // Act
        Object result = operation.computeValue(new EvalContextMock());

        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanOrEqualSymbol() {
        // Arrange
        String expected = ">=";

        // Act
        String result = operation.getSymbol();

        // Assert
        Assert.assertEquals(expected, result);
    }
}

class ExpressionMock extends Expression {
    private double value;

    public ExpressionMock() {
        super(null);
    }

    public ExpressionMock(double value) {
        super(null);
        this.value = value;
    }

    public void setArgs(Expression[] args) {
        this.args = args;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return value;
    }
}

class EvalContextMock extends EvalContext {
    @Override
    public Object getValue(String xpath) {
        return null;
    }
}