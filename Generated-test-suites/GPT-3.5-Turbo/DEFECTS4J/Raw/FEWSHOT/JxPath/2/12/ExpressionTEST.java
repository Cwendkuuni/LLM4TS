package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

public class ExpressionTest {
    
    // Helper methods for mocking EvalContext and ValueUtils
    private List<Object> createMockNodeSet(Object... values) {
        List<Object> nodeSet = new ArrayList<>();
        Collections.addAll(nodeSet, values);
        return nodeSet;
    }
    
    private Object createMockNode() {
        return new Object();
    }
    
    private EvalContext createMockEvalContext(Object node, List<Object> nodeSet) {
        EvalContext context = new EvalContext(null, node);
        context.setNodeSet(nodeSet);
        return context;
    }

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
        
        expression = new Expression() {
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
                return "result";
            }
            
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        
        EvalContext context = new EvalContext(null, createMockNode());
        assertEquals("result", expression.computeValue(context));

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
        
        EvalContext context = new EvalContext(null, createMockNode());
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
                return createMockNodeSet("value1", "value2", "value3");
            }
            
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        
        EvalContext context = createMockEvalContext(createMockNode(), createMockNodeSet());
        Iterator<Object> iterator = expression.iterate(context);
        
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value3", iterator.next());
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
                return createMockNodeSet("value1", "value2", "value3");
            }
            
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        
        EvalContext context = createMockEvalContext(createMockNode(), createMockNodeSet());
        Iterator<Object> iterator = expression.iteratePointers(context);
        
        assertTrue(iterator.hasNext());
        assertEquals("value1", ((Pointer)iterator.next()).getValue());
        assertTrue(iterator.hasNext());
        assertEquals("value2", ((Pointer)iterator.next()).getValue());
        assertTrue(iterator.hasNext());
        assertEquals("value3", ((Pointer)iterator.next()).getValue());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testPointerIterator() {
        List<Object> values = createMockNodeSet("value1", "value2", "value3");
        Expression.PointerIterator iterator = new Expression.PointerIterator(values.iterator(),
                new QName(null, "value"), null);
        
        assertTrue(iterator.hasNext());
        assertEquals("value1", ((Pointer)iterator.next()).getValue());
        assertTrue(iterator.hasNext());
        assertEquals("value2", ((Pointer)iterator.next()).getValue());
        assertTrue(iterator.hasNext());
        assertEquals("value3", ((Pointer)iterator.next()).getValue());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testValueIterator() {
        List<Object> values = createMockNodeSet("value1", "value2", "value3");
        Expression.ValueIterator iterator = new Expression.ValueIterator(values.iterator());
        
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }
}