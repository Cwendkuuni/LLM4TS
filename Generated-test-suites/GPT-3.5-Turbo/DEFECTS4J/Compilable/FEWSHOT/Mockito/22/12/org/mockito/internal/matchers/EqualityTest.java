package org.mockito.internal.matchers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        // Test for non-array objects
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, "foo"));
        assertFalse(Equality.areEqual("foo", null));
        assertTrue(Equality.areEqual("foo", "foo"));
        assertFalse(Equality.areEqual("foo", "bar"));
        
        // Test for arrays
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        int[] arr3 = {1, 2, 4};
        int[] arr4 = {1, 2};
        assertTrue(Equality.areEqual(arr1, arr2));
        assertFalse(Equality.areEqual(arr1, arr3));
        assertFalse(Equality.areEqual(arr1, arr4));
        
        String[] strArr1 = {"foo", "bar"};
        String[] strArr2 = {"foo", "bar"};
        String[] strArr3 = {"bar", "foo"};
        String[] strArr4 = {"foo"};
        assertFalse(Equality.areEqual(arr1, strArr1));
        assertFalse(Equality.areEqual(strArr1, strArr3));
        assertFalse(Equality.areEqual(strArr1, strArr4));
        assertTrue(Equality.areEqual(strArr1, strArr2));
    }

    @Test
    public void testAreArraysEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        int[] arr3 = {1, 2, 4};
        int[] arr4 = {1, 2};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
        assertFalse(Equality.areArraysEqual(arr1, arr3));
        assertFalse(Equality.areArraysEqual(arr1, arr4));
        
        String[] strArr1 = {"foo", "bar"};
        String[] strArr2 = {"foo", "bar"};
        String[] strArr3 = {"bar", "foo"};
        String[] strArr4 = {"foo"};
        assertFalse(Equality.areArraysEqual(arr1, strArr1));
        assertFalse(Equality.areArraysEqual(strArr1, strArr3));
        assertFalse(Equality.areArraysEqual(strArr1, strArr4));
        assertTrue(Equality.areArraysEqual(strArr1, strArr2));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        int[] arr3 = {1, 2, 4};
        int[] arr4 = {1, 2};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr3));
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr4));
        
        String[] strArr1 = {"foo", "bar"};
        String[] strArr2 = {"foo", "bar"};
        String[] strArr3 = {"bar", "foo"};
        String[] strArr4 = {"foo"};
        assertFalse(Equality.areArrayLengthsEqual(arr1, strArr1));
        assertFalse(Equality.areArrayLengthsEqual(strArr1, strArr3));
        assertFalse(Equality.areArrayLengthsEqual(strArr1, strArr4));
        assertTrue(Equality.areArrayLengthsEqual(strArr1, strArr2));
    }

    @Test
    public void testAreArrayElementsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        int[] arr3 = {1, 2, 4};
        int[] arr4 = {1, 2};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
        assertFalse(Equality.areArrayElementsEqual(arr1, arr3));
        assertFalse(Equality.areArrayElementsEqual(arr1, arr4));
        
        String[] strArr1 = {"foo", "bar"};
        String[] strArr2 = {"foo", "bar"};
        String[] strArr3 = {"bar", "foo"};
        String[] strArr4 = {"foo"};
        assertFalse(Equality.areArrayElementsEqual(arr1, strArr1));
        assertFalse(Equality.areArrayElementsEqual(strArr1, strArr3));
        assertFalse(Equality.areArrayElementsEqual(strArr1, strArr4));
        assertTrue(Equality.areArrayElementsEqual(strArr1, strArr2));
    }

    @Test
    public void testIsArray() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        String[] strArr1 = {"foo", "bar"};
        String[] strArr2 = {"foo", "bar"};
        assertFalse(Equality.isArray(null));
        assertTrue(Equality.isArray(arr1));
        assertTrue(Equality.isArray(arr2));
        assertTrue(Equality.isArray(strArr1));
        assertTrue(Equality.isArray(strArr2));
    }
}