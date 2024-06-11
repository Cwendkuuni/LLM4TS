package org.mockito.internal.matchers;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import org.mockito.internal.matchers.Equality;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
        assertTrue(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreArraysEqual() {
        assertTrue(Equality.areArraysEqual(null, null));
        assertFalse(Equality.areArraysEqual(null, new Object()));
        assertFalse(Equality.areArraysEqual(new Object(), null));
        assertFalse(Equality.areArraysEqual(new Object(), new Object()));
        assertTrue(Equality.areArraysEqual(new Object[0], new Object[0]));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        assertTrue(Equality.areArrayLengthsEqual(null, null));
        assertFalse(Equality.areArrayLengthsEqual(null, new Object()));
        assertFalse(Equality.areArrayLengthsEqual(new Object(), null));
        assertFalse(Equality.areArrayLengthsEqual(new Object(), new Object()));
        assertTrue(Equality.areArrayLengthsEqual(new Object[0], new Object[0]));
    }

    @Test
    public void testAreArrayElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(null, null));
        assertFalse(Equality.areArrayElementsEqual(null, new Object()));
        assertFalse(Equality.areArrayElementsEqual(new Object(), null));
        assertFalse(Equality.areArrayElementsEqual(new Object(), new Object()));
        assertTrue(Equality.areArrayElementsEqual(new Object[0], new Object[0]));
    }

    @Test
    public void testIsArray() {
        assertFalse(Equality.isArray(null));
        assertFalse(Equality.isArray(new Object()));
        assertFalse(Equality.isArray(new String()));
        assertFalse(Equality.isArray(new Integer()));
        assertFalse(Equality.isArray(new Double()));
        assertTrue(Equality.isArray(new Object[0]));
        assertTrue(Equality.isArray(new String[0]));
        assertTrue(Equality.isArray(new Integer[0]));
        assertTrue(Equality.isArray(new Double[0]));
    }
}