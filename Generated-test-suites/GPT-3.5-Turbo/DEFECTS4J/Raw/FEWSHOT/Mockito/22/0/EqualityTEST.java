
package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_BothNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_OneNull() {
        assertFalse(Equality.areEqual(null, "foo"));
        assertFalse(Equality.areEqual("foo", null));
    }

    @Test
    public void testAreEqual_BothNotArrays() {
        assertTrue(Equality.areEqual("foo", "foo"));
        assertFalse(Equality.areEqual("foo", "bar"));
    }

    @Test
    public void testAreEqual_ArraysEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_ArraysNotEqual() {
        int[] array1 = {1, 2};
        int[] array2 = {1, 2, 3};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_EqualLength() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {4, 5, 6};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_NotEqualLength() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_AllEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_NotAllEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 4, 3};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray_NotArray() {
        assertFalse(Equality.isArray("foo"));
    }

    @Test
    public void testIsArray_IsArray() {
        int[] array = {1, 2, 3};
        assertTrue(Equality.isArray(array));
    }
}
