import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
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
        };

        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return "test";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        assertEquals("test", expression.computeValue(context));
    }

    @Test
    public void testCompute() {
        Expression expression = new Expression() {
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
                return "test";
            }
        };

        EvalContext context = new EvalContext();
        assertEquals("test", expression.compute(context));
    }

    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return new Object[] {1, 2, 3};
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        assertNotNull(expression.iterate(context));
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return new Object[] {1, 2, 3};
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        assertNotNull(expression.iteratePointers(context));
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIteratorRemove() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);
        iterator.remove();
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);
        iterator.remove();
    }
}