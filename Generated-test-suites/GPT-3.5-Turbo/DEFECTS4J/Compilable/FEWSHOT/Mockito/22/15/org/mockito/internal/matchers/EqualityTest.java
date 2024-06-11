
package org.mockito.internal.matchers;

import static org.junit.Assert.*;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
        assertTrue(Equality.areEqual("foo", "foo"));
        assertFalse(Equality.areEqual("foo", "bar"));
    }

    @Test
    public void testAreArraysEqual() {
        assertTrue(Equality.areArraysEqual(null, null));
        assertFalse(Equality.areArraysEqual(null, new Object()));
        assertFalse(Equality.areArraysEqual(new Object(), null));
        assertTrue(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{4, 5, 6}));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        assertTrue(Equality.areArrayLengthsEqual(null, null));
        assertFalse(Equality.areArrayLengthsEqual(null, new Object()));
        assertFalse(Equality.areArrayLengthsEqual(new Object(), null));
        assertTrue(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{4, 5, 6}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3, 4}));
    }

    @Test
    public void testAreArrayElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(null, null));
        assertFalse(Equality.areArrayElementsEqual(null, new Object()));
        assertFalse(Equality.areArrayElementsEqual(new Object(), null));
        assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testIsArray() {
        assertFalse(Equality.isArray(null));
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
    }
}
