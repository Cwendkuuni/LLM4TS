package org.mockito.internal.matchers;

import static org.junit.Assert.*;

import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqual_NullNull() {
 assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqual_NullObject() {
 assertFalse(Equality.areEqual(null, new Object()));
 }

 @Test
 public void testAreEqual_ObjectNull() {
 assertFalse(Equality.areEqual(new Object(), null));
 }

 @Test
 public void testAreEqual_SameInstance() {
 Object obj = new Object();
 assertTrue(Equality.areEqual(obj, obj));
 }

 @Test
 public void testAreEqual_DifferentInstancesButEqual() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 obj1.equals(obj2); // assuming equals method is implemented correctly
 assertTrue(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqual_DifferentInstancesAndNotEqual() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 assertFalse(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreArraysEqual_NullNull() {
 assertTrue(Equality.areArraysEqual(null, null));
 }

 @Test
 public void testAreArraysEqual_NullArray() {
 assertFalse(Equality.areArraysEqual(null, new Object[]{}));
 }

 @Test
 public void testAreArraysEqual_ArrayNull() {
 assertFalse(Equality.areArraysEqual(new Object[]{}, null));
 }

 @Test
 public void testAreArraysEqual_ArraysOfDifferentLengths() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object()};
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqual_ArraysOfSameLengthButNotEqualElements() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object()[0]};
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqual_ArraysOfSameLengthAndEqualElements() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object()};
 assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqual_ArraysOfSameLengthButNullElement() {
 Object[] arr1 = new Object[]{new Object(), new Object(), null};
 Object[] arr2 = new Object[]{new Object(), new Object(), null};
 assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqual_ArraysOfSameLengthButDifferentNullElementsIndex() {
 Object[] arr1 = new Object[]{new Object(), null, new Object()};
 Object[] arr2 = new Object[]{null, new Object(), new Object()};
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqual_ArraysOfSameElementsButDifferentOrder() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object()[0], new Object()[1]};
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqual_ArraysOfReferenceTypes() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object()};
 Object obj1 = arr1[0];
 Object obj2 = arr2[0];
 arr1[0] = arr2[0];
 arr2[0] = obj1;
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 arr1[0] = obj1;
 arr2[0] = obj2;
 assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testIsArray_PrimitiveArray() {
 byte[] arr = new byte[10];
 assertTrue(Equality.isArray(arr));
 }

 @Test
 public void testIsArray_ObjectArray() {
 Object[] arr = new Object[10];
 assertTrue(Equality.isArray(arr));
 }

 @Test
 public void testIsArray_StringArray() {
 String[] arr = new String[10];
 assertTrue(Equality.isArray(arr));
 }

 @Test
 public void testIsArray_ArrayOfArrays() {
 Object[][] arr = new Object[10][10];
 assertTrue(Equality.isArray(arr));
 }
}