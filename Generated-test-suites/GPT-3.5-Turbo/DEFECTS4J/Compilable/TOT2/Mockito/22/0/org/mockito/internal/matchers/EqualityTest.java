package org.mockito.internal.matchers;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_OneObjectNull() {
        assertFalse(Equality.areEqual(null, "test"));
    }

    @Test
    public void testAreEqual_ArraysEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_EqualArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_EqualArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_ArraysWithDifferentLength() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_ArraysWithDifferentElements() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_DifferentObjects() {
        assertFalse(Equality.areEqual("hello", "world"));
    }

    @Test
    public void testAreEqual_SameObjects() {
        String str = "hello";
        assertTrue(Equality.areEqual(str, str));
    }
}