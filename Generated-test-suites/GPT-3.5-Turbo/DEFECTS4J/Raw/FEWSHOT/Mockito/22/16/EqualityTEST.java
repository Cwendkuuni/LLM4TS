package org.mockito.internal.matchers;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import org.junit.Test;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(null, null));
        assertTrue(Equality.areEqual(1, 1));
        assertTrue(Equality.areEqual("foo", "foo"));

        assertFalse(Equality.areEqual(null, 1));
        assertFalse(Equality.areEqual(1, null));
        assertFalse(Equality.areEqual("foo", "bar"));
    }

    @Test
    public void testAreArraysEqual() {
        assertTrue(Equality.areArraysEqual(null, null));
        assertTrue(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));

        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertFalse(Equality.areArraysEqual(null, new int[]{1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, null));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        assertTrue(Equality.areArrayLengthsEqual(null, null));
        assertTrue(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{4, 5, 6}));

        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
        assertFalse(Equality.areArrayLengthsEqual(null, new int[]{1, 2, 3}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, null));
    }

    @Test
    public void testAreArrayElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(null, null));
        assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));

        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertFalse(Equality.areArrayElementsEqual(null, new int[]{1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, null));
    }

    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(null));
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
        assertFalse(Equality.isArray(1));
        assertFalse(Equality.isArray("foo"));
    }
}