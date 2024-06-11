package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_nullValues() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_arrayValues() {
        assertTrue(Equality.areEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areEqual(new int[] {1, 2, 3}, new int[] {3, 2, 1}));
    }

    @Test
    public void testAreEqual_otherValues() {
        assertTrue(Equality.areEqual("foo", "foo"));
        assertFalse(Equality.areEqual("foo", "bar"));
    }

    @Test
    public void testAreArraysEqual() {
        assertTrue(Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {3, 2, 1}));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        assertTrue(Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {1, 2}));
    }

    @Test
    public void testAreArrayElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {3, 2, 1}));
    }

    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new int[] {1, 2, 3}));
        assertFalse(Equality.isArray("foo"));
    }
}