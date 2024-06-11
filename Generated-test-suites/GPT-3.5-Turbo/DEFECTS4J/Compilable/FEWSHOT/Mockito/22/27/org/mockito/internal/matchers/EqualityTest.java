package org.mockito.internal.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, ""));
        assertFalse(Equality.areEqual("", null));
        assertTrue(Equality.areEqual("", ""));
        assertTrue(Equality.areEqual("foo", "foo"));
        assertFalse(Equality.areEqual("foo", "bar"));
        assertFalse(Equality.areEqual("foo", null));
        assertFalse(Equality.areEqual(null, "foo"));
    }

    @Test
    public void testAreArraysEqual() {
        assertTrue(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new boolean[]{true, false, true}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, null));
        assertFalse(Equality.areArraysEqual(null, new int[]{1, 2, 3}));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        assertTrue(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3, 4}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new boolean[]{true, false, true}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, null));
        assertFalse(Equality.areArrayLengthsEqual(null, new int[]{1, 2, 3}));
    }

    @Test
    public void testAreArrayElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new boolean[]{true, false, true}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, null));
        assertFalse(Equality.areArrayElementsEqual(null, new int[]{1, 2, 3}));
    }

    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
        assertFalse(Equality.isArray("foo"));
    }
}