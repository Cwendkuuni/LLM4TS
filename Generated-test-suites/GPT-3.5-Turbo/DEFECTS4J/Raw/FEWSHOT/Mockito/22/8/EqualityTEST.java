
package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullNull_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_NullObject_ReturnsFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_ObjectNull_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_EqualObjects_ReturnsTrue() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_DifferentObjects_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreArraysEqual_NullNull_ReturnsTrue() {
        assertTrue(Equality.areArraysEqual(null, null));
    }

    @Test
    public void testAreArraysEqual_NullArray_ReturnsFalse() {
        assertFalse(Equality.areArraysEqual(null, new Object[]{}));
    }

    @Test
    public void testAreArraysEqual_ArrayNull_ReturnsFalse() {
        assertFalse(Equality.areArraysEqual(new Object[]{}, null));
    }

    @Test
    public void testAreArraysEqual_EmptyArrays_ReturnsTrue() {
        assertTrue(Equality.areArraysEqual(new Object[]{}, new Object[]{}));
    }

    @Test
    public void testAreArraysEqual_EqualArrays_ReturnsTrue() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_DifferentLengthArrays_ReturnsFalse() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_DifferentElementsArrays_ReturnsFalse() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{3, 2, 1};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_NullNull_ReturnsTrue() {
        assertTrue(Equality.areArrayLengthsEqual(null, null));
    }

    @Test
    public void testAreArrayLengthsEqual_NullArray_ReturnsFalse() {
        assertFalse(Equality.areArrayLengthsEqual(null, new Object[]{}));
    }

    @Test
    public void testAreArrayLengthsEqual_ArrayNull_ReturnsFalse() {
        assertFalse(Equality.areArrayLengthsEqual(new Object[]{}, null));
    }

    @Test
    public void testAreArrayLengthsEqual_EqualLengthArrays_ReturnsTrue() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{4, 5, 6};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_DifferentLengthArrays_ReturnsFalse() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3, 4};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_NullNull_ReturnsTrue() {
        assertTrue(Equality.areArrayElementsEqual(null, null));
    }

    @Test
    public void testAreArrayElementsEqual_NullArray_ReturnsFalse() {
        assertFalse(Equality.areArrayElementsEqual(null, new Object[]{}));
    }

    @Test
    public void testAreArrayElementsEqual_ArrayNull_ReturnsFalse() {
        assertFalse(Equality.areArrayElementsEqual(new Object[]{}, null));
    }

    @Test
    public void testAreArrayElementsEqual_EqualElementsArrays_ReturnsTrue() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_DifferentElementsArrays_ReturnsFalse() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{3, 2, 1};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testIsArray_IsArray_ReturnsTrue() {
        assertTrue(Equality.isArray(new Object[1]));
    }

    @Test
    public void testIsArray_IsNotArray_ReturnsFalse() {
        assertFalse(Equality.isArray(new Object()));
    }
}
