package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, "test"));
        assertFalse(Equality.areEqual("test", null));
        assertTrue(Equality.areEqual("test", "test"));
        assertFalse(Equality.areEqual("test", "Test"));
        assertFalse(Equality.areEqual("test", "tset"));
        assertTrue(Equality.areEqual(1, 1));
        assertFalse(Equality.areEqual(1, 2));
    }

    @Test
    public void testAreArraysEqual() {
        Object[] array1 = {"test", 1};
        Object[] array2 = {"test", 1};
        assertTrue(Equality.areArraysEqual(array1, array2));

        Object[] array3 = {"test", 1};
        Object[] array4 = {"Test", 1};
        assertFalse(Equality.areArraysEqual(array3, array4));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Object[] array1 = {"test", 1};
        Object[] array2 = {"test", 1};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));

        Object[] array3 = {"test", 1};
        Object[] array4 = {"test"};
        assertFalse(Equality.areArrayLengthsEqual(array3, array4));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Object[] array1 = {"test", 1};
        Object[] array2 = {"test", 1};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));

        Object[] array3 = {"test", 1};
        Object[] array4 = {"Test", 1};
        assertFalse(Equality.areArrayElementsEqual(array3, array4));
    }

    @Test
    public void testIsArray() {
        Object[] array = {"test", 1};
        assertTrue(Equality.isArray(array));

        String str = "test";
        assertFalse(Equality.isArray(str));
    }
}