package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_sameObject_true() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_bothNull_true() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_oneNull_false() {
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_differentClasses_false() {
        assertFalse(Equality.areEqual(new Object(), new String()));
    }

    @Test
    public void testAreEqual_sameArray_true() {
        Object[] arr1 = new Object[]{"test", 1, true};
        Object[] arr2 = arr1;
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_differentArrays_false() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_arrayWithDifferentLengths_false() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_arrayWithDifferentElements_false() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_differentObjects_true() {
        assertFalse(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreArraysEqual_sameArray_true() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = arr1;
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_differentArrays_false() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_sameLength_true() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_differentLengths_false() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3, 4};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_sameElements_true() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_differentElements_false() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testIsArray_array_true() {
        assertTrue(Equality.isArray(new Object[]{1, 2, 3}));
    }

    @Test
    public void testIsArray_nonArray_false() {
        assertFalse(Equality.isArray(new Object()));
    }
}