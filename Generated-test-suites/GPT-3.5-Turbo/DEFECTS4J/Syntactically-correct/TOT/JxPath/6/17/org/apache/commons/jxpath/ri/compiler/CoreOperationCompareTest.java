package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    
    @Before
    public void setup() {
        coreOperationCompare = new CoreOperationCompare(new ExpressionMock(), new ExpressionMock());
    }

    @Test
    public void testEqual_withNullPointers_returnTrue() {
        EvalContext context = null;
        ExpressionMock left = null;
        ExpressionMock right = null;
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_withInitialContextPointers_returnTrue() {
        InitialContext initialContext = new InitialContext(new JXPathContext());
        EvalContext context = initialContext;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_withSelfContextPointers_returnTrue() {
        SelfContext selfContext = new SelfContext(new JXPathContext());
        EvalContext context = selfContext;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_withCollectionPointers_returnTrue() {
        Collection<Object> collection = new ArrayList<>();
        collection.add("test");
        Iterator<Object> iterator = collection.iterator();
        EvalContext context = iterator;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_withDifferentIterators_returnFalse() {
        Collection<Object> collection1 = new ArrayList<>();
        collection1.add("test");
        Iterator<Object> iterator1 = collection1.iterator();
        Collection<Object> collection2 = new ArrayList<>();
        collection2.add("test");
        Iterator<Object> iterator2 = collection2.iterator();
        EvalContext context = iterator1;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual_withEqualIterators_returnTrue() {
        Collection<Object> collection1 = new ArrayList<>();
        collection1.add("test");
        Iterator<Object> iterator1 = collection1.iterator();
        Collection<Object> collection2 = new ArrayList<>();
        collection2.add("test");
        Iterator<Object> iterator2 = collection2.iterator();
        EvalContext context = iterator1;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_withEqualObjects_returnTrue() {
        Object obj1 = new Object();
        EvalContext context = null;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        left.setValue(obj1);
        right.setValue(obj1);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_withDifferentObjects_returnFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        EvalContext context = null;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        left.setValue(obj1);
        right.setValue(obj2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual_withBooleanObjects_returnTrue() {
        Object obj1 = Boolean.TRUE;
        Object obj2 = Boolean.TRUE;
        EvalContext context = null;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        left.setValue(obj1);
        right.setValue(obj2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_withDifferentBooleanObjects_returnFalse() {
        Object obj1 = Boolean.TRUE;
        Object obj2 = Boolean.FALSE;
        EvalContext context = null;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        left.setValue(obj1);
        right.setValue(obj2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual_withNumberObjects_returnTrue() {
        Object obj1 = 1;
        Object obj2 = 1;
        EvalContext context = null;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        left.setValue(obj1);
        right.setValue(obj2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_withDifferentNumberObjects_returnFalse() {
        Object obj1 = 1;
        Object obj2 = 2;
        EvalContext context = null;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        left.setValue(obj1);
        right.setValue(obj2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual_withStringObjects_returnTrue() {
        Object obj1 = "test";
        Object obj2 = "test";
        EvalContext context = null;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        left.setValue(obj1);
        right.setValue(obj2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_withDifferentStringObjects_returnFalse() {
        Object obj1 = "test";
        Object obj2 = "test2";
        EvalContext context = null;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        left.setValue(obj1);
        right.setValue(obj2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqual_withBothNullObjects_returnFalse() {
        Object obj1 = null;
        Object obj2 = null;
        EvalContext context = null;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        left.setValue(obj1);
        right.setValue(obj2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testContains_withEmptyIterator_returnFalse() {
        Iterator<Object> iterator = new ArrayList<>().iterator();
        Object value = new Object();
        
        boolean result = coreOperationCompare.contains(iterator, value);
        
        assertFalse(result);
    }
    
    @Test
    public void testContains_withElementNotPresent_returnFalse() {
        Collection<Object> collection = new ArrayList<>();
        collection.add("test");
        Iterator<Object> iterator = collection.iterator();
        Object value = new Object();
        
        boolean result = coreOperationCompare.contains(iterator, value);
        
        assertFalse(result);
    }
    
    @Test
    public void testContains_withElementPresent_returnTrue() {
        Collection<Object> collection = new ArrayList<>();
        collection.add("test");
        Iterator<Object> iterator = collection.iterator();
        Object value = "test";
        
        boolean result = coreOperationCompare.contains(iterator, value);
        
        assertTrue(result);
    }
    
    @Test
    public void testFindMatch_withEmptyIterators_returnFalse() {
        Iterator<Object> lit = new ArrayList<>().iterator();
        Iterator<Object> rit = new ArrayList<>().iterator();
        
        boolean result = coreOperationCompare.findMatch(lit, rit);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch_withDifferentIterators_returnFalse() {
        Collection<Object> collection = new ArrayList<>();
        collection.add("test");
        Iterator<Object> lit = collection.iterator();
        Iterator<Object> rit = new ArrayList<>().iterator();
        
        boolean result = coreOperationCompare.findMatch(lit, rit);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch_withMatchedIterators_returnTrue() {
        Collection<Object> collection = new ArrayList<>();
        collection.add("test");
        Iterator<Object> lit = collection.iterator();
        Iterator<Object> rit = collection.iterator();
        
        boolean result = coreOperationCompare.findMatch(lit, rit);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_withEqualPointers_returnTrue() {
        Pointer pointer1 = new PointerMock();
        Pointer pointer2 = new PointerMock();
        EvalContext context = null;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        left.setValue(pointer1);
        right.setValue(pointer2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqual_withDifferentPointers_returnFalse() {
        Pointer pointer1 = new PointerMock();
        Pointer pointer2 = new PointerMock();
        EvalContext context = null;
        ExpressionMock left = new ExpressionMock();
        ExpressionMock right = new ExpressionMock();
        left.setValue(pointer1);
        right.setValue(pointer2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }

    private class ExpressionMock extends Expression {
        
        private Object value;

        @Override
        public Object compute(EvalContext arg0) {
            return value;
        }
        
        public void setValue(Object value) {
            this.value = value;
        }
    }
    
    private class PointerMock implements Pointer {

        @Override
        public Object getBaseValue() {
            return null;
        }

        @Override
        public void setValue(Object arg0) {
            
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public boolean isActual() {
            return false;
        }
        
    }
}