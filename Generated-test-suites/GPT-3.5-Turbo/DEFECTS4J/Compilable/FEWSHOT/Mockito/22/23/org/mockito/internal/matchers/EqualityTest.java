
package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_NullObjectAndNonNullObject_ReturnsFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_NonNullObjectAndNullObject_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_SameNonArrayObjects_ReturnsTrue() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_SameArrayObjects_ReturnsTrue() {
        String[] arr = { "a", "b", "c" };
        assertTrue(Equality.areEqual(arr, arr));
    }

    @Test
    public void testAreEqual_DifferentNonArrayObjects_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreEqual_DifferentArrayObjects_ReturnsFalse() {
        String[] arr1 = { "a", "b", "c" };
        String[] arr2 = { "x", "y", "z" };
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_NullArrays_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_NullArrayAndNonNullArray_ReturnsFalse() {
        assertFalse(Equality.areEqual(null, new String[] { "a", "b", "c" }));
    }

    @Test
    public void testAreEqual_NonNullArrayAndNullArray_ReturnsFalse() {
        assertFalse(Equality.areEqual(new String[] { "a", "b", "c" }, null));
    }

    @Test
    public void testAreEqual_SameArrays_ReturnsTrue() {
        String[] arr = { "a", "b", "c" };
        assertTrue(Equality.areEqual(arr, arr));
    }

    @Test
    public void testAreEqual_DifferentArrays_ReturnsFalse() {
        String[] arr1 = { "a", "b", "c" };
        String[] arr2 = { "x", "y", "z" };
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_NullArrays_ReturnsTrue() {
        assertTrue(Equality.areArrayLengthsEqual(null, null));
    }

    @Test
    public void testAreArrayLengthsEqual_NullArrayAndNonNullArray_ReturnsFalse() {
        assertFalse(Equality.areArrayLengthsEqual(null, new String[] { "a", "b", "c" }));
    }

    @Test
    public void testAreArrayLengthsEqual_NonNullArrayAndNullArray_ReturnsFalse() {
        assertFalse(Equality.areArrayLengthsEqual(new String[] { "a", "b", "c" }, null));
    }

    @Test
    public void testAreArrayLengthsEqual_SameLengthArrays_ReturnsTrue() {
        String[] arr1 = { "a", "b", "c" };
        String[] arr2 = { "x", "y", "z" };
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_DifferentLengthArrays_ReturnsFalse() {
        String[] arr1 = { "a", "b", "c" };
        String[] arr2 = { "x", "y" };
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_SameArrays_ReturnsTrue() {
        String[] arr1 = { "a", "b", "c" };
        String[] arr2 = { "a", "b", "c" };
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_DifferentArrays_ReturnsFalse() {
        String[] arr1 = { "a", "b", "c" };
        String[] arr2 = { "x", "y", "z" };
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_NullArrays_ReturnsTrue() {
        assertTrue(Equality.areArrayElementsEqual(null, null));
    }

    @Test
    public void testAreArrayElementsEqual_NullArrayAndNonNullArray_ReturnsFalse() {
        assertFalse(Equality.areArrayElementsEqual(null, new String[] { "a", "b", "c" }));
    }

    @Test
    public void testAreArrayElementsEqual_NonNullArrayAndNullArray_ReturnsFalse() {
        assertFalse(Equality.areArrayElementsEqual(new String[] { "a", "b", "c" }, null));
    }

    @Test
    public void testAreArrayElementsEqual_SameLengthButDifferentElements_ReturnsFalse() {
        String[] arr1 = { "a", "b", "c" };
        String[] arr2 = { "x", "y", "z" };
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_SameLengthAndSameElements_ReturnsTrue() {
        String[] arr1 = { "a", "b", "c" };
        String[] arr2 = { "a", "b", "c" };
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testIsArray_NullObject_ReturnsFalse() {
        assertFalse(Equality.isArray(null));
    }

    @Test
    public void testIsArray_NonArrayObject_ReturnsFalse() {
        assertFalse(Equality.isArray(new Object()));
    }

    @Test
    public void testIsArray_ArrayObject_ReturnsTrue() {
        assertTrue(Equality.isArray(new String[] { "a", "b", "c" }));
    }
}
