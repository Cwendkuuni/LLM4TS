package org.mockito.internal.matchers;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_nullNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_nullObj1() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_obj1Null() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_obj1EqualObj2() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_obj1NotEqualObj2() {
        assertFalse(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreEqual_arrayEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_arrayNotEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {4, 5, 6};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_equalLength() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {4, 5, 6};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_notEqualLength() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_equalElements() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_notEqualElements() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 4, 3};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray_array() {
        int[] array = {1, 2, 3};
        assertTrue(Equality.isArray(array));
    }

    @Test
    public void testIsArray_notArray() {
        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
    }
}