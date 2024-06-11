package org.mockito.internal.matchers;
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs_ReturnsTrue() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_FirstInputIsNull_ReturnsFalse() {
        boolean result = Equality.areEqual(null, "test");
        assertFalse(result);
    }

    @Test
    public void testAreEqual_SecondInputIsNull_ReturnsFalse() {
        boolean result = Equality.areEqual("test", null);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_BothInputsAreArrays_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_OnlyOneInputIsArray_ReturnsFalse() {
        int[] arr = {1, 2, 3};
        Object obj = new Object();
        boolean result = Equality.areEqual(arr, obj);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_NonArrayInputs_ReturnsTrue() {
        boolean result = Equality.areEqual("test", "test");
        assertTrue(result);
    }

    @Test
    public void testAreArrayLengthsEqual_ArrayLengthsAreEqual_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};
        boolean result = Equality.areArrayLengthsEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreArrayLengthsEqual_ArrayLengthsAreNotEqual_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5};
        boolean result = Equality.areArrayLengthsEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_ArrayElementsAreEqual_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areArrayElementsEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreArrayElementsEqual_ArrayElementsAreNotEqual_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};
        boolean result = Equality.areArrayElementsEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_ArraysHaveDifferentLengths_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5};
        boolean result = Equality.areArrayElementsEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testIsArray_InputIsArray_ReturnsTrue() {
        int[] arr = {1, 2, 3};
        boolean result = Equality.isArray(arr);
        assertTrue(result);
    }

    @Test
    public void testIsArray_InputIsNotArray_ReturnsFalse() {
        Object obj = new Object();
        boolean result = Equality.isArray(obj);
        assertFalse(result);
    }
}