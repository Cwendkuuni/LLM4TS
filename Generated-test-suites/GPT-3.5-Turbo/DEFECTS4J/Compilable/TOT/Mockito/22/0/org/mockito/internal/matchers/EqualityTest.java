package org.mockito.internal.matchers;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

import static org.junit.Assert.assertEquals;

public class EqualityTest {

    @Test
    public void testAreEqual_whenBothObjectsNull_returnsTrue() {
        boolean result = Equality.areEqual(null, null);
        assertEquals(true, result);
    }

    @Test
    public void testAreEqual_whenOneObjectNull_returnsFalse() {
        boolean result = Equality.areEqual(null, new Object());
        assertEquals(false, result);
    }

    @Test
    public void testAreEqual_whenBothObjectsNotNullAndEqual_returnsTrue() {
        boolean result = Equality.areEqual("Hello", "Hello");
        assertEquals(true, result);
    }

    @Test
    public void testAreEqual_whenBothObjectsNotNullAndNotEqual_returnsFalse() {
        boolean result = Equality.areEqual("Hello", "World");
        assertEquals(false, result);
    }

    @Test
    public void testAreEqual_whenOneObjectIsArrayAndOtherIsNot_returnsFalse() {
        Object array = new Object[] {1, 2, 3};
        boolean result = Equality.areEqual(array, "Hello");
        assertEquals(false, result);
    }

    @Test
    public void testAreEqual_whenBothObjectsAreArraysAndEqual_returnsTrue() {
        Object array1 = new Object[] {1, 2, 3};
        Object array2 = new Object[] {1, 2, 3};
        boolean result = Equality.areEqual(array1, array2);
        assertEquals(true, result);
    }

    @Test
    public void testAreEqual_whenBothObjectsAreArraysAndNotEqual_returnsFalse() {
        Object array1 = new Object[] {1, 2, 3};
        Object array2 = new Object[] {4, 5, 6};
        boolean result = Equality.areEqual(array1, array2);
        assertEquals(false, result);
    }

    @Test
    public void testAreEqual_whenArraysHaveDifferentLengths_returnsFalse() {
        Object array1 = new Object[] {1, 2, 3};
        Object array2 = new Object[] {1, 2};
        boolean result = Equality.areEqual(array1, array2);
        assertEquals(false, result);
    }

    @Test
    public void testAreEqual_whenArrayElementsNotEqual_returnsFalse() {
        Object array1 = new Object[] {1, 2, 3};
        Object array2 = new Object[] {1, 2, 4};
        boolean result = Equality.areEqual(array1, array2);
        assertEquals(false, result);
    }

    @Test
    public void testAreEqual_whenObjectIsArray_returnsTrue() {
        Object array = new Object[] {1, 2, 3};
        boolean result = Equality.isArray(array);
        assertEquals(true, result);
    }

    @Test
    public void testAreEqual_whenObjectIsNotArray_returnsFalse() {
        Object object = "Hello";
        boolean result = Equality.isArray(object);
        assertEquals(false, result);
    }
}