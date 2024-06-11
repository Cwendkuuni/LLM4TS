package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {
    
    @Test
    public void testAreEqual() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(obj1, null));
        assertFalse(Equality.areEqual(null, obj2));
        assertTrue(Equality.areEqual(obj1, obj1));
        assertFalse(Equality.areEqual(obj1, obj2));
    }
    
    @Test
    public void testAreArraysEqual() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        Object[] array3 = {1, 2, 4};
        assertTrue(Equality.areArraysEqual(array1, array1));
        assertTrue(Equality.areArraysEqual(array1, array2));
        assertFalse(Equality.areArraysEqual(array1, array3));
    }
    
    @Test
    public void testAreArrayLengthsEqual() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        Object[] array3 = {1, 2, 3, 4};
        assertTrue(Equality.areArrayLengthsEqual(array1, array1));
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
        assertFalse(Equality.areArrayLengthsEqual(array1, array3));
    }
    
    @Test
    public void testAreArrayElementsEqual() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        Object[] array3 = {1, 2, 4};
        assertTrue(Equality.areArrayElementsEqual(array1, array1));
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
        assertFalse(Equality.areArrayElementsEqual(array1, array3));
    }
    
    @Test
    public void testIsArray() {
        Object[] array = {1, 2, 3};
        Object notArray = new Object();
        assertTrue(Equality.isArray(array));
        assertFalse(Equality.isArray(notArray));
    }
    
}