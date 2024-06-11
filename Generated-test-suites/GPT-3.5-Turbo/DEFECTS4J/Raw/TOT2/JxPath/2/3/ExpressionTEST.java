import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new SampleExpression();
        // Test typical case
        assertFalse(expression.isContextDependent());
        // Test edge case - context-dependent expression
        assertTrue(new ContextDependentExpression().isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new SampleExpression();
        EvalContext context = new EvalContext();
        // Test typical case
        Object result = expression.computeValue(context);
        assertNotNull(result);
        // Test error scenario - null context
        assertNull(new NullContextExpression().computeValue(null));
    }

    @Test
    public void testCompute() {
        Expression expression = new SampleExpression();
        EvalContext context = new EvalContext();
        // Test typical case
        Object result = expression.compute(context);
        assertNotNull(result);
        // Test error scenario - invalid context
        assertNull(new InvalidContextExpression().compute(null));
    }

    @Test
    public void testIterate() {
        Expression expression = new SampleExpression();
        EvalContext context = new EvalContext();
        // Test typical case
        assertNotNull(expression.iterate(context));
        // Test edge case - empty result
        assertFalse(expression.iterate(new EmptyContext()).hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new SampleExpression();
        EvalContext context = new EvalContext();
        // Test typical case
        assertNotNull(expression.iteratePointers(context));
        // Test edge case - invalid result
        assertFalse(expression.iteratePointers(new InvalidContext()).hasNext());
    }

    private static class SampleExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return 10;
        }

        @Override
        public Object compute(EvalContext context) {
            return "sample";
        }
    }

    private static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }

        @Override
        public Object compute(EvalContext context) {
            return null;
        }
    }

    private static class NullContextExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }

        @Override
        public Object compute(EvalContext context) {
            return null;
        }
    }

    private static class InvalidContextExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }

        @Override
        public Object compute(EvalContext context) {
            return null;
        }
    }

    private static class EmptyContext extends EvalContext {
        @Override
        public Iterator iterateNodeSetPointer() {
            return Collections.emptyList().iterator();
        }
    }

    private static class InvalidContext extends EvalContext {
        @Override
        public Iterator iterateNodeSetPointer() {
            return null;
        }
    }
}