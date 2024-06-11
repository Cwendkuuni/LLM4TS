package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_nullObjects() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_equalObjects() {
        assertTrue(Equality.areEqual("test", "test"));
        assertTrue(Equality.areEqual(new Integer(5), new Integer(5)));
        assertTrue(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
    }

    @Test
    public void testAreArrayElementsEqual_differentArrayElement() {
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testAreArrayLengthsEqual_differentArrayLength() {
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
    }

    @Test
    public void testAreArraysEqual_sameArray() {
        int[] array = new int[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(array, array));
    }

    @Test
    public void testAreArraysEqual_differentArray() {
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testAreArraysEqual_nullArray() {
        assertFalse(Equality.areArraysEqual(null, new int[]{1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, null));
    }

    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
        assertFalse(Equality.isArray(new Object()));
    }
}
