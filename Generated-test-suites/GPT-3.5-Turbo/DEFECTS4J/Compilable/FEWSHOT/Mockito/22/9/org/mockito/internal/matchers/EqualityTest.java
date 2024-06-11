package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        //Test when both objects are null
        assertTrue(Equality.areEqual(null, null));

        //Test when one object is null and the other is not
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));

        //Test when both objects are not null and are equal
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Equality.areEqual(obj1, obj2));

        //Test when both objects are not null and are not equal
        Object obj3 = new Object();
        assertFalse(Equality.areEqual(obj1, obj3));

        //Test when both objects are arrays and are equal
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));

        //Test when both objects are arrays and are not equal
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {4, 5, 6};
        assertFalse(Equality.areEqual(arr3, arr4));
    }

    @Test
    public void testAreArraysEqual() {
        //Test when array lengths are equal and array elements are equal
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));

        //Test when array lengths are not equal
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2};
        assertFalse(Equality.areArraysEqual(arr3, arr4));

        //Test when array elements are not equal
        int[] arr5 = {1, 2, 3};
        int[] arr6 = {4, 5, 6};
        assertFalse(Equality.areArraysEqual(arr5, arr6));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        //Test when array lengths are equal
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));

        //Test when array lengths are not equal
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(arr3, arr4));
    }

    @Test
    public void testAreArrayElementsEqual() {
        //Test when array elements are equal
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));

        //Test when array elements are not equal
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {4, 5, 6};
        assertFalse(Equality.areArrayElementsEqual(arr3, arr4));
    }

    @Test
    public void testIsArray() {
        //Test when object is an array
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));

        //Test when object is not an array
        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
    }
}