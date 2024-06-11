package org.mockito.internal.matchers;

import org.junit.Test;

import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects_ReturnsTrueIfBothNull() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_NullObjects_ReturnsFalseIfOneNotNull() {
        boolean result = Equality.areEqual(null, new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_Arrays_ReturnsTrueIfArraysEqual() {
        Integer[] array1 = {1, 2, 3};
        Integer[] array2 = {1, 2, 3};
        boolean result = Equality.areEqual(array1, array2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_Arrays_ReturnsFalseIfArraysNotEqual() {
        Integer[] array1 = {1, 2, 3};
        Integer[] array2 = {1, 2, 4};
        boolean result = Equality.areEqual(array1, array2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_Objects_ReturnsTrueIfObjectsEqual() {
        String object1 = "hello";
        String object2 = "hello";
        boolean result = Equality.areEqual(object1, object2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_Objects_ReturnsFalseIfObjectsNotEqual() {
        String object1 = "hello";
        String object2 = "world";
        boolean result = Equality.areEqual(object1, object2);
        assertFalse(result);
    }
}
