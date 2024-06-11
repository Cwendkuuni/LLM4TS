package org.mockito.internal.matchers;

import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqual() {
 Object obj1 = new Object() {};
 Object obj2 = new Object() {};
 Object obj3 = obj1;

 // null vs null
 Assert.assertTrue(Equality.areEqual(null, null));

 // null vs non-null
 Assert.assertFalse(Equality.areEqual(null, obj1));

 // non-null vs null
 Assert.assertFalse(Equality.areEqual(obj1, null));

 // primitive types
 Assert.assertTrue(Equality.areEqual(1, 1));
 Assert.assertFalse(Equality.areEqual(1, 2));

 // objects
 Assert.assertTrue(Equality.areEqual(obj1, obj3));
 Assert.assertFalse(Equality.areEqual(obj1, new Object() {}));
 }

 @Test
 public void testAreArraysEqual() {
 Object[] arr1 = new Object[] {1, 2, 3};
 Object[] arr2 = new Object[] {1, 2, 3};
 Object[] arr3 = new Object[] {"one", "two", "three"};
 Object[] arr4 = new Object[] {"one", "three", "two"};

 // same arrays
 Assert.assertTrue(Equality.areArraysEqual(arr1, arr1));

 // arrays of different lengths
 Assert.assertFalse(Equality.areArraysEqual(arr1, arr3));

 // arrays with equal elements
 Assert.assertTrue(Equality.areArraysEqual(arr1, arr2));

 // arrays with different elements
 Assert.assertFalse(Equality.areArraysEqual(arr1, arr4));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 Object[] arr1 = new Object[3];
 Object[] arr2 = new Object[5];
 Object[] arr3 = new Object[3];

 // same length
 Assert.assertTrue(Equality.areArrayLengthsEqual(arr1, arr3));

 // different lengths
 Assert.assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
 }

 @Test
 public void testAreArrayElementsEqual() {
 Object[] arr1 = new Object[] {1, 2, 3};
 Object[] arr2 = new Object[] {1, 2, 3};
 Object[] arr3 = new Object[] {"one", "two", "three"};
 Object[] arr4 = new Object[] {"one", "three", "two"};

 // same array elements
 Assert.assertTrue(Equality.areArrayElementsEqual(arr1, arr2));

 // different array elements
 Assert.assertFalse(Equality.areArrayElementsEqual(arr1, arr4));
 }
}