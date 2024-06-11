package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_Arrays() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {4, 5, 6};
        
        assertTrue(Equality.areEqual(array1, array2));
        assertFalse(Equality.areEqual(array1, array3));
        assertFalse(Equality.areEqual(array1, new int[]{1, 2}));
    }

    @Test
    public void testAreEqual_Objects() {
        String str1 = "Hello";
        String str2 = "Hello";
        String str3 = "World";
        
        assertTrue(Equality.areEqual(str1, str2));
        assertFalse(Equality.areEqual(str1, str3));
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
        int[] array2 = {1, 2, 3, 4};
        
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray() {
        int[] array = {1, 2, 3};
        String str = "Hello";
        
        assertTrue(Equality.isArray(array));
        assertFalse(Equality.isArray(str));
    }
}