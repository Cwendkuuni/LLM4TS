package org.mockito.internal.matchers;
import org.junit.Test;

import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqualNullInputs() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqualArrayInputs() {
        Object[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 2, 3};
        Object[] arr3 = {1, 2, 4};

        assertTrue(Equality.areEqual(arr1, arr2));
        assertFalse(Equality.areEqual(arr1, arr3));
    }

    @Test
    public void testAreEqualObjectInputs() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        assertFalse(Equality.areEqual(obj1, obj2));
        assertTrue(Equality.areEqual(obj1, obj1));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Object[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 2, 3, 4};

        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr1));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Object[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 2, 3};
        Object[] arr3 = {1, 2, 4};

        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
        assertFalse(Equality.areArrayElementsEqual(arr1, arr3));
    }

    @Test
    public void testIsArray() {
        int[] arr = {1, 2, 3};

        assertTrue(Equality.isArray(arr));
        assertFalse(Equality.isArray(new Object()));
    }
}