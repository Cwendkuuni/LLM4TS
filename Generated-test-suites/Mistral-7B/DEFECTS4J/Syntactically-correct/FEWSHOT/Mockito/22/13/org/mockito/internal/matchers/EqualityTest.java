package org.mockito.internal.matchers;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Equality.areArrayElementsEqual;
import static org.mockito.internal.matchers.Equality.areArrayLengthsEqual;
import static org.mockito.internal.matchers.Equality.areArraysEqual;
import static org.mockito.internal.matchers.Equality.isArray;

public class EqualityTest {

 @Test
 public void testAreEqualNull() {
 assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqualDifferentTypes() {
 assertFalse(Equality.areEqual(new Object(), new Integer(1)));
 }

 @Test
 public void testAreEqualObjects() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 assertTrue(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualDifferentObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 assertFalse(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualArraysLengthsDifferent() {
 Object[] arr1 = new Object[1];
 Object[] arr2 = new Object[2];
 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysLengthsSame() {
 Object[] arr1 = new Object[1];
 Object[] arr2 = new Object[1];
 assertTrue(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysDifferentElements() {
 Object[] arr1 = new Object[]{1, 2};
 Object[] arr2 = new Object[]{1, 3};
 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysSameElements() {
 Object[] arr1 = new Object[]{1, 2};
 Object[] arr2 = new Object[]{1, 2};
 assertTrue(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysNull() {
 Object[] arr1 = null;
 Object[] arr2 = null;
 assertTrue(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysOneNull() {
 Object[] arr1 = new Object[]{1, 2};
 Object[] arr2 = new Object[]{1, null};
 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysBothNull() {
 Object[] arr1 = null;
 Object[] arr2 = null;
 assertTrue(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testIsArrayTrue() {
 Object[] arr = new Object[1];
 assertTrue(Equality.isArray(arr));
 }

 @Test
 public void testIsArrayFalse() {
 String str = new String("foo");
 assertFalse(Equality.isArray(str));
 }

 @Test
 public void testAreArraysEqualArraysNull() {
 Object[] arr1 = null;
 Object[] arr2 = null;
 assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqualArraysDifferentLengths() {
 Object[] arr1 = new Object[1];
 Object[] arr2 = new Object[2];
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqualArraysSameLengthsButDifferentElements() {
 Object[] arr1 = new Object[]{1, 2};
 Object[] arr2 = new Object[]{1, 3};
 assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqualArraysSameLengthsAndElements() {
 Object[] arr1 = new Object[]{1, 2};
 Object[] arr2 = new Object[]{1, 2};
 assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqualArraysDifferentObjectsButSameContent() {
 Object[] arr1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] arr2 = new Object[]{new Integer(1), new Integer(2)};
 assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqualArraysSameObjectsButDifferentArrays() {
 Object[] arr1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] arr2 = new Object[]{arr1[0], arr1[1]};
 assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqualArraysSameObjectsButDifferentReferences() {
 Object[] arr1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] arr2 = new Object[]{new Integer(1), new Integer(2)};
 Object[] arr3 = arr1;
 assertFalse(Equality.areArraysEqual(arr1, arr3));
 }
}