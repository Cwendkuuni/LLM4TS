import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testConstructor() {
        // Create instances of Expression for testing
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();

        // Create an instance of CoreOperationLessThan
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        // Assert that the arguments are set correctly
        assertEquals(arg1, operation.args[0]);
        assertEquals(arg2, operation.args[1]);
    }

    @Test
    public void testComputeValue() {
        // Create test context
        EvalContext context = new DummyEvalContext();

        // Create instances of Expression for testing
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();

        // Create an instance of CoreOperationLessThan
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        // Test with a left value less than right value
        arg1.setValue(5);
        arg2.setValue(10);
        assertTrue((Boolean) operation.computeValue(context));

        // Test with a left value greater than right value
        arg1.setValue(15);
        arg2.setValue(10);
        assertFalse((Boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        // Create instances of Expression for testing
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();

        // Create an instance of CoreOperationLessThan
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        assertEquals("<", operation.getSymbol());
    }

    private static class DummyExpression implements Expression {
        private Object value;

        public void setValue(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }
    }

    private static class DummyEvalContext implements EvalContext {
        @Override
        public Object getContextBean() {
            return null;
        }
    }
}