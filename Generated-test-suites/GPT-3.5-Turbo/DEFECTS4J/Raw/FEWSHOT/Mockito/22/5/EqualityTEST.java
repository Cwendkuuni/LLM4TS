
package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertEquals(true, Equality.areEqual(null, null));
        assertEquals(false, Equality.areEqual(null, ""));
        assertEquals(false, Equality.areEqual("", null));
        assertEquals(true, Equality.areEqual("", ""));
        assertEquals(false, Equality.areEqual("foo", "bar"));
        assertEquals(true, Equality.areEqual("foo", "foo"));
        assertEquals(true, Equality.areEqual(1, 1));
        assertEquals(false, Equality.areEqual(1, 2));
        assertEquals(false, Equality.areEqual(null, new Object()));
        assertEquals(false, Equality.areEqual(new Object(), null));
        assertEquals(true, Equality.areEqual(new Object(), new Object()));
        assertEquals(true, Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertEquals(false, Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertEquals(false, Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
        assertEquals(false, Equality.areEqual(new int[]{1, 2, 3}, new int[0]));
        assertEquals(false, Equality.areEqual(new int[]{1, 2, 3}, new Object()));
        assertEquals(false, Equality.areEqual(new int[]{1, 2, 3}, null));
    }

    @Test
    public void testAreArraysEqual() {
        assertEquals(true, Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertEquals(false, Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertEquals(false, Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
        assertEquals(false, Equality.areArraysEqual(new int[]{1, 2, 3}, new int[0]));
        assertEquals(false, Equality.areArraysEqual(null, new int[0]));
        assertEquals(false, Equality.areArraysEqual(new int[]{1, 2, 3}, new Object()));
        assertEquals(false, Equality.areArraysEqual(new int[]{1, 2, 3}, null));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        assertEquals(true, Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertEquals(false, Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertEquals(false, Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
        assertEquals(false, Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[0]));
        assertEquals(false, Equality.areArrayLengthsEqual(null, new int[0]));
        assertEquals(false, Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new Object()));
        assertEquals(false, Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, null));
    }

    @Test
    public void testAreArrayElementsEqual() {
        assertEquals(true, Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertEquals(false, Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertEquals(false, Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
        assertEquals(false, Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[0]));
        assertEquals(false, Equality.areArrayElementsEqual(null, new int[0]));
        assertEquals(false, Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new Object()));
        assertEquals(false, Equality.areArrayElementsEqual(new int[]{1, 2, 3}, null));
    }

    @Test
    public void testIsArray() {
        assertEquals(true, Equality.isArray(new int[]{1, 2, 3}));
        assertEquals(false, Equality.isArray(1));
        assertEquals(false, Equality.isArray("foo"));
    }
}
