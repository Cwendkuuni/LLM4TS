package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Test cases for when one or both arguments are null
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
        
        // Test cases for when both arguments are arrays
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {4, 5, 6};
        assertTrue(Equality.areEqual(array1, array2));
        assertFalse(Equality.areEqual(array1, array3));
        
        // Test cases for when both arguments are not arrays
        assertTrue(Equality.areEqual("foo", "foo"));
        assertFalse(Equality.areEqual("foo", "bar"));
    }

    @Test
    public void testAreArraysEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {4, 5, 6};
        
        assertTrue(Equality.areArraysEqual(array1, array2));
        assertFalse(Equality.areArraysEqual(array1, array3));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {1, 2};
        
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
        assertFalse(Equality.areArrayLengthsEqual(array1, array3));
    }

    @Test
    public void testAreArrayElementsEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {4, 5, 6};
        
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
        assertFalse(Equality.areArrayElementsEqual(array1, array3));
    }

    @Test
    public void testIsArray() {
        int[] array = {1, 2, 3};
        Object object = new Object();
        
        assertTrue(Equality.isArray(array));
        assertFalse(Equality.isArray(object));
    }
}