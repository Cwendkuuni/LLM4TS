import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
    
    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return null;
            }
        };

        assertTrue(expression.isContextDependent());
    }
    
    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return null;
            }
        };

        Iterator iterator = expression.iterate(null);
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return null;
            }
        };

        Iterator iterator = expression.iteratePointers(null);
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testPointerIterator() {
        Iterator iterator = new Expression.PointerIterator(null, null, null);

        assertFalse(iterator.hasNext());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIteratorRemove() {
        Iterator iterator = new Expression.PointerIterator(null, null, null);

        iterator.remove();
    }
    
    @Test
    public void testValueIterator() {
        Iterator iterator = new Expression.ValueIterator(null);

        assertFalse(iterator.hasNext());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        Iterator iterator = new Expression.ValueIterator(null);

        iterator.remove();
    }

}