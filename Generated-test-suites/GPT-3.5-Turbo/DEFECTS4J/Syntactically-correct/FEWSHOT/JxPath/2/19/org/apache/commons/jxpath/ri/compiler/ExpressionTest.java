import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {

    private EvalContext context;

    @Before
    public void setUp() {
        context = new EvalContext(null, null, null, null);
    }
    
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
              return "value";
            }
            
            @Override
            public Object compute(EvalContext context) {
              return null;
            }
        };
        
        assertEquals("value", expression.computeValue(context));
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
              return "result";
            }
        };
        
        assertEquals("result", expression.compute(context));
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
              return ValueUtils.createNodeSet(new Object[] {1, 2, 3});
            }
            
            @Override
            public Object compute(EvalContext context) {
              return null;
            }
        };
        
        assertTrue(expression.iterate(context).hasNext());
        assertEquals(1, expression.iterate(context).next());
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
              return ValueUtils.createNodeSet(new Object[] {"value1", "value2"});
            }
            
            @Override
            public Object compute(EvalContext context) {
              return null;
            }
        };
        
        assertTrue(expression.iteratePointers(context).hasNext());
        assertNotNull(expression.iteratePointers(context).next());
    }
    
    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(ValueUtils.createNodeSet(new Object[] {"value1", "value2"}).iterator(), null, Locale.US);
        
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(ValueUtils.createNodeSet(new Object[] {"value1", "value2"}).iterator());
        
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }
}