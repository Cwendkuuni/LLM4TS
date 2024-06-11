package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        // Test when both objects are null
        assertTrue(Equality.areEqual(null, null));

        // Test when one object is null and the other is not
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));

        // Test when both objects are arrays with equal lengths and equal elements
        assertTrue(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));

        // Test when both objects are arrays with equal lengths but different elements
        assertFalse(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));

        // Test when one object is an array and the other is not
        assertFalse(Equality.areEqual(new int[]{1, 2, 3}, new Object()));

        // Test when both objects are not arrays
        assertTrue(Equality.areEqual("hello", "hello"));
        assertFalse(Equality.areEqual("hello", "world"));
    }

    @Test
    public void testAreArraysEqual() {
        // Test when both arrays have equal lengths and equal elements
        assertTrue(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));

        // Test when both arrays have equal lengths but different elements
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));

        // Test when arrays have different lengths
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        // Test when array lengths are equal
        assertTrue(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{4, 5, 6}));

        // Test when array lengths are not equal
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3, 4}));
    }

    @Test
    public void testAreArrayElementsEqual() {
        // Test when array elements are equal
        assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));

        // Test when array elements are not equal
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testIsArray() {
        // Test when object is an array
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));

        // Test when object is not an array
        assertFalse(Equality.isArray("hello"));
    }
}