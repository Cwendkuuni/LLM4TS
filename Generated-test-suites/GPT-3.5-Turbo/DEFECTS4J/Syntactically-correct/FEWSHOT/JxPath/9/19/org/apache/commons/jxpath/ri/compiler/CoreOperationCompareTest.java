package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object l = new Object();
        Object r = new Object();
        
        // Test when l = r
        arg1.setValue(l);
        arg2.setValue(l);
        assertTrue(operation.equal(context, arg1, arg2));
        
        // Test when l and r are Collection
        java.util.Collection col1 = new java.util.ArrayList();
        col1.add("element1");
        col1.add("element2");
        arg1.setValue(col1);
        arg2.setValue(col1);
        assertTrue(operation.equal(context, arg1, arg2));
        
        // Test when l is Iterator and r is not
        java.util.Iterator it1 = col1.iterator();
        arg1.setValue(it1);
        arg2.setValue(l);
        assertFalse(operation.equal(context, arg1, arg2));
        
        // Test when l is Iterator and r is Iterator
        java.util.Collection col2 = new java.util.ArrayList();
        col2.add("element3");
        col2.add("element4");
        java.util.Iterator it2 = col2.iterator();
        arg1.setValue(it1);
        arg2.setValue(it2);
        assertFalse(operation.equal(context, arg1, arg2));
        
        // Test when l is Iterator and r is in iterator
        arg1.setValue(it1);
        arg2.setValue("element1");
        assertTrue(operation.equal(context, arg1, arg2));
        
        // Test when l is Pointer and r is Pointer
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();
        arg1.setValue(pointer1);
        arg2.setValue(pointer2);
        assertFalse(operation.equal(context, arg1, arg2));
        
        // Test when l is Pointer and r is not
        arg1.setValue(pointer1);
        arg2.setValue(l);
        assertFalse(operation.equal(context, arg1, arg2));
        
        // Test when l and r are Boolean
        arg1.setValue(true);
        arg2.setValue(true);
        assertTrue(operation.equal(context, arg1, arg2));
        
        // Test when l is NaN
        arg1.setValue(Double.NaN);
        arg2.setValue(0.0);
        assertFalse(operation.equal(context, arg1, arg2));
        
        // Test when l and r are Number
        arg1.setValue(10);
        arg2.setValue(10);
        assertTrue(operation.equal(context, arg1, arg2));
        
        // Test when l and r are String
        arg1.setValue("hello");
        arg2.setValue("hello");
        assertTrue(operation.equal(context, arg1, arg2));
        
        // Test when l is null and r is not
        arg1.setValue(null);
        arg2.setValue(l);
        assertFalse(operation.equal(context, arg1, arg2));
        
        // Test when l is equal to r
        arg1.setValue(l);
        arg2.setValue(r);
        assertFalse(operation.equal(context, arg1, arg2));
    }
    
    @Test
    public void testContains() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        java.util.ArrayList list = new java.util.ArrayList();
        list.add("element1");
        list.add("element2");
        java.util.Iterator it = list.iterator();
        Object value = "element1";
        
        assertTrue(operation.contains(it, value));
        assertFalse(operation.contains(it, "element3"));
    }
    
    @Test
    public void testFindMatch() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        java.util.ArrayList left = new java.util.ArrayList();
        java.util.ArrayList right = new java.util.ArrayList();
        left.add("element1");
        left.add("element2");
        right.add("element2");
        
        java.util.Iterator lit = left.iterator();
        java.util.Iterator rit = right.iterator();
        
        assertTrue(operation.findMatch(lit, rit));
        assertFalse(operation.findMatch(lit, rit));
    }
    
    @Test
    public void testEqualObjectObject() {
        Object l = new Object();
        Object r = new Object();
        assertTrue(CoreOperationCompare.equal(l, l));
        assertFalse(CoreOperationCompare.equal(l, r));
    }
}