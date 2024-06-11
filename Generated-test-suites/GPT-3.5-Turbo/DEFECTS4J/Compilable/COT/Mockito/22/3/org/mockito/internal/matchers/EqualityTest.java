package org.mockito.internal.matchers;

import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_objectsAreEqual() {
        assertTrue(Equality.areEqual("abc", "abc"));
    }

    @Test
    public void testAreEqual_objectsAreNotEqual() {
        assertFalse(Equality.areEqual("abc", "def"));
    }

    @Test
    public void testAreEqual_objectIsNull() {
        assertFalse(Equality.areEqual(null, "abc"));
    }

    @Test
    public void testAreEqual_objectsBothNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_objectsAreArray() {
        assertTrue(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
    }

    @Test
    public void testAreEqual_objectsAreArray_differentLengths() {
        assertFalse(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
    }

    @Test
    public void testAreEqual_objectsAreArray_differentElements() {
        assertFalse(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testArrayLengthsEqual_equalLengths() {
        assertTrue(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{4, 5, 6}));
    }

    @Test
    public void testArrayLengthsEqual_differentLengths() {
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{4, 5}));
    }

    @Test
    public void testArrayElementsEqual_allElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
    }

    @Test
    public void testArrayElementsEqual_notAllElementsEqual() {
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testIsArray_isArray() {
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
    }

    @Test
    public void testIsArray_isNotArray() {
        assertFalse(Equality.isArray("abc"));
    }

}