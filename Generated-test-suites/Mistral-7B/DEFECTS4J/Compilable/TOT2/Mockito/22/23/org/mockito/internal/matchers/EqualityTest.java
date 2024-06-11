package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualityTest {

    // Test cases for areEqual(Object o1, Object o2)

    @Test
    public void testAreEqual_SameObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_DifferentObjects_Null() {
        Object obj1 = null;
        Object obj2 = null;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_DifferentObjects_NotArrays() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    // Add test cases for other scenarios

    // Test cases for areArraysEqual(Object o1, Object o2)

    @Test
    public void testAreArraysEqual_SameArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = arr1.clone();
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_DifferentArrays_DifferentLengths() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_DifferentArrays_SameLengths_DifferentElements() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_DifferentArrays_SameLengths_SameElementsInDifferentOrder() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {2, 3, 1};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    // Add test cases for other scenarios

    // Test cases for areArrayLengthsEqual(Object o1, Object o2)

    @Test
    public void testAreArrayLengthsEqual_SameArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = arr1.clone();
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_DifferentArrays_DifferentLengths() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_DifferentArrays_SameLengths() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3, 4};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    // Add test cases for other scenarios

    // Test cases for areArrayElementsEqual(Object o1, Object o2)

    @Test
    public void testAreArrayElementsEqual_SameArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = arr1.clone();
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_DifferentArrays_DifferentLengths() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_DifferentArrays_SameLengths_DifferentElements() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_DifferentArrays_SameLengths_SameElementsInDifferentOrder() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {2, 3, 1};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    // Test cases for isArray(Object o)

    @Test
    public void testIsArray_Array() {
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArray_NonArray() {
        String str = "Hello World";
        assertFalse(Equality.isArray(str));
    }

    // Add test cases for other scenarios

}