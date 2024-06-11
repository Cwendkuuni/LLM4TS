package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        String s1 = "hello";
        String s2 = "hello";
        String s3 = "world";
        
        assertTrue(Equality.areEqual(s1, s1));
        
        assertTrue(Equality.areEqual(s1, s2));
        
        assertFalse(Equality.areEqual(s1, s3));
        
        assertFalse(Equality.areEqual(s1, null));
        
        assertFalse(Equality.areEqual(null, s3));
        
        assertTrue(Equality.areEqual(null, null));
    }
    
    @Test
    public void testAreArraysEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        int[] arr3 = {4, 5, 6};
        int[] arr4 = {1, 2, 4};
        
        assertTrue(Equality.areArraysEqual(arr1, arr1));
        
        assertTrue(Equality.areArraysEqual(arr1, arr2));
        
        assertFalse(Equality.areArraysEqual(arr1, arr3));
        
        assertFalse(Equality.areArraysEqual(arr1, arr4));
    }
    
    @Test
    public void testAreArrayLengthsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3, 4};
        int[] arr3 = {1, 2};
        
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr1));
        
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
        
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr3));
    }
    
    @Test
    public void testAreArrayElementsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        int[] arr3 = {4, 5, 6};
        int[] arr4 = {1, 2, 4};
        
        assertTrue(Equality.areArrayElementsEqual(arr1, arr1));
        
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
        
        assertFalse(Equality.areArrayElementsEqual(arr1, arr3));
        
        assertFalse(Equality.areArrayElementsEqual(arr1, arr4));
    }
    
    @Test
    public void testIsArray() {
        int[] arr = {1, 2, 3};
        String str = "hello";
        
        assertTrue(Equality.isArray(arr));
        
        assertFalse(Equality.isArray(str));
    }
}