package org.mockito.internal.matchers.test;

import org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Test equal objects
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Equality.areEqual(obj1, obj2));

        // Test null objects
        Object nullObj1 = null;
        Object nullObj2 = null;
        assertTrue(Equality.areEqual(nullObj1, nullObj2));

        // Test different types of objects
        int intObj1 = 1;
        String strObj1 = "one";
        assertFalse(Equality.areEqual(intObj1, strObj1));

        // Test arrays
        Object arr1 = new Object[]{1, 2, 3};
        Object arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));

        // Test arrays with different lengths
        Object arr3 = new Object[]{1, 2, 3};
        Object arr4 = new Object[]{1, 2, 3, 4};
        assertFalse(Equality.areEqual(arr3, arr4));

        // Test arrays with different elements
        Object arr5 = new Object[]{1, 2, 3};
        Object arr6 = new Object[]{1, 2, 4};
        assertFalse(Equality.areEqual(arr5, arr6));

        // Test arrays with null elements
        Object arr7 = new Object[]{1, null, 3};
        Object arr8 = new Object[]{1, 2, 3};
        assertFalse(Equality.areEqual(arr7, arr8));
    }

    @Test
    public void testAreArraysEqual() {
        // Test empty arrays
        Object[] arr1 = new Object[0];
        Object[] arr2 = new Object[0];
        assertTrue(Equality.areArraysEqual(arr1, arr2));

        // Test arrays with same length and elements
        Object[] arr3 = new Object[]{1, 2, 3};
        Object[] arr4 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr3, arr4));

        // Test arrays with different lengths
        Object[] arr5 = new Object[]{1, 2, 3};
        Object[] arr6 = new Object[]{1, 2, 3, 4};
        assertFalse(Equality.areArraysEqual(arr5, arr6));

        // Test arrays with same length but different elements
        Object[] arr7 = new Object[]{1, 2, 3};
        Object[] arr8 = new Object[]{1, 2, 4};
        assertFalse(Equality.areArraysEqual(arr7, arr8));

        // Test arrays with null elements
        Object[] arr9 = new Object[]{1, null, 3};
        Object[] arr10 = new Object[]{1, 2, 3};
        assertFalse(Equality.areArraysEqual(arr9, arr10));
    }

    @Test
    public void testHelperMethods() {
        // Test isArray
        assertFalse(Equality.isArray(new Object()));
        assertTrue(Equality.isArray(new Object[0]));
        assertTrue(Equality.isArray(new int[0]));

        // Test areArrayLengthsEqual
        Object[] arr1 = new Object[0];
        Object[] arr2 = new Object[0];
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));

        Object[] arr3 = new Object[3];
        Object[] arr4 = new Object[4];
        assertFalse(Equality.areArrayLengthsEqual(arr3, arr4));

        // Test areArrayElementsEqual
        Object[] arr5 = new Object[]{1, 2, 3};
        Object[] arr6 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr5, arr6));

        Object[] arr7 = new Object[]{1, 2, 3};
        Object[] arr8 = new Object[]{1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(arr7, arr8));
    }
}