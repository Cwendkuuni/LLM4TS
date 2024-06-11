package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Array;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Test when both objects are null
        assertTrue(Equality.areEqual(null, null));

        // Test when one object is null and the other is not
        assertFalse(Equality.areEqual(null, "Hello"));
        assertFalse(Equality.areEqual("Hello", null));

        // Test when both objects are arrays and their elements are equal
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));

        // Test when both objects are arrays but their lengths are different
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2, 3, 4};
        assertFalse(Equality.areEqual(arr3, arr4));

        // Test when both objects are arrays with equal lengths but different elements
        int[] arr5 = {1, 2, 3};
        int[] arr6 = {1, 2, 4};
        assertFalse(Equality.areEqual(arr5, arr6));

        // Test when both objects are not arrays but are equal
        String str1 = "Hello";
        String str2 = "Hello";
        assertTrue(Equality.areEqual(str1, str2));

        // Test when both objects are not arrays but are not equal
        String str3 = "Hello";
        String str4 = "World";
        assertFalse(Equality.areEqual(str3, str4));
    }

    @Test
    public void testAreArraysEqual() {
        // Test when both arrays have equal lengths and equal elements
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));

        // Test when both arrays have different lengths
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2, 3, 4};
        assertFalse(Equality.areArraysEqual(arr3, arr4));

        // Test when both arrays have equal lengths but different elements
        int[] arr5 = {1, 2, 3};
        int[] arr6 = {1, 2, 4};
        assertFalse(Equality.areArraysEqual(arr5, arr6));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        // Test when both arrays have equal lengths
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));

        // Test when both arrays have different lengths
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2, 3, 4};
        assertFalse(Equality.areArrayLengthsEqual(arr3, arr4));
    }

    @Test
    public void testAreArrayElementsEqual() {
        // Test when both arrays have equal lengths and equal elements
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));

        // Test when both arrays have equal lengths but different elements
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(arr3, arr4));
    }

    @Test
    public void testIsArray() {
        // Test when the object is an array
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));

        // Test when the object is not an array
        String str = "Hello";
        assertFalse(Equality.isArray(str));
    }
}

