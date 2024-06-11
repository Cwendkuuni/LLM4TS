package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual("foo", "foo"));
        assertFalse(Equality.areEqual("foo", "bar"));
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual("foo", null));
        assertFalse(Equality.areEqual(null, "bar"));
    }

    @Test
    public void testAreArraysEqual() {
        Object[] array1 = {"foo", "bar"};
        Object[] array2 = {"foo", "bar"};
        assertTrue(Equality.areArraysEqual(array1, array2));

        Object[] array3 = {"foo", "bar"};
        Object[] array4 = {"bar", "foo"};
        assertFalse(Equality.areArraysEqual(array3, array4));

        Object[] array5 = {"foo", "bar"};
        Object[] array6 = {"foo"};
        assertFalse(Equality.areArraysEqual(array5, array6));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Object[] array1 = {"foo", "bar"};
        Object[] array2 = {"foo", "bar"};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));

        Object[] array3 = {"foo", "bar"};
        Object[] array4 = {"foo"};
        assertFalse(Equality.areArrayLengthsEqual(array3, array4));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Object[] array1 = {"foo", "bar"};
        Object[] array2 = {"foo", "bar"};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));

        Object[] array3 = {"foo", "bar"};
        Object[] array4 = {"bar", "foo"};
        assertFalse(Equality.areArrayElementsEqual(array3, array4));
    }

    @Test
    public void testIsArray() {
        Object[] array1 = {"foo", "bar"};
        assertTrue(Equality.isArray(array1));

        String str = "foo";
        assertFalse(Equality.isArray(str));
    }
}