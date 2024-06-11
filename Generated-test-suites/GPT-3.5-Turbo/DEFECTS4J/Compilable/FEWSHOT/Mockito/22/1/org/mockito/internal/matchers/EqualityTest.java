package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        // Test with both null objects
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);

        // Test with one null object
        result = Equality.areEqual(null, new Object());
        assertFalse(result);

        result = Equality.areEqual(new Object(), null);
        assertFalse(result);

        // Test with non-array objects
        result = Equality.areEqual("hello", "hello");
        assertTrue(result);

        result = Equality.areEqual("hello", "world");
        assertFalse(result);

        result = Equality.areEqual(123, 123);
        assertTrue(result);

        result = Equality.areEqual(123, 456);
        assertFalse(result);

        // Test with array objects
        result = Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        assertTrue(result);

        result = Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4});
        assertFalse(result);

        result = Equality.areEqual(new String[]{"hello", "world"}, new String[]{"hello", "world"});
        assertTrue(result);

        result = Equality.areEqual(new String[]{"hello", "world"}, new String[]{"hello", "mars"});
        assertFalse(result);
    }

    @Test
    public void testAreArraysEqual() {
        // Test with equal array lengths and elements
        boolean result = Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        assertTrue(result);

        // Test with different array lengths
        result = Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3, 4});
        assertFalse(result);

        // Test with equal array lengths but different elements
        result = Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4});
        assertFalse(result);
    }

    @Test
    public void testAreArrayLengthsEqual() {
        // Test with equal array lengths
        boolean result = Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        assertTrue(result);

        // Test with different array lengths
        result = Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3, 4});
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual() {
        // Test with equal elements
        boolean result = Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        assertTrue(result);

        // Test with different elements
        result = Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4});
        assertFalse(result);
    }

    @Test
    public void testIsArray() {
        // Test with an array
        boolean result = Equality.isArray(new int[]{1, 2, 3});
        assertTrue(result);

        // Test with a non-array object
        result = Equality.isArray("hello");
        assertFalse(result);
    }
}