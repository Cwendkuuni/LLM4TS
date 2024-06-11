package org.mockito.internal.matchers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqualNullNull() {
 assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqualNullObject() {
 assertFalse(Equality.areEqual(null, new Object()));
 }

 @Test
 public void testAreEqualObjectNull() {
 assertFalse(Equality.areEqual(new Object(), null));
 }

 @Test
 public void testAreEqualObjects() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 assertTrue(Equality.areEqual(obj1, obj2));

 Object obj3 = new Object();
 assertFalse(Equality.areEqual(obj1, obj3));
 }

 @Test
 public void testAreEqualArrays() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object()};
 assertTrue(Equality.areEqual(arr1, arr2));

 Object[] arr3 = new Object[]{new Object(), new Object()};
 Object[] arr4 = new Object[]{new Object(), new Object(), new Object()};
 assertFalse(Equality.areEqual(arr1, arr4));
 }

 @Test
 public void testAreEqualArraysDifferentLengths() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object(), new Object()};
 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysOneNull() {
 Object[] arr1 = new Object[]{new Object(), new Object(), null};
 Object[] arr2 = new Object[]{new Object(), new Object(), new Object()};
 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysOneNullOtherNull() {
 Object[] arr1 = new Object[]{null, new Object()};
 Object[] arr2 = new Object[]{null, null};
 assertTrue(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqualArrayLengthsNotEqual() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object(), new Object()};
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqualSameArrays() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = arr1;
 assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqualArraysWithSameElements() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object()};
 assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqualArraysWithDifferentElements() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object(), new Object()};
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqualArraysWithNullElements() {
 Object[] arr1 = new Object[]{new Object(), new Object(), null};
 Object[] arr2 = new Object[]{new Object(), new Object(), null};
 assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testIsArrayPrimitiveBoolean() {
 boolean[] arr = new boolean[0];
 assertTrue(Equality.isArray(arr));
 }

 @Test
 public void testIsArrayPrimitiveInt() {
 int[] arr = new int[0];
 assertTrue(Equality.isArray(arr));
 }

 @Test
 public void testIsArrayObject() {
 Object[] arr = new Object[0];
 assertTrue(Equality.isArray(arr));
 }

 @Test
 public void testIsArrayNull() {
 assertFalse(Equality.isArray(null));
 }

 @Test
 public void testIsArrayNonArrayObject() {
 Object obj = new Object();
 assertFalse(Equality.isArray(obj));
 }
}