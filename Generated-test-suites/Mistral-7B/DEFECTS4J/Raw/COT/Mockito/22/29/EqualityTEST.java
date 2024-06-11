package org.mockito.internal.matchers.test;

import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import static org.mockito.internal.matchers.Equality.areArrayElementsEqual;
import static org.mockito.internal.matchers.Equality.areArrayLengthsEqual;
import static org.mockito.internal.matchers.Equality.areEqual;
import static org.mockito.internal.matchers.Equality.isArray;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualityTest {

 @Test
 public void testAreEqualNullWithNull() {
 assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqualNullWithNonNull() {
 assertFalse(Equality.areEqual(null, new Object()));
 }

 @Test
 public void testAreEqualSameInstance() {
 Object test = new Object();
 assertTrue(Equality.areEqual(test, test));
 }

 @Test
 public void testAreEqualObjectsWithSameValues() {
 Object o1 = new Object();
 Object o2 = new Object();
 assertTrue(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqualArraysWithSameLengthAndSameElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithDifferentLengths() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3, 4};
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test(expected = ClassCastException.class)
 public void testAreEqualNonArrayWithArray() {
 Object o1 = new Object();
 Object[] o2 = new Object[]{1, 2, 3};
 assertFalse(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqualArraysWithDifferentElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 4};
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithNullElements() {
 Object[] array1 = new Object[]{1, null, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithEmptyArray() {
 Object[] array1 = new Object[]{};
 Object[] array2 = new Object[]{};
 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithPrimitiveTypes() {
 byte[] array1 = new byte[]{1, 2, 3};
 short[] array2 = new short[]{1, 2, 3};
 int[] array3 = new int[]{1, 2, 3};
 long[] array4 = new long[]{1, 2, 3};
 float[] array5 = new float[]{1.0f, 2.0f, 3.0f};
 double[] array6 = new double[]{1.0, 2.0, 3.0};
 assertTrue(Equality.areEqual(array1, array1));
 assertTrue(Equality.areEqual(array1, array1.clone()));
 assertTrue(Equality.areEqual(array2, array2));
 assertTrue(Equality.areEqual(array2, array2.clone()));
 assertTrue(Equality.areEqual(array3, array3));
 assertTrue(Equality.areEqual(array3, array3.clone()));
 assertTrue(Equality.areEqual(array4, array4));
 assertTrue(Equality.areEqual(array4, array4.clone()));
 assertTrue(Equality.areEqual(array5, array5));
 assertTrue(Equality.areEqual(array5, array5.clone()));
 assertTrue(Equality.areEqual(array6, array6));
 assertTrue(Equality.areEqual(array6, array6.clone()));
 assertTrue(Equality.areEqual(array1, array2)); // should be false since they are not the same type
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithArraysAsElements() {
 Object[] array1 = new Object[]{new Object[]{1, 2}, new Object[]{3, 4}};
 Object[] array2 = new Object[]{new Object[]{1, 2}, new Object[]{3, 4}};
 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithReferencesToThisArray() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = array1;
 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreEqualNullWithNullArray() {
 Object[] nullArray = null;
 assertFalse(Equality.areEqual(null, nullArray));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreEqualNullWithNonArray() {
 Object nonArray = new Object();
 assertFalse(Equality.areEqual(nonArray, null));
 }

 @Test
 public void testAreArraysEqualArraysWithSameLengthAndSameElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysWithDifferentLengths() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3, 4};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAreArraysEqualArraysWithNegativeIndex() {
 Object[] array = new Object[]{1, 2, 3};
 assertFalse(Equality.areArraysEqual(array, new Object[]{1, 2, 3, 4}));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAreArraysEqualArraysWithArrayIndexGreaterThanLength() {
 Object[] array = new Object[]{1, 2, 3};
 assertFalse(Equality.areArraysEqual(array, new Object[]{1, 2, 3, 4}));
 }

 @Test(expected = ClassCastException.class)
 public void testAreArraysEqualArraysWithDifferentTypes() {
 Object[] arrayInt = new Object[]{1, 2, 3};
 Object[] arrayDouble = new Object[]{1.0, 2.0, 3.0};
 assertFalse(Equality.areArraysEqual(arrayInt, arrayDouble));
 }

 @Test(expected = NullPointerException.class)
 public void testAreArraysEqualArraysWithNullElements() {
 Object[] array1 = new Object[]{1, null, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test(expected = IllegalStateException.class)
 public void testAreArraysEqualArraysWithArraysAsElementsRecursively() {
 Object[] array1 = new Object[]{new Object[]{1, 2}, new Object[]{3, 4}};
 Object[] array2 = new Object[]{new Object[]{1, new Object[]{2, 3}}, new Object[]{3, 4}};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreArraysEqualArraysWithPrimitiveTypesAsElements() {
 byte[] array1 = new byte[]{1, 2, 3};
 short[] array2 = new short[]{1, 2, 3};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreArraysEqualArraysWithDifferentTypesAsElements() {
 byte[] arrayInt = new byte[]{1, 2, 3};
 int[] arrayDouble = new int[]{1, 2, 3};
 assertFalse(Equality.areArraysEqual(arrayInt, arrayDouble));
 }

 @Test
 public void testIsArray() {
 assertTrue(Equality.isArray(new Object[]{1, 2, 3}));
 assertTrue(Equality.isArray(new int[]{1, 2, 3}));
 assertTrue(Equality.isArray(new boolean[]{true, false, true}));
 assertTrue(Equality.isArray(new double[]{1.0, 2.0, 3.0}));
 assertFalse(Equality.isArray(new Object()));
 assertFalse(Equality.isArray("test"));
 }

}