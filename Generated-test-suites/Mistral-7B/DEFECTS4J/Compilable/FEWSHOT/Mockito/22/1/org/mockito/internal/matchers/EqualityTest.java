package org.mockito.internal.matchers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqual() {
 // Test null objects
 assertFalse(Equality.areEqual(null, null));
 assertFalse(Equality.areEqual(null, new Object()));
 assertFalse(Equality.areEqual(new Object(), null));

 // Test simple objects
 Object obj1 = new Object();
 Object obj2 = new Object();
 assertFalse(Equality.areEqual(obj1, obj2));
 obj2 = obj1; // same object
 assertTrue(Equality.areEqual(obj1, obj2));

 // Test arrays
 Object[] arr1 = new Object[0];
 Object[] arr2 = new Object[0];
 assertTrue(Equality.areEqual(arr1, arr2));

 Object[] arr3 = new Object[]{new Object(), new Object()};
 Object[] arr4 = new Object[]{new Object(), new Object()};
 assertTrue(Equality.areEqual(arr3, arr4));

 Object[] arr5 = new Object[]{new Object(), new Object()};
 Object[] arr6 = new Object[]{new Object(), new Integer(0)};
 assertFalse(Equality.areEqual(arr5, arr6));
 }

 @Test
 public void testAreArraysEqual() {
 // Test arrays of different lengths
 Object[] arr1 = new Object[0];
 Object[] arr2 = new Object[1];
 assertFalse(Equality.areArraysEqual(arr1, arr2));

 // Test arrays of same length with different elements
 Object[] arr3 = new Object[]{new Object(), new Object()};
 Object[] arr4 = new Object[]{new Integer(0), new Object()};
 assertFalse(Equality.areArraysEqual(arr3, arr4));

 // Test arrays of same length with same elements
 Object[] arr5 = new Object[]{new Object(), new Object()};
 Object[] arr6 = new Object[]{new Object(), new Object()};
 assertTrue(Equality.areArraysEqual(arr5, arr6));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 // Test arrays of different lengths
 Object[] arr1 = new Object[0];
 Object[] arr2 = new Object[1];
 assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));

 // Test arrays of same length
 Object[] arr3 = new Object[0];
 Object[] arr4 = new Object[0];
 assertTrue(Equality.areArrayLengthsEqual(arr3, arr4));

 Object[] arr5 = new Object[3];
 Object[] arr6 = new Object[3];
 assertTrue(Equality.areArrayLengthsEqual(arr5, arr6));
 }

 @Test
 public void testAreArrayElementsEqual() {
 // Test empty arrays
 Object[] arr1 = new Object[0];
 Object[] arr2 = new Object[0];
 assertTrue(Equality.areArrayElementsEqual(arr1, arr2));

 // Test arrays with one element
 Object[] arr3 = new Object[]{new Object()};
 Object[] arr4 = new Object[]{new Object()};
 assertTrue(Equality.areArrayElementsEqual(arr3, arr4));

 // Test arrays with multiple elements
 Object[] arr5 = new Object[]{new Object(), new Object()};
 Object[] arr6 = new Object[]{new Object(), new Object()};
 assertTrue(Equality.areArrayElementsEqual(arr5, arr6));

 // Test arrays with different elements
 Object[] arr7 = new Object[]{new Object(), new Object()};
 Object[] arr8 = new Object[]{new Integer(0), new Object()};
 assertFalse(Equality.areArrayElementsEqual(arr7, arr8));
 }

 @Test
 public void testIsArray() {
 // Test non-array objects
 assertFalse(Equality.isArray(null));
 assertFalse(Equality.isArray(new Object()));

 // Test array objects
 assertTrue(Equality.isArray(new int[0]));
 assertTrue(Equality.isArray(new int[0][]));
 assertTrue(Equality.isArray(new int[0][0][]));
 }
}