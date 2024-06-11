package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_OneNullInput_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_BothArrays_ReturnsTrue() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_OneArrayOneNonArray_ReturnsFalse() {
        int[] array = {1, 2, 3};
        Object nonArray = new Object();
        assertFalse(Equality.areEqual(array, nonArray));
    }

    @Test
    public void testAreEqual_SameObjects_ReturnsTrue() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_DifferentObjects_ReturnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreArraysEqual_LengthsNotEqual_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2};
        assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqual_ElementsNotEqual_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 4, 3};
        assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_LengthsEqual_ReturnsTrue() {
        int[] array1 = {1, 2};
        int[] array2 = {3, 4};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_LengthsNotEqual_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_AllElementsEqual_ReturnsTrue() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_NotAllElementsEqual_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 4, 3};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray_Array_ReturnsTrue() {
        int[] array = {1, 2, 3};
        assertTrue(Equality.isArray(array));
    }

    @Test
    public void testIsArray_NonArray_ReturnsFalse() {
        Object nonArray = new Object();
        assertFalse(Equality.isArray(nonArray));
    }
}