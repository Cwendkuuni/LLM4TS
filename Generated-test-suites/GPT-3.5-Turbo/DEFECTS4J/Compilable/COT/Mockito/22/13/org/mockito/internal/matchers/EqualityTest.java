package org.mockito.internal.matchers;

import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_PrimitiveTypes() {
        assertTrue(Equality.areEqual(5, 5));
        assertFalse(Equality.areEqual(5, 10));
    }

    @Test
    public void testAreEqual_ObjectTypes() {
        assertTrue(Equality.areEqual("hello", "hello"));
        assertFalse(Equality.areEqual("hello", "world"));
        assertTrue(Equality.areEqual(new Integer(5), new Integer(5)));
        assertFalse(Equality.areEqual(new Integer(5), new Integer(10)));
    }

    @Test
    public void testAreEqual_Arrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        int[] arr3 = {1, 2, 4};

        assertTrue(Equality.areEqual(arr1, arr1));
        assertTrue(Equality.areEqual(arr1, arr2));
        assertFalse(Equality.areEqual(arr1, arr3));
    }

    @Test
    public void testAreEqual_InvalidInput() {
        assertFalse(Equality.areEqual("hello", null));
        assertFalse(Equality.areEqual(null, "hello"));
    }
}

