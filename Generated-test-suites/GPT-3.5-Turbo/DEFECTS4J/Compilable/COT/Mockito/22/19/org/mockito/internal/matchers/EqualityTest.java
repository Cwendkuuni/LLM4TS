package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_nullObjects() {
        assertTrue(Equality.areEqual(null, null));
    }
    
    @Test
    public void testAreEqual_firstObjectNull() {
        assertFalse(Equality.areEqual(null, new Object()));
    }
    
    @Test
    public void testAreEqual_secondObjectNull() {
        assertFalse(Equality.areEqual(new Object(), null));
    }
    
    @Test
    public void testAreEqual_objectsNotEqual() {
        assertFalse(Equality.areEqual("hello", "world"));
    }
    
    @Test
    public void testAreEqual_equalObjects() {
        assertTrue(Equality.areEqual("hello", "hello"));
    }
    
    @Test
    public void testAreArrayLengthsEqual_true() {
        Object array1 = new int[3];
        Object array2 = new int[3];
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }
    
    @Test
    public void testAreArrayLengthsEqual_false() {
        Object array1 = new int[3];
        Object array2 = new int[4];
        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }
    
    @Test
    public void testAreArrayElementsEqual_true() {
        Object array1 = new int[] {1, 2, 3};
        Object array2 = new int[] {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }
    
    @Test
    public void testAreArrayElementsEqual_false() {
        Object array1 = new int[] {1, 2, 3};
        Object array2 = new int[] {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }
    
    @Test
    public void testAreEqual_arrayObjectsNotEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 4};
        assertFalse(Equality.areEqual(array1, array2));
    }
    
    @Test
    public void testAreEqual_arrayObjectsEqual() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }
}
