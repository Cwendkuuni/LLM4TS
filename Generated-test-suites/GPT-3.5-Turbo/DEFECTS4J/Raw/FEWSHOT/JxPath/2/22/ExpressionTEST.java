package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.model.NodePointer;
import org.junit.Test;

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
    public void testIsContextDependent_NotDependent() {
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
    public void testComputeValue() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }

            public Object computeValue(EvalContext context) {
                return "test";
            }

            public Object compute(EvalContext context) {
                return null;
            }
        };
        
        EvalContext evalContext = new EvalContext(null, null);
        assertEquals("test", expression.computeValue(evalContext));
    }
    
    @Test
    public void testComputeValue_Null() {
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
        
        EvalContext evalContext = new EvalContext(null, null);
        assertNull(expression.computeValue(evalContext));
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
                return "test";
            }
        };
        
        EvalContext evalContext = new EvalContext(null, null);
        assertEquals("test", expression.compute(evalContext));
    }
    
    @Test
    public void testCompute_Null() {
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
        
        EvalContext evalContext = new EvalContext(null, null);
        assertNull(expression.compute(evalContext));
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
                return new Integer(5);
            }
        };
        
        EvalContext evalContext = new EvalContext(null, null);
        Iterator iter = expression.iterate(evalContext);
        assertTrue(iter.hasNext());
        assertEquals(new Integer(5), iter.next());
        assertFalse(iter.hasNext());
    }
    
    @Test
    public void testIterate_Null() {
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
        
        EvalContext evalContext = new EvalContext(null, null);
        Iterator iter = expression.iterate(evalContext);
        assertFalse(iter.hasNext());
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
                return "test";
            }
        };
        
        EvalContext evalContext = new EvalContext(null, null);
        Iterator iter = expression.iteratePointers(evalContext);
        assertTrue(iter.hasNext());
        assertEquals("test", iter.next());
        assertFalse(iter.hasNext());
    }
    
    @Test
    public void testIteratePointers_Null() {
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
        
        EvalContext evalContext = new EvalContext(null, null);
        Iterator iter = expression.iteratePointers(evalContext);
        assertFalse(iter.hasNext());
    }
}