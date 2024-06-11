
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;


public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    private Expression arg1;
    private Expression arg2;
    private EvalContext evalContext;
    
    @Before
    public void setUp() {
        arg1 = mock(Expression.class);
        arg2 = mock(Expression.class);
        coreOperationCompare = new CoreOperationCompare(arg1, arg2) {
            @Override
            protected Object computeValue(EvalContext context) {
                return null;
            }
        };
        evalContext = mock(EvalContext.class);
    }
    
    @Test
    public void testEqual() {
        when(arg1.compute(eq(evalContext))).thenReturn("value");
        when(arg2.compute(eq(evalContext))).thenReturn("value");
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_WithEqualPointers() {
        Pointer pointer1 = mock(Pointer.class);
        Pointer pointer2 = mock(Pointer.class);
        
        when(arg1.compute(eq(evalContext))).thenReturn(pointer1);
        when(arg2.compute(eq(evalContext))).thenReturn(pointer2);
        when(pointer1.getValue()).thenReturn("value");
        when(pointer2.getValue()).thenReturn("value");
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertTrue(result);
    }
    
    @Test
    public void testNotEqual_WithNotEqualPointers() {
        Pointer pointer1 = mock(Pointer.class);
        Pointer pointer2 = mock(Pointer.class);
        
        when(arg1.compute(eq(evalContext))).thenReturn(pointer1);
        when(arg2.compute(eq(evalContext))).thenReturn(pointer2);
        when(pointer1.getValue()).thenReturn("value1");
        when(pointer2.getValue()).thenReturn("value2");
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual_WithEqualBooleanValues() {
        when(arg1.compute(eq(evalContext))).thenReturn(true);
        when(arg2.compute(eq(evalContext))).thenReturn(true);
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertTrue(result);
    }
    
    @Test
    public void testNotEqual_WithNotEqualBooleanValues() {
        when(arg1.compute(eq(evalContext))).thenReturn(true);
        when(arg2.compute(eq(evalContext))).thenReturn(false);
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual_WithEqualNumberValues() {
        when(arg1.compute(eq(evalContext))).thenReturn(10);
        when(arg2.compute(eq(evalContext))).thenReturn(10);
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertTrue(result);
    }
    
    @Test
    public void testNotEqual_WithNotEqualNumberValues() {
        when(arg1.compute(eq(evalContext))).thenReturn(10.5);
        when(arg2.compute(eq(evalContext))).thenReturn(10);
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual_WithEqualStringValues() {
        when(arg1.compute(eq(evalContext))).thenReturn("hello");
        when(arg2.compute(eq(evalContext))).thenReturn("hello");
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertTrue(result);
    }
    
    @Test
    public void testNotEqual_WithNotEqualStringValues() {
        when(arg1.compute(eq(evalContext))).thenReturn("hello");
        when(arg2.compute(eq(evalContext))).thenReturn("world");
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual_WithEqualValuesOfTypeInitialContext() {
        EvalContext evalContext1 = mock(EvalContext.class);
        EvalContext evalContext2 = mock(EvalContext.class);
        
        when(arg1.compute(eq(evalContext))).thenReturn(evalContext1);
        when(arg2.compute(eq(evalContext))).thenReturn(evalContext2);
        when(evalContext1.getSingleNodePointer()).thenReturn("value");
        when(evalContext2.getSingleNodePointer()).thenReturn("value");
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertTrue(result);
    }
    
    @Test
    public void testNotEqual_WithNotEqualValuesOfTypeInitialContext() {
        EvalContext evalContext1 = mock(EvalContext.class);
        EvalContext evalContext2 = mock(EvalContext.class);
        
        when(arg1.compute(eq(evalContext))).thenReturn(evalContext1);
        when(arg2.compute(eq(evalContext))).thenReturn(evalContext2);
        when(evalContext1.getSingleNodePointer()).thenReturn("value1");
        when(evalContext2.getSingleNodePointer()).thenReturn("value2");
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual_WithEqualValuesOfTypeCollection() {
        Collection<Object> collection1 = new HashSet<>();
        Collection<Object> collection2 = new HashSet<>();
        
        collection1.add("value");
        collection2.add("value");
        
        when(arg1.compute(eq(evalContext))).thenReturn(collection1);
        when(arg2.compute(eq(evalContext))).thenReturn(collection2);
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertTrue(result);
    }
    
    @Test
    public void testNotEqual_WithNotEqualValuesOfTypeCollection() {
        Collection<Object> collection1 = new HashSet<>();
        Collection<Object> collection2 = new HashSet<>();
        
        collection1.add("value1");
        collection2.add("value2");
        
        when(arg1.compute(eq(evalContext))).thenReturn(collection1);
        when(arg2.compute(eq(evalContext))).thenReturn(collection2);
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual_WithEqualValuesOfTypeIterator() {
        Iterator<Object> iterator1 = mock(Iterator.class);
        Iterator<Object> iterator2 = mock(Iterator.class);
        
        when(arg1.compute(eq(evalContext))).thenReturn(iterator1);
        when(arg2.compute(eq(evalContext))).thenReturn(iterator2);
        when(iterator1.hasNext()).thenReturn(true, false);
        when(iterator2.hasNext()).thenReturn(true, false);
        when(iterator1.next()).thenReturn("value");
        when(iterator2.next()).thenReturn("value");
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertTrue(result);
    }
    
    @Test
    public void testNotEqual_WithNotEqualValuesOfTypeIterator() {
        Iterator<Object> iterator1 = mock(Iterator.class);
        Iterator<Object> iterator2 = mock(Iterator.class);
        
        when(arg1.compute(eq(evalContext))).thenReturn(iterator1);
        when(arg2.compute(eq(evalContext))).thenReturn(iterator2);
        when(iterator1.hasNext()).thenReturn(true, false);
        when(iterator2.hasNext()).thenReturn(true, false);
        when(iterator1.next()).thenReturn("value1");
        when(iterator2.next()).thenReturn("value2");
        
        boolean result = coreOperationCompare.equal(evalContext, arg1, arg2);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch_WithMatch() {
        Iterator<Object> iterator1 = mock(Iterator.class);
        Iterator<Object> iterator2 = mock(Iterator.class);
        
        when(iterator1.hasNext()).thenReturn(true, false);
        when(iterator2.hasNext()).thenReturn(true, false);
        when(iterator1.next()).thenReturn("value");
        when(iterator2.next()).thenReturn("value");
        
        boolean result = coreOperationCompare.findMatch(iterator1, iterator2);
        
        assertTrue(result);
    }
    
    @Test
    public void testFindMatch_WithNoMatch() {
        Iterator<Object> iterator1 = mock(Iterator.class);
        Iterator<Object> iterator2 = mock(Iterator.class);
        
        when(iterator1.hasNext()).thenReturn(true, false);
        when(iterator2.hasNext()).thenReturn(true, false);
        when(iterator1.next()).thenReturn("value1");
        when(iterator2.next()).thenReturn("value2");
        
        boolean result = coreOperationCompare.findMatch(iterator1, iterator2);
        
        assertFalse(result);
    }
    
    @Test
    public void testContains_WithContainsValue() {
        Iterator<Object> iterator = mock(Iterator.class);
        
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("value");
        
        boolean result = coreOperationCompare.contains(iterator, "value");
        
        assertTrue(result);
    }
    
    @Test
    public void testContains_WithNoContainsValue() {
        Iterator<Object> iterator = mock(Iterator.class);
        
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("value1");
        
        boolean result = coreOperationCompare.contains(iterator, "value2");
        
        assertFalse(result);
    }
    
    @Test
    public void testComputeValue() {
        Object result = coreOperationCompare.computeValue(evalContext);
        
        assertNull(result);
    }
    
    private boolean equal(Object l, Object r) {
        if (l instanceof Pointer && r instanceof Pointer) {
            if (l.equals(r)) {
                return true;
            }
        }

        if (l instanceof Pointer) {
            l = ((Pointer) l).getValue();
        }

        if (r instanceof Pointer) {
            r = ((Pointer) r).getValue();
        }

        if (l == r) {
            return true;
        }

        if (l instanceof Boolean || r instanceof Boolean) {
            return (InfoSetUtil.booleanValue(l) == InfoSetUtil.booleanValue(r));
        }
        if (l instanceof Number || r instanceof Number) {
            return (InfoSetUtil.doubleValue(l) == InfoSetUtil.doubleValue(r));
        }
        if (l instanceof String || r instanceof String) {
            return (
                InfoSetUtil.stringValue(l).equals(InfoSetUtil.stringValue(r)));
        }
        return l != null && l.equals(r);
    }
}
