package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullArguments_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_OneArgumentIsNull_ReturnsFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_BothArgumentsAreArrays_ReturnsTrue() {
        assertTrue(Equality.areEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3}));
    }

    @Test
    public void testAreEqual_OneArgumentIsArrayAndOtherIsNot_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object[]{1, 2, 3}, new Object()));
    }

    @Test
    public void testAreEqual_BothArgumentsAreEqual_ReturnsTrue() {
        assertTrue(Equality.areEqual("abc", "abc"));
    }

    @Test
    public void testAreEqual_BothArgumentsAreNotEqual_ReturnsFalse() {
        assertFalse(Equality.areEqual("abc", "def"));
    }

    @Test
    public void testAreArraysEqual_LengthsAreEqual_ElementsAreEqual_ReturnsTrue() {
        assertTrue(Equality.areArraysEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3}));
    }

    @Test
    public void testAreArraysEqual_LengthsAreEqual_ElementsAreNotEqual_ReturnsFalse() {
        assertFalse(Equality.areArraysEqual(new Object[]{1, 2, 3}, new Object[]{4, 5, 6}));
    }

    @Test
    public void testAreArrayLengthsEqual_LengthsAreEqual_ReturnsTrue() {
        assertTrue(Equality.areArrayLengthsEqual(new Object[]{1, 2, 3}, new Object[]{4, 5, 6}));
    }

    @Test
    public void testAreArrayLengthsEqual_LengthsAreNotEqual_ReturnsFalse() {
        assertFalse(Equality.areArrayLengthsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2}));
    }

    @Test
    public void testAreArrayElementsEqual_AllElementsAreEqual_ReturnsTrue() {
        assertTrue(Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3}));
    }

    @Test
    public void testAreArrayElementsEqual_AtLeastOneElementIsNotEqual_ReturnsFalse() {
        assertFalse(Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 4}));
    }

    @Test
    public void testIsArray_IsArray_ReturnsTrue() {
        assertTrue(Equality.isArray(new Object[]{1, 2, 3}));
    }

    @Test
    public void testIsArray_IsNotArray_ReturnsFalse() {
        assertFalse(Equality.isArray(new Object()));
    }
}