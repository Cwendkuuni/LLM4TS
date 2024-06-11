package org.mockito.internal.matchers;

import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqual() {
 // Test null cases
 Assert.assertFalse(Equality.areEqual(null, null));
 Assert.assertFalse(Equality.areEqual(null, new Object()));
 Assert.assertFalse(Equality.areEqual(new Object(), null));

 // Test basic types
 Assert.assertTrue(Equality.areEqual(1, 1));
 Assert.assertFalse(Equality.areEqual(1, 2));

 // Test String cases
 Assert.assertTrue(Equality.areEqual("foo", "foo"));
 Assert.assertFalse(Equality.areEqual("foo", "bar"));

 // Test Arrays cases
 Object[] arr1 = {1, 2, 3};
 Object[] arr2 = {1, 2, 3};
 Object[] arr3 = {1, 2, 4};

 Assert.assertTrue(Equality.areEqual(arr1, arr2));
 Assert.assertFalse(Equality.areEqual(arr1, arr3));
 }

 @Test
 public void testAreArraysEqual() {
 // Test arrays of different lengths
 Object[] arr1 = {1, 2, 3};
 Object[] arr2 = {1, 2};

 Assert.assertFalse(Equality.areArraysEqual(arr1, arr2));

 // Test arrays with the same length and same elements
 Object[] arr3 = {1, 2, 3};
 Object[] arr4 = {1, 2, 3};

 Assert.assertTrue(Equality.areArraysEqual(arr3, arr4));

 // Test arrays with the same length but different elements
 Object[] arr5 = {1, 2, 3};
 Object[] arr6 = {1, 2, 4};

 Assert.assertFalse(Equality.areArraysEqual(arr5, arr6));
 }

 @Test
 public void testAreArrayElementsEqual() {
 // Test single element arrays
 Object[] arr1 = {1};
 Object[] arr2 = {1};

 Assert.assertTrue(Equality.areArrayElementsEqual(arr1, arr2));

 // Test arrays with different elements
 Object[] arr3 = {1, 2};
 Object[] arr4 = {1, 3};

 Assert.assertFalse(Equality.areArrayElementsEqual(arr3, arr4));
 }

 @Test
 public void testIsArray() {
 Object[] arr1 = new Object[0];
 Object[] arr2 = new int[0][];

 Assert.assertTrue(Equality.isArray(arr1));
 Assert.assertTrue(Equality.isArray(arr2));
 }
}