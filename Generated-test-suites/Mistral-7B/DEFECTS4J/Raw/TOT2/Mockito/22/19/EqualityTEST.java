
package org.mockito.internal.matchers.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    // Expert 1's test cases
    @Test
    public void testAreEqual_sameObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    // Expert 1's test cases
    @Test
    public void testAreEqual_nullObjects() {
        Object obj1 = null;
        Object obj2 = null;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    // Expert 1's test cases
    @Test
    public void testAreEqual_differentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    // Expert 1's test cases
    @Test
    public void testAreEqual_nullAndObject() {
        Object obj1 = null;
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    // Expert 1's test cases
    @Test
    public void testAreEqual_ObjectAndArray() {
        Object obj1 = new Object();
        Object[] arr2 = new Object[0];
        assertFalse(Equality.areEqual(obj1, arr2));
    }

    // Expert 1's test cases
    @Test(expected = IllegalArgumentException.class)
    public void testAreEqual_ArrayAndObject() {
        Object[] arr1 = new Object[0];
        Object obj2 = new Object();
        Equality.areEqual(arr1, obj2);
    }

    // Expert 1's test cases
    @Test
    public void testAreArraysEqual_sameArrays() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    // Expert 1's test cases
    @Test
    public void testAreArraysEqual_nullArrays() {
        Object[] arr1 = null;
        Object[] arr2 = null;
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    // Expert 1's test cases
    @Test
    public void testAreArraysEqual_differentLengthArrays() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    // Expert 1's test cases
    @Test
    public void testAreArraysEqual_differentElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    // Expert 1's test cases
    @Test
    public void testAreArraysEqual_nullAndArray() {
        Object[] arr1 = null;
        Object[] arr2 = new Object[]{1, 2, 3};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    // Expert 1's test cases
    @Test(expected = IllegalArgumentException.class)
    public void testAreArraysEqual_ArrayAndNull() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = null;
        Equality.areArraysEqual(arr1, arr2);
    }

    // Expert 1's test cases
    @Test
    public void testAreArrayLengthsEqual_sameLengthArrays() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    // Expert 1's test cases
    @Test
    public void testAreArrayLengthsEqual_differentLengthArrays() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    // Expert 1's test cases
    @Test
    public void testAreArrayElementsEqual_sameElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    // Expert 1's test cases
    @Test
    public void testAreArrayElementsEqual_differentElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    // Expert 1's test cases
    @Test
    public void testIsArray_array() {
        Object[] arr = new Object[0];
        assertTrue(Equality.isArray(arr));
    }

    // Expert 1's test cases
    @Test
    public void testIsArray_nonArray() {
        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
    }

    // Expert 2's test cases (to be added)
    // Expert 3's test cases (to be added)
}
