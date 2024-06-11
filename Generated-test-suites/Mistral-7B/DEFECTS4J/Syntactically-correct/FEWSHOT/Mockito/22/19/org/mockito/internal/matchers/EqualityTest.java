package org.mockito.internal.matchers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqual_ObjectsAreEqual() {
 String str1 = new String("foo");
 String str2 = new String("foo");
 assertTrue(Equality.areEqual(str1, str2));
 }

 @Test
 public void testAreEqual_ObjectsAreDifferent() {
 String str1 = new String("foo");
 String str2 = new String("bar");
 assertFalse(Equality.areEqual(str1, str2));
 }

 @Test
 public void testAreEqual_OneObjectIsNull() {
 String str1 = new String("foo");
 assertFalse(Equality.areEqual(str1, null));
 }

 @Test
 public void testAreEqual_BothObjectsIsNull() {
 assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqual_ArrayOfPrimitives() {
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {1, 2, 3};
 assertTrue(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqual_ArraysDifferentLengths() {
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {1, 2};
 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqual_ArraysWithSameElementsButDifferentOrder() {
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {2, 3, 1};
 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqual_ArraysWithDifferentElements() {
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {1, 2, 4};
 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqual_ArrayOfObjects() {
 String[] arr1 = {"foo", "bar"};
 String[] arr2 = {"bar", "foo"};
 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqual_ArraysWithSameObjects() {
 String[] arr1 = {"foo", "bar"};
 String[] arr2 = {"bar", "foo"};
 String[] arr3 = {"foo", "bar"};
 assertTrue(Equality.areEqual(arr1, arr3));
 }

 @Test
 public void testAreEqual_ArraysWithNullElements() {
 Object[] arr1 = {"foo", null};
 Object[] arr2 = {null, "foo"};
 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqual_ArraysAreEqual() {
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {1, 2, 3};
 assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqual_ArraysAreDifferentLengths() {
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {1, 2};
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqual_ArraysHaveDifferentElements() {
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {1, 2, 4};
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqual_ArraysWithNullElements() {
 int[] arr1 = {1, null, 3};
 int[] arr2 = {1, 2, 3};
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testIsArray_PrimitivesArray() {
 boolean[] arr = new boolean[0];
 assertTrue(Equality.isArray(arr));
 }

 @Test
 public void testIsArray_ObjectArray() {
 Object[] arr = new Object[0];
 assertTrue(Equality.isArray(arr));
 }

 @Test
 public void testIsArray_NonArrayObject() {
 String str = new String("foo");
 assertFalse(Equality.isArray(str));
 }
}