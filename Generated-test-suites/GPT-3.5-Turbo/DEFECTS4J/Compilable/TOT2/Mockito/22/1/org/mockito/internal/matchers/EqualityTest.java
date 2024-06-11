package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_BothNull_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_BothNotNull_ReturnsTrue() {
        assertTrue(Equality.areEqual(1, 1));
    }

    @Test
    public void testAreEqual_ArraysEqual_ReturnsTrue() {
        assertTrue(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
    }

    @Test
    public void testAreEqual_ArraysNotEqual_ReturnsFalse() {
        assertFalse(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testAreArraysEqual_SameLengths_ReturnsTrue() {
        assertTrue(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{4, 5, 6}));
    }

    @Test
    public void testAreArraysEqual_DifferentLengths_ReturnsFalse() {
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{4, 5}));
    }

    @Test
    public void testAreArrayElementsEqual_AllElementsEqual_ReturnsTrue() {
        assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
    }

    @Test
    public void testAreArrayElementsEqual_NotAllElementsEqual_ReturnsFalse() {
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 5, 3}));
    }

    @Test
    public void testIsArray_IsArray_ReturnsTrue() {
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
    }

    @Test
    public void testIsArray_IsNotArray_ReturnsFalse() {
        assertFalse(Equality.isArray(1));
    }
}