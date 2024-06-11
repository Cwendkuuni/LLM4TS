package org.mockito.internal.matchers;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EqualityTest {

 @Test
 public void testAreEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Object obj3 = new Object();

 assertTrue(Equality.areEqual(obj1, obj2));
 assertTrue(Equality.areEqual(obj2, obj1));
 assertTrue(Equality.areEqual(obj1, obj1));
 assertFalse(Equality.areEqual(obj1, obj3));
 assertFalse(Equality.areEqual(obj3, obj1));
 }

 @Test
 public void testAreEqualNull() {
 assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqualObjects() {
 String str1 = new String("foo");
 String str2 = new String("foo");
 String str3 = new String("bar");

 assertTrue(Equality.areEqual(str1, str2));
 assertFalse(Equality.areEqual(str1, str3));
 }

 @Test
 public void testAreEqualArrays() {
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {1, 2, 3};
 int[] arr3 = {1, 2, 4};

 assertTrue(Equality.areEqual(arr1, arr2));
 assertFalse(Equality.areEqual(arr1, arr3));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {1, 2, 3, 4};
 int[] arr3 = {1, 2};

 assertFalse(Equality.areArraysEqual(arr1, arr2));
 assertFalse(Equality.areArraysEqual(arr1, arr3));
 assertFalse(Equality.areArraysEqual(arr2, arr3));
 }

 @Test
 public void testAreArraysEqualWithNulls() {
 Object[] arr1 = {null, new Object()};
 Object[] arr2 = {null, new Object()};
 Object[] arr3 = {new Object(), null};
 Object[] arr4 = {null, new Object()};
 Object[] arr5 = {new Object()};

 assertTrue(Equality.areArraysEqual(arr1, arr2));
 assertFalse(Equality.areArraysEqual(arr1, arr3));
 assertFalse(Equality.areArraysEqual(arr2, arr3));
 assertFalse(Equality.areArraysEqual(arr1, arr4));
 assertFalse(Equality.areArraysEqual(arr1, arr5));
 }
}