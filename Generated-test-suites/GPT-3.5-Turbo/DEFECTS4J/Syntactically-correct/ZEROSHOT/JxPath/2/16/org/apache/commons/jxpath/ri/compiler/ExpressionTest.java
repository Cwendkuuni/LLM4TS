import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
    
    private Expression expression;
    
    @Before
    public void setUp() {
        expression = new Expression() {
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
    }
    
    @After
    public void tearDown() {
        expression = null;
    }
    
    @Test
    public void testIsContextDependent() {
        boolean isContextDependent = expression.isContextDependent();
        assertTrue(isContextDependent);
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object computeValue = expression.computeValue(context);
        assertNull(computeValue);
    }
    
    @Test
    public void testCompute() {
        EvalContext context = new EvalContext();
        Object compute = expression.compute(context);
        assertNull(compute);
    }
    
    @Test
    public void testIterate() {
        EvalContext context = new EvalContext();
        Iterator iterate = expression.iterate(context);
        assertFalse(iterate.hasNext());
    }
    
    @Test
    public void testIteratePointers() {
        EvalContext context = new EvalContext();
        Iterator iteratePointers = expression.iteratePointers(context);
        assertFalse(iteratePointers.hasNext());
    }
    
    @Test
    public void testPointerIteratorHasNext() {
        Iterator iterator = new Expression.PointerIterator(null, null, null);
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testPointerIteratorNext() {
        Iterator iterator = new Expression.PointerIterator(null, null, null);
        assertNull(iterator.next());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIteratorRemove() {
        Iterator iterator = new Expression.PointerIterator(null, null, null);
        iterator.remove();
    }
    
    @Test
    public void testValueIteratorHasNext() {
        Iterator iterator = new Expression.ValueIterator(null);
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testValueIteratorNext() {
        Iterator iterator = new Expression.ValueIterator(null);
        assertNull(iterator.next());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        Iterator iterator = new Expression.ValueIterator(null);
        iterator.remove();
    }
}