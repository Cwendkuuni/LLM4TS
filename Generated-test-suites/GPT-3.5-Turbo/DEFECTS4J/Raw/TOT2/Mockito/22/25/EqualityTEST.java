import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_SameNonArrayObjects_ReturnsTrue() {
        assertTrue(Equality.areEqual("hello", "hello"));
    }

    @Test
    public void testAreEqual_DifferentNonArrayObjects_ReturnsFalse() {
        assertFalse(Equality.areEqual("hello", "world"));
    }

    @Test
    public void testAreEqual_SameArrayObjects_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_DifferentArrayObjects_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_DifferentLengthArrays_ReturnsFalse() {
        int[] arr1 = {1, 2};
        int[] arr2 = {1, 2, 3};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_SameLengthDifferentElementsArrays_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 4, 3};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_SameArrays_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_DifferentLengthArrays_ReturnsFalse() {
        int[] arr1 = {1, 2};
        int[] arr2 = {1, 2, 3};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_SameArrayElements_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_DifferentArrayElements_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 4, 3};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testIsArray_ArrayObject_ReturnsTrue() {
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArray_NonArrayObject_ReturnsFalse() {
        assertFalse(Equality.isArray("hello"));
    }
}