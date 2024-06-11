package org.mockito.internal.matchers;

import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Test cases for when either o1 or o2 is null
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, 5));
        assertFalse(Equality.areEqual(5, null));
        
        // Test cases for when o1 and o2 are arrays
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
        
        Object[] array3 = {1, 2, 3};
        Object[] array4 = {1, 2, 4};
        assertFalse(Equality.areEqual(array3, array4));
        
        // Test cases for when o1 and o2 are not arrays
        assertTrue(Equality.areEqual("hello", "hello"));
        assertFalse(Equality.areEqual("hello", "world"));
        assertFalse(Equality.areEqual("hello", 5));
    }

    @Test
    public void testAreArraysEqual() {
        // Test cases for when o1 and o2 have equal length arrays with equal elements
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(array1, array2));
        
        Object[] array3 = {"hello", "world"};
        Object[] array4 = {"hello", "world"};
        assertTrue(Equality.areArraysEqual(array3, array4));
        
        // Test cases for when o1 and o2 have equal length arrays with non-equal elements
        Object[] array5 = {1, 2, 3};
        Object[] array6 = {1, 2, 4};
        assertFalse(Equality.areArraysEqual(array5, array5));
        
        // Test cases for when o1 and o2 have different length arrays
        Object[] array7 = {1, 2, 3};
        Object[] array8 = {1, 2, 3, 4};
        assertFalse(Equality.areArraysEqual(array7, array8));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        // Test cases for when o1 and o2 have equal length arrays
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
        
        Object[] array3 = {"hello", "world"};
        Object[] array4 = {"hello", "world"};
        assertTrue(Equality.areArrayLengthsEqual(array3, array4));
        
        // Test cases for when o1 and o2 have different length arrays
        Object[] array5 = {1, 2, 3};
        Object[] array6 = {1, 2, 3, 4};
        assertFalse(Equality.areArrayLengthsEqual(array5, array6));
    }

    @Test
    public void testAreArrayElementsEqual() {
        // Test cases for when o1 and o2 have equal length arrays with equal elements
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
        
        Object[] array3 = {"hello", "world"};
        Object[] array4 = {"hello", "world"};
        assertTrue(Equality.areArrayElementsEqual(array3, array4));
        
        // Test cases for when o1 and o2 have equal length arrays with non-equal elements
        Object[] array5 = {1, 2, 3};
        Object[] array6 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(array5, array5));
    }

    @Test
    public void testIsArray() {
        // Test cases for when o is an array
        Object[] array1 = {1, 2, 3};
        assertTrue(Equality.isArray(array1));
        
        String[] array2 = {"hello", "world"};
        assertTrue(Equality.isArray(array2));
        
        // Test cases for when o is not an array
        String str = "hello";
        assertFalse(Equality.isArray(str));
        
        Integer num = 5;
        assertFalse(Equality.isArray(num));
    }
}