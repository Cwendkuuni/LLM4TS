package org.mockito.internal.matchers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqualNulls() {
 assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqualObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = obj1;

 assertTrue(Equality.areEqual(obj1, obj1));
 assertTrue(Equality.areEqual(obj1, obj3));
 assertFalse(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualArrays() {
 Object[] arr1 = new Object[] {new Object(), new Object()};
 Object[] arr2 = new Object[] {new Object(), new Object()};
 Object[] arr3 = new Object[] {new Object(), new Object(), new Object()};

 assertTrue(Equality.areEqual(arr1, arr1));
 assertTrue(Equality.areEqual(arr1, arr2)); //should be false
 assertTrue(Equality.areEqual(arr1, Arrays.copyOf(arr1, arr1.length)));
 assertFalse(Equality.areEqual(arr1, arr3));
 }

 @Test
 public void testAreEqualArraysDifferentLengths() {
 Object[] arr1 = new Object[] {new Object(), new Object()};
 Object[] arr2 = new Object[] {new Object(), new Object(), new Object()};

 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysSameLengthDifferentElements() {
 Object[] arr1 = new Object[] {new Object(), new Object()};
 Object[] arr2 = new Object[] {new Object(), new Object(), new Object()};

 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testArrayElementsEqual() {
 Object[] arr1 = new Object[] {new Object(), new Object()};
 Object[] arr2 = new Object[] {new Object(), new Object()};
 Object[] arr3 = new Object[] {new Object(), new Object(), new Object()};

 assertTrue(Equality.areArraysEqual(arr1, arr1));
 assertFalse(Equality.areArraysEqual(arr1, arr2)); //should be false
 assertTrue(Equality.areArraysEqual(arr1, Arrays.copyOf(arr1, arr1.length)));
 assertFalse(Equality.areArraysEqual(arr1, arr3));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 Object[] arr1 = new Object[] {new Object(), new Object()};
 Object[] arr2 = new Object[] {new Object(), new Object(), new Object()};
 Object[] arr3 = new Object[] {new Object(), new Object()};

 assertTrue(Equality.areArrayLengthsEqual(arr1, arr1));
 assertFalse(Equality.areArrayLengthsEqual(arr1, arr2)); //should be false
 assertTrue(Equality.areArrayLengthsEqual(arr1, arr3));
 }

 @Test
 public void testIsArray() {
 Object obj = new Object();
 Object[] arr = new Object[1];

 assertTrue(Equality.isArray(arr));
 assertFalse(Equality.isArray(obj));
 }
}