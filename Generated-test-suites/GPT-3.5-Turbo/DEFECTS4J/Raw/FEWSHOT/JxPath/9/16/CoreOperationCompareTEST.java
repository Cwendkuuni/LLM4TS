package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        // Compare two null values
        assertTrue(operation.equal(null, null));
        
        // Compare null and non-null value
        assertFalse(operation.equal(null, "test"));
        assertFalse(operation.equal("test", null));
        
        // Compare two Boolean values
        assertTrue(operation.equal(true, true));
        assertTrue(operation.equal(false, false));
        assertFalse(operation.equal(true, false));
        assertFalse(operation.equal(false, true));
        
        // Compare two Number values
        assertTrue(operation.equal(10, 10));
        assertFalse(operation.equal(10, 20));
        assertTrue(operation.equal(10.5, 10.5));
        assertFalse(operation.equal(10.5, 20.5));
        
        // Compare two String values
        assertTrue(operation.equal("test", "test"));
        assertFalse(operation.equal("test", "Test"));
        assertFalse(operation.equal("test", "test2"));
        
        // Compare two Pointer values
        Object pointer1 = new Pointer();
        Object pointer2 = new Pointer();
        assertTrue(operation.equal(pointer1, pointer1));
        assertFalse(operation.equal(pointer1, pointer2));
        
        // Compare different types of values
        assertFalse(operation.equal(true, "test"));
        assertFalse(operation.equal(10, "test"));
        assertFalse(operation.equal("test", 10));
    }
    
    @Test
    public void testContains() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        Iterator<Integer> it = new ArrayList<>(Arrays.asList(1, 2, 3)).iterator();
        Integer value = 2;
        
        // Value is contained in the iterator
        assertTrue(operation.contains(it, value));
        
        // Value is not contained in the iterator
        assertFalse(operation.contains(it, 4));
    }
    
    @Test
    public void testFindMatch() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        Iterator<Integer> left = new ArrayList<>(Arrays.asList(1, 2, 3)).iterator();
        Iterator<Integer> right = new ArrayList<>(Arrays.asList(2, 3, 4)).iterator();
        
        // Common element exists in both iterators
        assertTrue(operation.findMatch(left, right));
        
        left = new ArrayList<>(Arrays.asList(1, 2, 3)).iterator();
        right = new ArrayList<>(Arrays.asList(4, 5, 6)).iterator();
        
        // No common element exists in both iterators
        assertFalse(operation.findMatch(left, right));
    }
}