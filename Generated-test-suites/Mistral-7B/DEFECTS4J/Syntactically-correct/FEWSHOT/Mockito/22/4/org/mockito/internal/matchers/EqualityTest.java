package org.mockito.internal.matchers;

import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqual() {
 // null checks
 Assert.assertFalse(Equality.areEqual(null, new Object()));
 Assert.assertFalse(Equality.areEqual(new Object(), null));
 // primitives
 Assert.assertTrue(Equality.areEqual(1, 1));
 Assert.assertFalse(Equality.areEqual(1, "1"));
 // objects
 Assert.assertTrue(Equality.areEqual(new Object(), new Object()));
 // arrays
 Object[] arr1 = new Object[]{1, 2, 3};
 Object[] arr2 = new Object[]{1, 2, 3};
 Assert.assertTrue(Equality.areEqual(arr1, arr2));
 // arrays with different lengths
 Object[] arr3 = new Object[]{1, 2, 3, 4};
 Object[] arr4 = new Object[]{1, 2, 3};
 Assert.assertFalse(Equality.areEqual(arr3, arr4));
 // arrays with different elements
 Object[] arr5 = new Object[]{1, 2, 3};
 Object[] arr6 = new Object[]{1, 2, 4};
 Assert.assertFalse(Equality.areEqual(arr5, arr6));
 }

 @Test
 public void testAreArraysEqual() {
 // null checks
 Assert.assertFalse(Equality.areArraysEqual(null, new Object[]{}));
 Assert.assertFalse(Equality.areArraysEqual(new Object[]{}, null));
 // arrays with same length and elements
 Object[] arr1 = new Object[]{1, 2, 3};
 Object[] arr2 = new Object[]{1, 2, 3};
 Assert.assertTrue(Equality.areArraysEqual(arr1, arr2));
 // arrays with different lengths
 Object[] arr3 = new Object[]{1, 2, 3, 4};
 Object[] arr4 = new Object[]{1, 2, 3};
 Assert.assertFalse(Equality.areArraysEqual(arr3, arr4));
 // arrays with different elements
 Object[] arr5 = new Object[]{1, 2, 3};
 Object[] arr6 = new Object[]{1, 2, 4};
 Assert.assertFalse(Equality.areArraysEqual(arr5, arr6));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 // null checks
 Assert.assertFalse(Equality.areArrayLengthsEqual(null, new Object[]{}));
 Assert.assertFalse(Equality.areArrayLengthsEqual(new Object[]{}, null));
 // arrays with same length
 Object[] arr1 = new Object[]{1, 2, 3};
 Object[] arr2 = new Object[]{1, 2, 3};
 Assert.assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
 // arrays with different lengths
 Object[] arr3 = new Object[]{1, 2, 3, 4};
 Object[] arr4 = new Object[]{1, 2, 3};
 Assert.assertFalse(Equality.areArrayLengthsEqual(arr3, arr4));
 }

 @Test
 public void testAreArrayElementsEqual() {
 // null checks
 Assert.assertFalse(Equality.areArrayElementsEqual(null, new Object[]{1, 2, 3}));
 Assert.assertFalse(Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, null));
 // arrays with same elements
 Object[] arr1 = new Object[]{1, 2, 3};
 Object[] arr2 = new Object[]{1, 2, 3};
 Assert.assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
 // arrays with different elements
 Object[] arr3 = new Object[]{1, 2, 3};
 Object[] arr4 = new Object[]{1, 2, 4};
 Assert.assertFalse(Equality.areArrayElementsEqual(arr3, arr4));
 }

 @Test
 public void testIsArray() {
 // negative tests
 Assert.assertFalse(Equality.isArray(new String("abc")));
 Assert.assertFalse(Equality.isArray(123));
 Assert.assertFalse(Equality.isArray(new Object()));
 // positive tests
 Object[] arr = new Object[0];
 Assert.assertTrue(Equality.isArray(arr));
 Object[] arr1 = new Object[]{1, 2, 3};
 Assert.assertTrue(Equality.isArray(arr1));
}
}