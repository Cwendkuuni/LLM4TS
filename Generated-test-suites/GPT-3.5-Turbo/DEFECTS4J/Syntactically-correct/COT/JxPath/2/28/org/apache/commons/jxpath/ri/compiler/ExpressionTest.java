import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {
    
    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                // TODO: Implement this method
                return false;
            }
            public Object computeValue(EvalContext context) {
                // TODO: Implement this method
                return null;
            }
            public Object compute(EvalContext context) {
                // TODO: Implement this method
                return null;
            }
        };
        boolean result = expression.isContextDependent();
        assertEquals(false, result);
    }
    
    @Test
    public void testComputeValue() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                // TODO: Implement this method
                return false;
            }
            public Object computeValue(EvalContext context) {
                // TODO: Implement this method
                return null;
            }
            public Object compute(EvalContext context) {
                // TODO: Implement this method
                return null;
            }
        };
        Object result = expression.computeValue(null);
        assertEquals(null, result);
    }
    
    @Test
    public void testCompute() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                // TODO: Implement this method
                return false;
            }
            public Object computeValue(EvalContext context) {
                // TODO: Implement this method
                return null;
            }
            public Object compute(EvalContext context) {
                // TODO: Implement this method
                return null;
            }
        };
        Object result = expression.compute(null);
        assertEquals(null, result);
    }
    
    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                // TODO: Implement this method
                return false;
            }
            public Object computeValue(EvalContext context) {
                // TODO: Implement this method
                return null;
            }
            public Object compute(EvalContext context) {
                // TODO: Implement this method
                return null;
            }
        };
        Iterator result = expression.iterate(null);
        assertEquals(null, result);
    }
    
    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                // TODO: Implement this method
                return false;
            }
            public Object computeValue(EvalContext context) {
                // TODO: Implement this method
                return null;
            }
            public Object compute(EvalContext context) {
                // TODO: Implement this method
                return null;
            }
        };
        Iterator result = expression.iteratePointers(null);
        assertEquals(Collections.EMPTY_LIST.iterator(), result);
    }
    
    @Test
    public void testPointerIterator() {
        Expression.PointerIterator pointerIterator = new Expression.PointerIterator(null, null, null);
        boolean result = pointerIterator.hasNext();
        assertEquals(false, result);
    }
    
    @Test
    public void testValueIterator() {
        Expression.ValueIterator valueIterator = new Expression.ValueIterator(null);
        boolean result = valueIterator.hasNext();
        assertEquals(false, result);
    }
    
}