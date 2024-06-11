
package org.junit.test; // Replace with your test package name

import static org.junit.Assert.*;
import org.mockito.internal.matchers.Equality.*;

public class EqualityTest {

    @Test
    public void testAreEqual_SameObjects() {
        //Same objects
        Integer num1 = new Integer(1);
        Integer num2 = num1;
        assertTrue(areEqual(num1, num2));
    }

    @Test
    public void testAreEqual_NullObjects() {
        //Null objects
        assertFalse(areEqual(null, null));
        assertFalse(areEqual(null, new Object()));
        assertFalse(areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_DifferentObjectsWithSameValues() {
        //Different objects with same values
        Integer num1 = new Integer(1);
        Integer num2 = new Integer(1);
        assertTrue(areEqual(num1, num2));
    }

    @Test
    public void testAreEqual_DifferentObjectsWithDifferentValues() {
        //Different objects with different values
        Integer num1 = new Integer(1);
        Integer num2 = new Integer(2);
        assertFalse(areEqual(num1, num2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqual_OneObjectIsArray_OtherIsNot() {
        //One object is an array, other is not
        Integer[] arrInt1 = {1, 2, 3};
        Integer num = new Integer(1);
        try {
            areEqual(arrInt1, num);
        } catch (IllegalArgumentException e) {
            assertEquals("expected:<T> but:<int[]>(<org.junit.Assert.AssertionError: expected:<T> but:<int[]>)", e.getMessage());
        }
    }

    @Test
    public void testAreEqual_BothObjectsAreArrays() {
        //Both objects are arrays
        Integer[] arrInt1 = {1, 2, 3};
        Integer[] arrInt2 = {1, 2, 3};
        assertTrue(areArraysEqual(arrInt1, arrInt2));
    }

    @Test
    public void testAreArraysEqual_SameArrays() {
        //Same arrays
        Integer[] arrInt1 = {1, 2, 3};
        Integer[] arrInt2 = arrInt1;
        assertTrue(areArraysEqual(arrInt1, arrInt2));
    }

    @Test
    public void testAreArraysEqual_DifferentArrays() {
        //Different arrays
        Integer[] arrInt1 = {1, 2, 3};
        Integer[] arrInt2 = {1, 2, 4};
        assertFalse(areArraysEqual(arrInt1, arrInt2));
    }

    @Test
    public void testAreArraysEqual_ArraysWithSameLengthButDifferentElements() {
        //Arrays with the same length but different elements
        Integer[] arrInt1 = {1, 2, 3};
        Integer[] arrInt2 = {1, 2};
        assertFalse(areArraysEqual(arrInt1, arrInt2));
    }

    @Test
    public void testAreArraysEqual_ArraysWithDifferentLengthsButSameElements() {
        //Arrays with different lengths but the same elements
        Integer[] arrInt1 = {1, 2, 3};
        Integer[] arrInt2 = {1, 2};
        assertFalse(areArraysEqual(arrInt1, arrInt2));
    }

    @Test
    public void testAreArraysEqual_ArraysWithNullElements() {
        //Arrays with null elements
        Integer[] arrInt1 = {1, null, 3};
        Integer[] arrInt2 = {1, 2, 3};
        assertFalse(areArraysEqual(arrInt1, arrInt2));
    }

    @Test
    public void testAreArraysEqual_ArraysWithPrimitiveTypes() {
        //Arrays with primitive types
        byte[] arrByte1 = {1, 2, 3};
        byte[] arrByte2 = {1, 2, 3};
        assertTrue(areArraysEqual(arrByte1, arrByte2));
    }

    @Test
    public void testAreArraysEqual_ArraysWithObjects() {
        //Arrays with objects
        String str1 = "test";
        String str2 = "test";
        Object[] obj1 = {str1, str1};
        Object[] obj2 = {str1, str1};
        assertTrue(areArraysEqual(obj1, obj2));
    }

    @Test
    public void testAreArraysEqual_ArraysWithMismatchedElementTypes() {
        //Arrays with mismatched element types
        Integer[] arrInt1 = {1, 2, 3};
        String[] arrStr1 = {"test", "test", "test"};
        assertFalse(areArraysEqual(arrInt1, arrStr1));
    }

    @Test
    public void testAreArraysEqual_ArraysWithMultipleObjectTypes() {
        //Arrays with multiple object types
        Integer[] arrInt1 = {1, new Integer(2), "test"};
        Object[] obj1 = {"test", new Integer(2), "test"};
        assertFalse(areArraysEqual(arrInt1, obj1));
    }

    @Test
    public void testAreArraysEqual_ArraysWithDifferentObjectTypes() {
        //Arrays with different object types
        Integer[] arrInt1 = {1, new Integer(2), 3};
        Long[] arrLong1 = {1L, 2L, 3L};
        assertFalse(areArraysEqual(arrInt1, arrLong1));
    }
}