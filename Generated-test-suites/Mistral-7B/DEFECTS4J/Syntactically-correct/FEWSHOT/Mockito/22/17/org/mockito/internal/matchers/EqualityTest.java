package org.mockito.internal.matchers;

import org.junit.Test;
import org.junit.Assert.*;

public class EqualityTest {

 @Test
 public void testAreEqual() {
 String str1 = "foo";
 String str2 = "foo";
 Integer int1 = 1;
 Integer int2 = 1;
 Object[] array1 = new Object[]{str1, int1};
 Object[] array2 = new Object[]{str1, int1};
 Object[] array3 = new Object[]{str1, int2};
 Object[] array4 = new Object[]{int1, str1};

 assertTrue(Equality.areEqual(str1, str2));
 assertTrue(Equality.areEqual(int1, int2));
 assertTrue(Equality.areEqual(array1, array2));
 assertFalse(Equality.areEqual(array1, array3));
 assertFalse(Equality.areEqual(array1, array4));
 }

 @Test
 public void testAreArraysEqual() {
 Integer[] intArray1 = {1, 2, 3};
 Integer[] intArray2 = {1, 2, 3};
 Integer[] intArray3 = {1, 2, 4};
 Object[] objArray1 = {1, new Integer(2), 3};
 Object[] objArray2 = {1, new Integer(2), 3};
 Object[] objArray3 = {1, new Integer(3), 3};

 assertTrue(Equality.areArraysEqual(intArray1, intArray2));
 assertFalse(Equality.areArraysEqual(intArray1, intArray3));
 assertTrue(Equality.areArraysEqual(objArray1, objArray2));
 assertFalse(Equality.areArraysEqual(objArray1, objArray3));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 Integer[] intArray1 = new Integer[3];
 Integer[] intArray2 = new Integer[5];
 Object[] objArray1 = new Object[3];
 Object[] objArray2 = new Object[5];

 assertFalse(Equality.areArraysEqual(intArray1, intArray2));
 assertFalse(Equality.areArraysEqual(objArray1, objArray2));
 assertFalse(Equality.areArrayLengthsEqual(intArray1, intArray2));
 assertFalse(Equality.areArrayLengthsEqual(objArray1, objArray2));
 assertTrue(Equality.areArrayLengthsEqual(intArray1, intArray1));
 assertTrue(Equality.areArrayLengthsEqual(objArray1, objArray1));
 }

 @Test
 public void testIsArray() {
 Integer[] intArray = new Integer[3];
 Object[] objArray = new Object[3];
 String str = "foo";

 assertTrue(Equality.isArray(intArray));
 assertTrue(Equality.isArray(objArray));
 assertFalse(Equality.isArray(str));
 }
}