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
            public Object computeValue(EvalContext context) {
                return null;
            }
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        assertTrue(expression.isContextDependent());
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
        assertEquals("test", expression.computeValue(null));
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
        assertEquals("test", expression.compute(null));
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
                return null;
            }
            @Override
            public Object compute(EvalContext context) {
                return new Object[] {"test1", "test2"};
            }
        };
        Iterator iterator = expression.iterate(null);
        assertTrue(iterator.hasNext());
        assertEquals("test1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test2", iterator.next());
        assertFalse(iterator.hasNext());
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
                return null;
            }
            @Override
            public Object compute(EvalContext context) {
                return new Object[] {"test1", "test2"};
            }
        };
        Iterator iterator = expression.iteratePointers(null);
        assertTrue(iterator.hasNext());
        assertEquals("test1", ((Pointer) iterator.next()).getValue());
        assertTrue(iterator.hasNext());
        assertEquals("test2", ((Pointer) iterator.next()).getValue());
        assertFalse(iterator.hasNext());
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