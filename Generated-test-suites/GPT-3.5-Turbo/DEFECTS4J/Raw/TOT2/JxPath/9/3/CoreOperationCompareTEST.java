import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqualMethod() {
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        CoreOperationCompare coreOperation = new CoreOperationCompare(arg1, arg2);

        // Test for typical case
        EvalContext context = new DummyEvalContext();
        boolean result = coreOperation.equal(context, arg1, arg2);
        assertTrue(result);

        // Test for edge case with different types
        Object l = "abc";
        Object r = 123;
        boolean edgeResult = coreOperation.equal(context, l, r);
        assertFalse(edgeResult);

        // Add more edge cases and error scenarios here
    }

    // Add more test methods for other public methods in the CoreOperationCompare class

    private static class DummyExpression extends Expression {
        @Override
        public Object compute(EvalContext context) {
            return null;
        }
    }

    private static class DummyEvalContext extends EvalContext {
        @Override
        public Object getSingleNodePointer() {
            return null;
        }
    }
}