package org.mockito.internal.matchers;
import org.junit.Test;

import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_null() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_objects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Equality.areEqual(obj1, obj1));
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_arrays() {
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {1, 2, 3};
        Integer[] arr3 = {1, 2, 4};

        assertTrue(Equality.areEqual(arr1, arr1));
        assertTrue(Equality.areEqual(arr1, arr2));
        assertFalse(Equality.areEqual(arr1, arr3));

        String[] strArr1 = {"hello", "world"};
        String[] strArr2 = {"hello", "world"};
        String[] strArr3 = {"hello", "java"};

        assertTrue(Equality.areEqual(strArr1, strArr1));
        assertTrue(Equality.areEqual(strArr1, strArr2));
        assertFalse(Equality.areEqual(strArr1, strArr3));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {1, 2, 3};
        Integer[] arr3 = {1, 2};
        Integer[] arr4 = {1, 2, 3, 4};

        assertTrue(Equality.areArrayLengthsEqual(arr1, arr1));
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr3));
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr4));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {1, 2, 3};
        Integer[] arr3 = {1, 2, 4};
        String[] strArr1 = {"hello", "world"};
        String[] strArr2 = {"hello", "world"};
        String[] strArr3 = {"hello", "java"};

        assertTrue(Equality.areArrayElementsEqual(arr1, arr1));
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
        assertFalse(Equality.areArrayElementsEqual(arr1, arr3));

        assertTrue(Equality.areArrayElementsEqual(strArr1, strArr1));
        assertTrue(Equality.areArrayElementsEqual(strArr1, strArr2));
        assertFalse(Equality.areArrayElementsEqual(strArr1, strArr3));
    }

    @Test
    public void testIsArray() {
        Object obj = new Object();
        Integer[] arr = {1, 2, 3};
        String[] strArr = {"hello", "world"};

        assertFalse(Equality.isArray(obj));
        assertTrue(Equality.isArray(arr));
        assertTrue(Equality.isArray(strArr));
    }
    
}