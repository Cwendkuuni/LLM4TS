package org.mockito.internal.matchers;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;
        Object obj4 = new Object[]{"a", "b"};
        Object obj5 = new Object[]{obj1, obj2};
        Object obj6 = new Object[]{obj1, obj2};

        // null check
        assertFalse(Equality.areEqual(null, obj1));
        assertFalse(Equality.areEqual(obj1, null));

        // primitive types
        assertTrue(Equality.areEqual(1, 1));
        assertFalse(Equality.areEqual(1, 2));

        // same object
        assertTrue(Equality.areEqual(obj1, obj3));

        // different objects, but equal by value
        Object str1 = new String("foo");
        Object str2 = new String("foo");
        assertTrue(Equality.areEqual(str1, str2));

        // different arrays of the same type and length with equal elements
        Object[] arr1 = new Object[]{1, 2};
        Object[] arr2 = new Object[]{1, 2};
        assertTrue(Equality.areEqual(arr1, arr2));

        // different arrays of the same type and length with non-equal elements
        Object[] arr3 = new Object[]{1, 2};
        Object[] arr4 = new Object[]{1, 3};
        assertFalse(Equality.areEqual(arr3, arr4));

        // different arrays of different types and lengths
        Object[] arr5 = new Object[]{1, 2};
        int[] arr6 = new int[]{1, 2};
        assertFalse(Equality.areEqual(arr5, arr6));

        // different arrays of the same type and length, but with null elements
        Object[] arr7 = new Object[]{1, null};
        Object[] arr8 = new Object[]{1, 2};
        assertFalse(Equality.areEqual(arr7, arr8));

        // equal arrays with null elements
        Object[] arr9 = new Object[]{1, null};
        Object[] arr10 = new Object[]{1, null};
        assertTrue(Equality.areEqual(arr9, arr10));

        // arrays with different lengths and equal elements
        Object[] arr11 = new Object[]{1, 2, 3};
        Object[] arr12 = new Object[]{1, 2};
        assertFalse(Equality.areEqual(arr11, arr12));

        // test the special case with Object arrays
        assertTrue(Equality.areEqual(obj5, obj6));
    }

    @Test
    public void testAreArraysEqual() {
        Object obj1 = new Object[]{1, 2};
        Object obj2 = new Object[]{1, 2};
        Object obj3 = new Object[]{1, 2, 3};
        Object obj4 = new Object[]{1, null};

        // arrays with equal length and equal elements
        assertTrue(Equality.areArraysEqual(obj1, obj2));

        // arrays with equal length, but non-equal elements
        assertFalse(Equality.areArraysEqual(obj1, obj3));

        // arrays with different lengths
        assertFalse(Equality.areArraysEqual(obj1, obj4));

        // arrays with null elements
        Object[] arr1 = new Object[]{1, null};
        Object[] arr2 = new Object[]{1, 2};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Object obj1 = new Object[]{1, 2};
        Object obj2 = new Object[]{1, 2};
        Object obj3 = new Object[]{1, 2, 3};
        Object obj4 = new Object[]{1, null};

        // arrays with equal length
        assertTrue(Equality.areArrayLengthsEqual(obj1, obj2));
        assertTrue(Equality.areArrayLengthsEqual(obj1, obj1));

        // arrays with different lengths
        assertFalse(Equality.areArrayLengthsEqual(obj1, obj3));
        assertFalse(Equality.areArrayLengthsEqual(obj1, obj4));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2};
        Object[] arr3 = new Object[]{1, 2, 3, 3};
        Object[] arr4 = new Object[]{1, null};

        // arrays with equal elements
        assertTrue(Equality.areArrayElementsEqual(arr1, arr1));
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));

        // arrays with non-equal elements
        assertFalse(Equality.areArrayElementsEqual(arr1, arr3));
        assertFalse(Equality.areArrayElementsEqual(arr1, arr4));

        // array with null elements
        Object[] arr5 = new Object[]{1, null};
        Object[] arr6 = new Object[]{1, 2};
        assertFalse(Equality.areArrayElementsEqual(arr5, arr6));

        // array with identical null elements
        Object[] arr7 = new Object[]{1, null, null};
        Object[] arr8 = new Object[]{1, null, null};
        assertTrue(Equality.areArrayElementsEqual(arr7, arr8));
    }

    @Test
    public void testIsArray() {
        Object obj1 = new Object[]{1, 2};
        int[] arr2 = {1, 2};
        String str3 = "test";

        // array
        assertTrue(Equality.isArray(obj1));

        // non-array
        assertFalse(Equality.isArray(arr2));
        assertFalse(Equality.isArray(str3));
    }
}