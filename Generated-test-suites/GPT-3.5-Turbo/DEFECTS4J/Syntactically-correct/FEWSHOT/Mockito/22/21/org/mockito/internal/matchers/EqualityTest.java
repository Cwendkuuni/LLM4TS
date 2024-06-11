import static org.junit.Assert.*;

import org.junit.Test;

public class EqualityTest {
    @Test
    public void testAreEqual_identicalObjects_returnTrue() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_nullObjects_returnTrue() {
        Object obj1 = null;
        Object obj2 = null;

        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_nullObjectAndNonNullObject_returnFalse() {
        Object obj1 = null;
        Object obj2 = new Object();

        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_nonNullObjectAndNullObject_returnFalse() {
        Object obj1 = new Object();
        Object obj2 = null;

        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_equalArrays_returnTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_differentArrays_returnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};

        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_equalStrings_returnTrue() {
        String str1 = "hello";
        String str2 = "hello";

        assertTrue(Equality.areEqual(str1, str2));
    }

    @Test
    public void testAreEqual_differentStrings_returnFalse() {
        String str1 = "hello";
        String str2 = "world";

        assertFalse(Equality.areEqual(str1, str2));
    }

    @Test
    public void testAreArraysEqual_equalArrays_returnTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_differentArrays_returnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};

        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_equalArrays_returnTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};

        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_differentArrays_returnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6, 7};

        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_equalArrays_returnTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_differentArrays_returnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};

        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testIsArray_isArray_returnTrue() {
        int[] arr = {1, 2, 3};

        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArray_isNotArray_returnFalse() {
        Object obj = new Object();

        assertFalse(Equality.isArray(obj));
    }
}