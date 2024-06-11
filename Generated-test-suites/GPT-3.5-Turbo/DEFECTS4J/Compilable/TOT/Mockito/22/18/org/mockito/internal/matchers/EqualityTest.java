package org.mockito.internal.matchers;

import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_sameObject() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_nullObject() {
        Object obj = null;
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_nullObjects() {
        assertTrue(Equality.areEqual(null, null));
    }
  
    @Test
    public void testAreEqual_differentTypes() {
        assertFalse(Equality.areEqual(5, "5"));
    }
  
    @Test
    public void testAreEqual_equalArrays() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }
  
    @Test
    public void testAreEqual_unequalArrays() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {3, 2, 1};
        assertFalse(Equality.areEqual(array1, array2));
    }
  
    @Test
    public void testAreEqual_nullArray() {
        int[] array = null;
        assertTrue(Equality.areEqual(array, array));
    }
  
    @Test
    public void testAreEqual_nullArrays() {
        assertTrue(Equality.areEqual(null, null));
    }
  
    @Test
    public void testAreArrayElementsEqual_sameObject() {
        int[] array = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array, array));
    }
  
    @Test
    public void testAreArrayElementsEqual_nullObject() {
        int[] array = null;
        assertTrue(Equality.areArrayElementsEqual(array, array));
    }
  
    @Test
    public void testAreArrayElementsEqual_nullObjects() {
        assertTrue(Equality.areArrayElementsEqual(null, null));
    }
  
    @Test
    public void testAreArrayElementsEqual_equalArrays() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }
  
    @Test
    public void testAreArrayElementsEqual_unequalArrays() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {3, 2, 1};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }
}
