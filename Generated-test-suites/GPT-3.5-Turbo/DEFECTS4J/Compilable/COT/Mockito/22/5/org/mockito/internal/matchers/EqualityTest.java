package org.mockito.internal.matchers;

import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqualObjects() {
        // Basic test case with two equal objects
        assertTrue(Equality.areEqual("hello", "hello"));
        
        // Basic test case with two different objects
        assertFalse(Equality.areEqual("hello", "world"));
        
        // Test case with two null objects
        assertTrue(Equality.areEqual(null, null));
        
        // Test case with one null object and one non-null object
        assertFalse(Equality.areEqual(null, "hello"));
        
        // Test case with one non-null object and one null object
        assertFalse(Equality.areEqual("hello", null));
    }

    @Test
    public void testAreEqualArrays() {
        // Test case with two equal arrays
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
        
        // Test case with two different arrays
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {4, 5, 6};
        assertFalse(Equality.areEqual(arr3, arr4));
        
        // Test case with two arrays of different types 
        int[] arr5 = {1, 2, 3};
        String[] arr6 = {"1", "2", "3"};
        assertFalse(Equality.areEqual(arr5, arr6));
        
        // Test case with two arrays of different lengths
        int[] arr7 = {1, 2, 3};
        int[] arr8 = {1, 2, 3, 4};
        assertFalse(Equality.areEqual(arr7, arr8));
        
        // Test case with one null array and one non-null array
        int[] arr9 = {1, 2, 3};
        int[] arr10 = null;
        assertFalse(Equality.areEqual(arr9, arr10));
        
        // Test case with one non-null array and one null array
        int[] arr11 = null;
        int[] arr12 = {1, 2, 3};
        assertFalse(Equality.areEqual(arr11, arr12));
    }
}
