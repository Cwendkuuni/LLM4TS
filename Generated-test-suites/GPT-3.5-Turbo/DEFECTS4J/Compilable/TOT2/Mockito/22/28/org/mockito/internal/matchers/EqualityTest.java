package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_nullInputs() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_equalObjects() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreArraysEqual_equalArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3, 4};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testIsArray_arrayInput() {
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArray_nonArrayInput() {
        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
    }

}