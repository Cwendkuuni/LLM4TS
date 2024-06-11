package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_NullInput1() {
        boolean result = Equality.areEqual(null, "Test");
        assertFalse(result);
    }

    @Test
    public void testAreEqual_NullInput2() {
        boolean result = Equality.areEqual("Test", null);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_BothNotNull_EqualObject() {
        boolean result = Equality.areEqual("Test", "Test");
        assertTrue(result);
    }

    @Test
    public void testAreEqual_BothNotNull_NotEqualObject() {
        boolean result = Equality.areEqual("Test1", "Test2");
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ArraysEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_ArraysNotEqual_LengthMismatch() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        boolean result = Equality.areEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ArraysNotEqual_ElementMismatch() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        boolean result = Equality.areEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreArraysEqual_ArraysEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areArraysEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreArraysEqual_ArraysNotEqual_LengthMismatch() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        boolean result = Equality.areArraysEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreArraysEqual_ArraysNotEqual_ElementMismatch() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        boolean result = Equality.areArraysEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreArrayLengthsEqual_LengthsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areArrayLengthsEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreArrayLengthsEqual_LengthsNotEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        boolean result = Equality.areArrayLengthsEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_ElementsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areArrayElementsEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreArrayElementsEqual_ElementsNotEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        boolean result = Equality.areArrayElementsEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testIsArray_IsArray() {
        int[] arr = {1, 2, 3};
        boolean result = Equality.isArray(arr);
        assertTrue(result);
    }

    @Test
    public void testIsArray_IsNotArray() {
        String str = "Test";
        boolean result = Equality.isArray(str);
        assertFalse(result);
    }
}