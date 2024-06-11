import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_EqualObjects() {
        assertTrue(Equality.areEqual("hello", "hello"));
    }

    @Test
    public void testAreEqual_DifferentObjects() {
        assertFalse(Equality.areEqual("hello", "world"));
    }

    @Test
    public void testAreArraysEqual_EqualArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_DifferentArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_SameLengthArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_DifferentLengthArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_SameElements() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_DifferentElements() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testIsArray_IsArray() {
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArray_NotArray() {
        assertFalse(Equality.isArray("hello"));
    }
}