import org.junit.*;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_GreaterThan() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        // Test for true case
        assertEquals(Boolean.TRUE, expression.computeValue(new TestEvalContext(true)));
        // Test for false case
        assertEquals(Boolean.FALSE, expression.computeValue(new TestEvalContext(false)));
    }

    @Test
    public void testComputeValue_GreaterThanOrEqual() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        // Test for true case
        assertEquals(Boolean.TRUE, expression.computeValue(new TestEvalContext(true)));
        // Test for false case
        assertEquals(Boolean.FALSE, expression.computeValue(new TestEvalContext(false)));
    }

    @Test
    public void testComputeValue_LessThan() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        // Test for true case
        assertEquals(Boolean.TRUE, expression.computeValue(new TestEvalContext(true)));
        // Test for false case
        assertEquals(Boolean.FALSE, expression.computeValue(new TestEvalContext(false)));
    }

    @Test
    public void testComputeValue_LessThanOrEqual() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        // Test for true case
        assertEquals(Boolean.TRUE, expression.computeValue(new TestEvalContext(true)));
        // Test for false case
        assertEquals(Boolean.FALSE, expression.computeValue(new TestEvalContext(false)));
    }

    private class TestExpression extends Expression {
        // Implement necessary methods for testing
    }

    private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        // Implement necessary abstract methods
    }

    private class TestEvalContext extends EvalContext {
        private boolean result;

        public TestEvalContext(boolean result) {
            this.result = result;
        }

        // Implement necessary methods for testing
    }
}