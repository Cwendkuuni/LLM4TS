package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual("hello", "hello"));
        assertFalse(Equality.areEqual("hello", "world"));
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual("hello", null));
    }

    @Test
    public void testAreArraysEqual() {
        assertTrue(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{3, 2, 1}));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        assertTrue(Equality.areArrayLengthsEqual(new int[3], new int[3]));
        assertFalse(Equality.areArrayLengthsEqual(new int[3], new int[4]));
    }

    @Test
    public void testAreArrayElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{3, 2, 1}));
    }

    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
        assertFalse(Equality.isArray("hello"));
    }
}