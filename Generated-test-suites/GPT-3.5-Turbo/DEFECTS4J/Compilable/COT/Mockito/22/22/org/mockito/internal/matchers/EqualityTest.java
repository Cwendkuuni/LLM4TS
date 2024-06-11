package org.mockito.internal.matchers;
import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Basic test case
        assertTrue(Equality.areEqual("hello", "hello"));
        assertFalse(Equality.areEqual("hello", "world"));

        // Testing with null values
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, "hello"));
        assertFalse(Equality.areEqual("hello", null));
    }

    @Test
    public void testAreArraysEqual() {
        // Basic test case
        int[] array1 = new int[] { 1, 2, 3 };
        int[] array2 = new int[] { 1, 2, 3 };
        assertTrue(Equality.areArraysEqual(array1, array2));

        int[] array3 = new int[] { 1, 2, 3 };
        int[] array4 = new int[] { 1, 2, 4 };
        assertFalse(Equality.areArraysEqual(array3, array4));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        // Basic test case
        int[] array1 = new int[] { 1, 2, 3 };
        int[] array2 = new int[] { 1, 2, 3 };
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));

        int[] array3 = new int[] { 1, 2, 3 };
        int[] array4 = new int[] { 1, 2, 3, 4 };
        assertFalse(Equality.areArrayLengthsEqual(array3, array4));
    }

    @Test
    public void testAreArrayElementsEqual() {
        // Basic test case
        int[] array1 = new int[] { 1, 2, 3 };
        int[] array2 = new int[] { 1, 2, 3 };
        assertTrue(Equality.areArrayElementsEqual(array1, array2));

        int[] array3 = new int[] { 1, 2, 3 };
        int[] array4 = new int[] { 1, 2, 4 };
        assertFalse(Equality.areArrayElementsEqual(array3, array4));
    }

    @Test
    public void testIsArray() {
        // Basic test case
        int[] array = new int[] { 1, 2, 3 };
        assertTrue(Equality.isArray(array));

        String str = "hello";
        assertFalse(Equality.isArray(str));
    }

    // Add additional test cases for edge cases and exception handling scenarios as needed

}

