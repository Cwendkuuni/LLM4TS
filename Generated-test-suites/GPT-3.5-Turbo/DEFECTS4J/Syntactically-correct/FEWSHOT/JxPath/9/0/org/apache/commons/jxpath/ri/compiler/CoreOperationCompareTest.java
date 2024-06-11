package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        Expression left = new Expression();
        Expression right = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        
        assertEquals(true, coreOperationCompare.equal(context, left, right));
    }
    
    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        assertEquals(false, coreOperationCompare.contains(it, value));
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        assertEquals(false, coreOperationCompare.findMatch(lit, rit));
    }
    
    @Test
    public void testEqualObjects() {
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        assertEquals(true, coreOperationCompare.equal(l, r));
    }
    
    @Test
    public void testEqualPointers() {
        Pointer l = new Pointer();
        Pointer r = new Pointer();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        assertEquals(true, coreOperationCompare.equal(l, r));
    }
    
    @Test
    public void testEqualBoolean() {
        Object l = new Boolean(true);
        Object r = new Boolean(false);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        assertEquals(false, coreOperationCompare.equal(l, r));
    }
    
    @Test
    public void testEqualNumber() {
        Object l = new Double(1.0);
        Object r = new Double(2.0);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        assertEquals(false, coreOperationCompare.equal(l, r));
    }
    
    @Test
    public void testEqualString() {
        Object l = "Hello";
        Object r = "World";
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        assertEquals(false, coreOperationCompare.equal(l, r));
    }
    
    @Test
    public void testEqualNull() {
        Object l = null;
        Object r = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        assertEquals(false, coreOperationCompare.equal(l, r));
    }
}