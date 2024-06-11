package org.mockito.internal.matchers;
import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual("hello", "hello"));
        assertFalse(Equality.areEqual("hello", "world"));
    }

    @Test
    public void testAreArraysEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(array1, array2));
        
        String[] strArray1 = {"hello", "world"};
        String[] strArray2 = {"hello", "world"};
        assertTrue(Equality.areArraysEqual(strArray1, strArray2));
        
        int[] array3 = {1, 2, 3};
        int[] array4 = {1, 2, 4};
        assertFalse(Equality.areArraysEqual(array3, array4));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
        
        int[] array3 = {1, 2, 3};
        int[] array4 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array3, array4));
    }

    @Test
    public void testAreArrayElementsEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
        
        int[] array3 = {1, 2, 3};
        int[] array4 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(array3, array4));
    }

    @Test
    public void testIsArray() {
        int[] array = {1, 2, 3};
        assertTrue(Equality.isArray(array));
        
        String str = "hello";
        assertFalse(Equality.isArray(str));
    }
}