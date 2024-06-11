package org.mockito.internal.matchers;
import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_BothNull() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_AreArraysEqual() {
        assertTrue(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
    }

    @Test
    public void testAreArraysEqual_ArrayLengthsEqual() {
        assertTrue(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{4, 5, 6}));
    }

    @Test
    public void testAreArrayElementsEqual() {
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testAreArrayElementsEqual_ValidInput() {
        assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
    }

    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
    }

}