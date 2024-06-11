package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.JXPathBasicBeanInfo;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.PageBinding;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {
    private EvalContext context;
    
    @Before
    public void setUp() {
        context = new EvalContext();
    }
    
    @After
    public void tearDown() {
        context = null;
    }
    
    @Test
    public void testIsContextDependent() {
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
        
        assertFalse(expression.isContextDependent());
    }
    
    @Test
    public void testIsContextDependentWithData() {
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
    public void testComputeValue() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            
            public Object computeValue(EvalContext context) {
                return "value";
            }
            
            public Object compute(EvalContext context) {
                return null;
            }
        };
        
        assertEquals("value", expression.computeValue(null));
    }
    
    @Test
    public void testCompute() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            
            public Object computeValue(EvalContext context) {
                return null;
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
                return new Object[] {1, 2, 3};
            }
            
            public Object compute(EvalContext context) {
                return null;
            }
        };
        
        assertEquals(1, expression.iterate(null).next());
        assertEquals(2, expression.iterate(null).next());
        assertEquals(3, expression.iterate(null).next());
    }
    
    @Test
    public void testIterateWithNullResult() {
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
        
        assertFalse(expression.iterate(null).hasNext());
    }
    
    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            
            public Object computeValue(EvalContext context) {
                return new Object[] {1, 2, 3};
            }
            
            public Object compute(EvalContext context) {
                return null;
            }
        };
        
        assertEquals(PageBinding.getPagePointer(1), expression.iteratePointers(null).next());
        assertEquals(PageBinding.getPagePointer(2), expression.iteratePointers(null).next());
        assertEquals(PageBinding.getPagePointer(3), expression.iteratePointers(null).next());
    }
    
    @Test
    public void testIteratePointersWithNullResult() {
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
        
        assertFalse(expression.iteratePointers(null).hasNext());
    }
    
    @Test
    public void testPointerIteratorHasNext() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(new Object[] {1}.iterator(), null, null);
        assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testPointerIteratorHasNextWithNullResult() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(Collections.EMPTY_LIST.iterator(), null, null);
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testPointerIteratorNext() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(new Object[] {1}.iterator(), null, null);
        assertEquals(PageBinding.getPagePointer(1), iterator.next());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIteratorRemove() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(new Object[] {1}.iterator(), null, null);
        iterator.remove();
    }
    
    @Test
    public void testValueIteratorHasNext() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(new Object[] {1}.iterator());
        assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testValueIteratorHasNextWithNullResult() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(Collections.EMPTY_LIST.iterator());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testValueIteratorNext() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(new Object[] {1}.iterator());
        assertSame(1, iterator.next());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(new Object[] {1}.iterator());
        iterator.remove();
    }
}