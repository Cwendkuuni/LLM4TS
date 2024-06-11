import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {
    @Test
    public void testIsContextDependent() {
        // Test when contextDependencyKnown is false
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
        assertFalse(expression.isContextDependent());
        
        // Test when contextDependencyKnown is true
        expression = new Expression() {
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
        assertFalse(expression.isContextDependent());
    }
    
    @Test
    public void testComputeValue() {
        // Test when result is null
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
        assertNull(expression.computeValue(null));
        
        // Test when result is not null
        expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
            public Object computeValue(EvalContext context) {
                return "result";
            }
            public Object compute(EvalContext context) {
                return "result";
            }
        };
        assertEquals("result", expression.computeValue(null));
    }
    
    @Test
    public void testCompute() {
        // Test when result is null
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
        assertNull(expression.compute(null));
        
        // Test when result is not null
        expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
            public Object computeValue(EvalContext context) {
                return "result";
            }
            public Object compute(EvalContext context) {
                return "result";
            }
        };
        assertEquals("result", expression.compute(null));
    }
    
    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return new Object[]{1, 2, 3};
            }
            public Object compute(EvalContext context) {
                return new Object[]{1, 2, 3};
            }
        };
        assertTrue(expression.iterate(null).hasNext());
        assertEquals(1, expression.iterate(null).next());
    }
    
    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return new Object[]{1, 2, 3};
            }
            public Object compute(EvalContext context) {
                return new Object[]{1, 2, 3};
            }
        };
        assertTrue(expression.iteratePointers(null).hasNext());
        assertEquals(1, expression.iteratePointers(null).next());
    }
    
    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }
    
    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }
}