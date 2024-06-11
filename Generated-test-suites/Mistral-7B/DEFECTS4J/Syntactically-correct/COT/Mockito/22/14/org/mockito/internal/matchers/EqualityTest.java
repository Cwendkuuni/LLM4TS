package org.mockito.internal.matchers.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqualNullNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqualNullObject() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqualObjectNull() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqualObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Equality.areEqual(obj1, obj2));
        obj2 = new Object() {}; // different instance
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqualArraysSameObjects() {
        Object[] array1 = new Object[] {new Object()};
        Object[] array2 = new Object[] {new Object()};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqualArraysDifferentLengths() {
        Object[] array1 = new Object[] {new Object()};
        Object[] array2 = new Object[] {new Object(), new Object()};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test(expected = ArrayStoreException.class)
    public void testAreEqualArraysDifferentTypes() {
        Object[] array1 = new Object[] {new Object()};
        int[] array2 = new int[] {1, 2};
        Equality.areArraysEqual(array1, array2);
    }

    @Test
    public void testAreArraysEqualArraysEqual() {
        Object[] array1 = new Object[] {new Object()};
        Object[] array2 = new Object[] {new Object()};
        assertTrue(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqualArraysDifferentLengths() {
        Object[] array1 = new Object[] {new Object()};
        Object[] array2 = new Object[] {new Object(), new Object()};
        assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqualArraysEqualElements() {
        Object[] array1 = new Object[] {new Object(), new Object()};
        Object[] array2 = new Object[] {new Object(), new Object()};
        assertTrue(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqualArraysDifferentElements() {
        Object[] array1 = new Object[] {new Object(), new Object()};
        Object[] array2 = new Object[] {new Object(), new Object()};
        array2[0] = new Object() {}; // different instance
        assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testIsArray() {
        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
        Object[] array = new Object[0];
        assertTrue(Equality.isArray(array));
    }

    @Test
    public void testAreArrayLengthsEqualArrayLengthZero() {
        Object[] array1 = new Object[0];
        Object[] array2 = new Object[0];
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqualArrayLengthNotEqual() {
        Object[] array1 = new Object[1];
        Object[] array2 = new Object[2];
        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqualArrayElementsEqual() {
        Object[] array = new Object[] {new Object(), new Object()};
        for (int i = 0; i < array.length; i++) {
            Object obj1 = array[i];
            Object obj2 = array[i];
            assertTrue(Equality.areEqual(obj1, obj2));
        }
        assertTrue(Equality.areArraysEqual(array, array));
    }

    @Test
    public void testAreArrayElementsEqualArrayElementsNotEqual() {
        Object[] array = new Object[] {new Object(), new Object()};
        Object obj1 = array[0];
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
        assertFalse(Equality.areArraysEqual(array, new Object[] {obj1, obj2}));
    }
}