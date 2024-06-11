import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new ConcreteExpression();
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new ConcreteExpression();
        EvalContext evalContext = new EvalContext();
        Object result = expression.computeValue(evalContext);
        assertNotNull(result);
    }

    @Test
    public void testCompute() {
        Expression expression = new ConcreteExpression();
        EvalContext evalContext = new EvalContext();
        Object result = expression.compute(evalContext);
        assertNotNull(result);
    }

    @Test
    public void testIterator() {
        Expression expression = new ConcreteExpression();
        EvalContext evalContext = new EvalContext();
        Iterator iterator = expression.iterate(evalContext);
        assertNotNull(iterator);
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new ConcreteExpression();
        EvalContext evalContext = new EvalContext();
        Iterator pointerIterator = expression.iteratePointers(evalContext);
        assertNotNull(pointerIterator);
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator pointerIterator = new Expression.PointerIterator(null, null, null);
        assertFalse(pointerIterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator valueIterator = new Expression.ValueIterator(null);
        assertFalse(valueIterator.hasNext());
    }

    private class ConcreteExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return new Object();
        }

        @Override
        public Object compute(EvalContext context) {
            return new Object();
        }
    }
}