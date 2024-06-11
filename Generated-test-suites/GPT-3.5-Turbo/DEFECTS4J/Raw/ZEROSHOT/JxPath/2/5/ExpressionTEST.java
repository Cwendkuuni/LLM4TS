import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };

        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeContextDependent() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }

            @Override
            public Object computeValue(EvalContext context) {
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
            public Object compute(EvalContext context) {
                return "test";
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };

        assertEquals(expression.computeValue(null), "test");
    }

    @Test
    public void testCompute() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object compute(EvalContext context) {
                return "test";
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };

        assertEquals(expression.compute(null), "test");
    }

    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object compute(EvalContext context) {
                return new String[]{"test"};
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };

        assertEquals(expression.iterate(null).next(), "test");
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object compute(EvalContext context) {
                return new String[]{"test"};
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };

        assertEquals(expression.iteratePointers(null).next(), "test");
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