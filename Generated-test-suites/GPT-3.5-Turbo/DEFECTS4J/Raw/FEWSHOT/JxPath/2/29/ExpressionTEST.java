
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression1 = new Expression() {
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
        
        assertFalse(expression1.isContextDependent());
        
        Expression expression2 = new Expression() {
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
        
        assertTrue(expression2.isContextDependent());
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
        
        EvalContext evalContext = new EvalContext(JXPathContext.newContext(null));
        
        assertEquals("value", expression.computeValue(evalContext));
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
        
        EvalContext evalContext = new EvalContext(JXPathContext.newContext(null));
        
        assertEquals("result", expression.compute(evalContext));
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
        
        EvalContext evalContext = new EvalContext(JXPathContext.newContext(null));
        
        assertTrue(expression.iterate(evalContext).hasNext());
        assertEquals(1, expression.iterate(evalContext).next());
        assertEquals(2, expression.iterate(evalContext).next());
        assertEquals(3, expression.iterate(evalContext).next());
        assertFalse(expression.iterate(evalContext).hasNext());
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
        
        EvalContext evalContext = new EvalContext(JXPathContext.newContext(null));
        
        assertFalse(expression.iteratePointers(evalContext).hasNext());
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);

        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
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
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);

        iterator.remove();
    }
}
