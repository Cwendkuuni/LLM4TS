package org.mockito.internal.matchers;
import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {
    
    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual("hello", null));
        assertFalse(Equality.areEqual(null, "hello"));
        assertTrue(Equality.areEqual("hello", "hello"));
    }
    
    @Test
    public void testAreArraysEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
       int[] arr3 = {2, 3, 4};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
        assertFalse(Equality.areArraysEqual(arr1, arr3));
    }
    
    @Test
    public void testAreArrayLengthsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3, 4};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }
    
    @Test
    public void testAreArrayElementsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        int[] arr3 = {1, 2, 4};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
        assertFalse(Equality.areArrayElementsEqual(arr1, arr3));
    }
    
    @Test
    public void testIsArray() {
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
        assertFalse(Equality.isArray("hello"));
    }
}