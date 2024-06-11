import static org.junit.Assert.*;
import org.junit.Test;

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
    public void testComputeContextDependent() {
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
        
        assertTrue(expression.computeContextDependent());
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
                return "Value";
            }
            
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        
        assertEquals("Value", expression.computeValue(null));
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
                return "Computed";
            }
        };
        
        assertEquals("Computed", expression.compute(null));
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
        
        assertTrue(expression.iterate(null).hasNext());
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
                return new Object[] {new Pointer(null), new Pointer(null)};
            }
            
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        
        assertTrue(expression.iteratePointers(null).hasNext());
    }
    
    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);
        
        assertFalse(iterator.hasNext());
    }
}