package org.mockito.internal.matchers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqual() {
 // Test equal objects
 assertTrue(Equality.areEqual(1, 1));
 assertTrue(Equality.areEqual("foo", "foo"));

 // Test null objects
 assertFalse(Equality.areEqual(null, 1));
 assertFalse(Equality.areEqual(1, null));
 assertFalse(Equality.areEqual(null, null));

 // Test arrays
 Object[] arr1 = new Object[] {1, "foo"};
 Object[] arr2 = new Object[] {1, "foo"};
 Object[] arr3 = new Object[] {1, "bar"};
 Object[] arr4 = null;

 assertTrue(Equality.areEqual(arr1, arr2));
 assertFalse(Equality.areEqual(arr1, arr3));
 assertFalse(Equality.areEqual(arr1, arr4));
 }

 @Test
 public void testAreArraysEqual() {
 Object[] arr1 = new Object[] {1, "foo", null};
 Object[] arr2 = new Object[] {1, "foo", null};
 Object[] arr3 = new Object[] {1, "foo", "bar"};
 Object[] arr4 = new Object[] {1, "bar", null};
 Object[] arr5 = new Object[] {null, "foo", null};

 assertTrue(Equality.areArraysEqual(arr1, arr2));
 assertFalse(Equality.areArraysEqual(arr1, arr3));
 assertFalse(Equality.areArraysEqual(arr1, arr4));
 assertFalse(Equality.areArraysEqual(arr1, arr5));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 Object[] arr1 = new Object[] {1, "foo"};
 Object[] arr2 = new Object[] {1, "foo"};
 Object[] arr3 = new Object[] {1, "foo", "bar"};
 Object[] arr4 = new Object[] {};

 assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
 assertFalse(Equality.areArrayLengthsEqual(arr1, arr3));
 assertFalse(Equality.areArrayLengthsEqual(arr1, arr4));
 }

 @Test
 public void testAreArrayElementsEqual() {
 Object[] arr1 = new Object[] {1, "foo"};
 Object[] arr2 = new Object[] {1, "foo"};
 Object[] arr3 = new Object[] {1, "bar"};
 Object[] arr4 = new Object[] {null, null};
 Object[] arr5 = new Object[] {1, null};

 for (int i = 0; i < arr1.length; i++) {
 assertTrue(Equality.areEqual(arr1[i], arr2[i]));
 }

 assertFalse(Equality.areArraysEqual(arr1, arr3));
 assertFalse(Equality.areEqual(arr1[0], arr4[0]));
 assertFalse(Equality.areEqual(arr1[1], arr4[1]));
 assertNull(arr5[1]);
 }

 @Test
 public void testIsArray() {
 assertTrue(Equality.isArray(new int[] {1, 2, 3}));
 assertTrue(Equality.isArray(new String[] {"foo", "bar"}));
 assertFalse(Equality.isArray(new Integer(1)));
 assertFalse(Equality.isArray("foo"));
 }
}