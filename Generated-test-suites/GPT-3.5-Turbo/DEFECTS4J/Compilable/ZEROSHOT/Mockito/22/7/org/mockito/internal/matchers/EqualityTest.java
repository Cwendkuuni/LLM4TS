package org.mockito.internal.matchers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_bothNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_oneNull() {
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_notEqual() {
        assertFalse(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreEqual_equal() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreArraysEqual_bothNull() {
        assertTrue(Equality.areArraysEqual(null, null));
    }

    @Test
    public void testAreArraysEqual_oneNull() {
        assertFalse(Equality.areArraysEqual(null, new Object()));
        assertFalse(Equality.areArraysEqual(new Object(), null));
    }

    @Test
    public void testAreArraysEqual_notEqual() {
        assertFalse(Equality.areArraysEqual(new Object(), new Object()));
    }

    @Test
    public void testAreArraysEqual_equal() {
        Object[] array = {1, 2, 3};
        Object[] arrayCopy = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(array, arrayCopy));
    }

    @Test
    public void testAreArrayLengthsEqual_notEqual() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_equal() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {4, 5, 6};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }
    
    @Test
    public void testAreArrayElementsEqual_notEqual() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 3, 2};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_equal() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray_array() {
        Object[] array = {1, 2, 3};
        assertTrue(Equality.isArray(array));
    }

    @Test
    public void testIsArray_notArray() {
        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
    }

}