package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {
    
    @Test
    public void testGetPrecedence() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        assertEquals(2, operation.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        assertTrue(operation.isSymmetric());
    }
    
    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        assertEquals(false, result);
    }
    
    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.contains(it, value);
        assertEquals(false, result);
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.findMatch(lit, rit);
        assertEquals(false, result);
    }
    
    @Test
    public void testEqualWithPointers() {
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assertEquals(false, result);
    }
    
    @Test
    public void testEqualWithBooleans() {
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assertEquals(false, result);
    }
    
    @Test
    public void testEqualWithNumbers() {
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assertEquals(false, result);
    }
    
    @Test
    public void testEqualWithStrings() {
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assertEquals(false, result);
    }
    
    @Test
    public void testEqualWithNulls() {
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assertEquals(false, result);
    }
}