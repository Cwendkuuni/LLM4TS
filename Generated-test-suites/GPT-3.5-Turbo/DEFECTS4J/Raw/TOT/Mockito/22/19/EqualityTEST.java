import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_typicalCase() {
        // Typical case: Two equal objects
        String s1 = "hello";
        String s2 = "hello";
        assertTrue(Equality.areEqual(s1, s2));
    }

    @Test
    public void testAreEqual_typicalCase_2() {
        // Typical case: Two different objects
        String s1 = "hello";
        String s2 = "world";
        assertFalse(Equality.areEqual(s1, s2));
    }

    @Test
    public void testAreEqual_nullObjects() {
        // Edge case: Both objects are null
        Object o1 = null;
        Object o2 = null;
        assertTrue(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_oneNullObject() {
        // Edge case: One object is null and the other is not
        Object o1 = null;
        String s2 = "hello";
        assertFalse(Equality.areEqual(o1, s2));
    }

    @Test
    public void testAreEqual_nonArrayObjects() {
        // Typical case: Two non-array objects
        Integer i1 = 10;
        Integer i2 = 10;
        assertTrue(Equality.areEqual(i1, i2));
    }

    @Test
    public void testAreArraysEqual_sameArrays() {
        // Typical case: Two equal arrays
        int[] a1 = {1, 2, 3};
        int[] a2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(a1, a2));
    }

    @Test
    public void testAreArraysEqual_differentArrays() {
        // Typical case: Two different arrays
        int[] a1 = {1, 2, 3};
        int[] a2 = {3, 2, 1};
        assertFalse(Equality.areArraysEqual(a1, a2));
    }

    @Test
    public void testAreArrayLengthsEqual_sameLength() {
        // Typical case: Two arrays with same length
        String[] arr1 = {"hello", "world"};
        String[] arr2 = {"foo", "bar"};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_differentLength() {
        // Typical case: Two arrays with different length
        String[] arr1 = {"hello", "world"};
        String[] arr2 = {"foo"};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_allElementsEqual() {
        // Typical case: All elements in the arrays are equal
        int[] a1 = {1, 2, 3};
        int[] a2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(a1, a2));
    }

    @Test
    public void testAreArrayElementsEqual_notAllElementsEqual() {
        // Typical case: Not all elements in the arrays are equal
        int[] a1 = {1, 2, 3};
        int[] a2 = {4, 5, 6};
        assertFalse(Equality.areArrayElementsEqual(a1, a2));
    }

    @Test
    public void testIsArray_arrayObject() {
        // Typical case: Object is an array
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArray_nonArrayObject() {
        // Typical case: Object is not an array
        String str = "hello";
        assertFalse(Equality.isArray(str));
    }
}