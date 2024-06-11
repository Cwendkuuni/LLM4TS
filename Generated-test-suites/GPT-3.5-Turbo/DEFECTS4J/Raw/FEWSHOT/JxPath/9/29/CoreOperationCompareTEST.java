package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {
    
    private CoreOperationCompare operationCompare = new CoreOperationCompareMock(null, null);
    
    @Test
    public void testGetPrecedence() {
        assertEquals(2, operationCompare.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        assertTrue(operationCompare.isSymmetric());
    }
    
    @Test
    public void testEqualWithEqualPointers() {
        Pointer p1 = new PointerMock();
        Pointer p2 = new PointerMock();
        assertTrue(operationCompare.equal(p1, p2));
    }
    
    @Test
    public void testEqualWithDifferentPointers() {
        Pointer p1 = new PointerMock();
        Pointer p2 = new Pointer();
        assertFalse(operationCompare.equal(p1, p2));
    }
    
    @Test
    public void testEqualWithEqualBooleanValues() {
        assertTrue(operationCompare.equal(true, true));
    }
    
    @Test
    public void testEqualWithDifferentBooleanValues() {
        assertFalse(operationCompare.equal(true, false));
    }
    
    @Test
    public void testEqualWithEqualNumbers() {
        assertTrue(operationCompare.equal(1, 1));
    }
    
    @Test
    public void testEqualWithDifferentNumbers() {
        assertFalse(operationCompare.equal(1, 2));
    }
    
    @Test
    public void testEqualWithEqualStrings() {
        assertTrue(operationCompare.equal("abc", "abc"));
    }
    
    @Test
    public void testEqualWithDifferentStrings() {
        assertFalse(operationCompare.equal("abc", "def"));
    }
    
    @Test
    public void testEqualWithNullValues() {
        assertFalse(operationCompare.equal(null, "abc"));
        assertFalse(operationCompare.equal("abc", null));
        assertTrue(operationCompare.equal(null, null));
    }
    
    @Test
    public void testEqualWithCollectionAndValue() {
        List<String> collection = new ArrayList<>();
        collection.add("a");
        collection.add("b");
        assertTrue(operationCompare.equal(collection.iterator(), "a"));
        assertFalse(operationCompare.equal(collection.iterator(), "c"));
    }
    
    @Test
    public void testEqualWithCollections() {
        List<String> collection1 = new ArrayList<>();
        collection1.add("a");
        collection1.add("b");
        List<String> collection2 = new ArrayList<>();
        collection2.add("b");
        assertTrue(operationCompare.equal(collection1.iterator(), collection2.iterator()));
    }
    
    @Test
    public void testEqualWithCollectionsAndDifferentTypes() {
        List<String> collection1 = new ArrayList<>();
        collection1.add("a");
        collection1.add("b");
        Collection<String> collection2 = Arrays.asList("b", "c");
        assertTrue(operationCompare.equal(collection1.iterator(), collection2.iterator()));
    }
    
    private class CoreOperationCompareMock extends CoreOperationCompare {
        
        public CoreOperationCompareMock(Expression arg1, Expression arg2) {
            super(arg1, arg2);
        }
        
        protected boolean findMatch(Iterator lit, Iterator rit) {
            List<Object> left = new ArrayList<>();
            while (lit.hasNext()) {
                left.add(lit.next());
            }
            while (rit.hasNext()) {
                if (contains(left.iterator(), rit.next())) {
                    return true;
                }
            }
            return false;
        }
        
        protected boolean contains(Iterator it, Object value) {
            while (it.hasNext()) {
                Object element = it.next();
                if (equal(element, value)) {
                    return true;
                }
            }
            return false;
        }
        
        protected boolean equal(Object l, Object r) {
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
                return (boolean) l == (boolean) r;
            }
            if (l instanceof Number || r instanceof Number) {
                return (double) l == (double) r;
            }
            if (l instanceof String || r instanceof String) {
                return String.valueOf(l).equals(String.valueOf(r));
            }
            return l != null && l.equals(r);
        }
        
        protected Object compute(EvalContext context) {
            return null;
        }
    }
    
    private class PointerMock extends Pointer {
        
        public Object getValue() {
            return null;
        }
    }
}