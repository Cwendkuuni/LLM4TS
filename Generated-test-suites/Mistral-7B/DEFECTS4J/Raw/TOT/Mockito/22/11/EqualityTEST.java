import org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {
    @Test
    public void testAreEqualNull() {
        // null equals null
        assertTrue(Equality.areEqual(null, null));
    }

    // Test cases for Equality.areEqual method
    @Test
    public void testAreEqualObjects() {
        String str1 = "Hello";
        String str2 = "Hello";
        // same object
        assertTrue(Equality.areEqual(str1, str1));
        // two equal objects
        assertTrue(Equality.areEqual(str1, str2));
    }

    @Test
    public void testAreEqualArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        // two equal arrays
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqualArraysDifferentLengths() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        // arrays of different lengths are not equal
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqualArraysDifferentElements() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        // arrays with different elements are not equal
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqualArraysNullAndObject() {
        int[] arr = {1, 2, 3};
        Object obj = "not an array";
        // array is not equal to null or an object
        assertFalse(Equality.areEqual(arr, null));
        assertFalse(Equality.areEqual(arr, obj));
    }

    @Test
    public void testAreEqualArraysNullAndArray() {
        Object obj = null;
        int[] arr = {1, 2, 3};
        // null is not equal to an array
        assertFalse(Equality.areEqual(obj, arr));
    }

    // Test cases for Equality.areArraysEqual method
    @Test
    public void testAreArraysEqualNullAndObject() {
        Object obj1 = null;
        Object obj2 = null;
        // null arrays are equal to each other
        assertTrue(Equality.areArraysEqual(obj1, obj2));
    }

    @Test
    public void testAreArraysEqualObjects() {
        Object[] obj1 = new Object[0];
        Object[] obj2 = new Object[0];
        // empty arrays are equal to each other
        assertTrue(Equality.areArraysEqual(obj1, obj2));
    }

    @Test
    public void testAreArraysEqualArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        // two equal arrays are equal to each other
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqualArraysDifferentLengths() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        // arrays of different lengths are not equal to each other
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqualArraysDifferentElements() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        // arrays with different elements are not equal to each other
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }
}