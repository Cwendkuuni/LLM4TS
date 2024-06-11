import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new TestCoreOperationRelationalExpression();
    }

    @Test
    public void testComputeValue_typicalCase() {
        // Arrange
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(5);
        Expression[] args = {
            new TestExpression(10),
            new TestExpression(7)
        };
        expression.setArgs(args);

        // Act
        Object result = expression.computeValue(context);

        // Assert
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_edgeCase() {
        // Arrange
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(5);
        Expression[] args = {
            new TestExpression(5),
            new TestExpression(5)
        };
        expression.setArgs(args);

        // Act
        Object result = expression.computeValue(context);

        // Assert
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_errorCase() {
        // Arrange
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(5);
        Expression[] args = {
            new TestExpression(Double.NaN),
            new TestExpression(10)
        };
        expression.setArgs(args);

        // Act
        Object result = expression.computeValue(context);

        // Assert
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public TestCoreOperationRelationalExpression() {
            super(null);
        }
        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }

    private class TestExpression extends Expression {
        private Object value;
        public TestExpression(Object value) {
            super(null);
            this.value = value;
        }
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}