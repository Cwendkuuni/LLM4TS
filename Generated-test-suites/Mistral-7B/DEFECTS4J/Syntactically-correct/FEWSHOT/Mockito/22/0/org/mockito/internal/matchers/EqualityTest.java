package org.mockito.internal.matchers;

import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqual_null_null() {
 Assert.assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqual_null_notNull() {
 Assert.assertFalse(Equality.areEqual(null, new Object()));
 }

 @Test
 public void testAreEqual_notNull_null() {
 Assert.assertFalse(Equality.areEqual(new Object(), null));
 }

 @Test
 public void testAreEqual_notNull_notNull() {
 Assert.assertTrue(Equality.areEqual(new Object(), new Object()));
 }

 @Test
 public void testAreEqual_differentTypes() {
 Assert.assertFalse(Equality.areEqual(new Object(), new String()));
 }

 @Test
 public void testAreArraysEqual_null_null() {
 Assert.assertTrue(Equality.areArraysEqual(null, null));
 }

 @Test
 public void testAreArraysEqual_null_notNull() {
 Assert.assertFalse(Equality.areArraysEqual(null, new Object[]{}));
 }

 @Test
 public void testAreArraysEqual_notNull_null() {
 Assert.assertFalse(Equality.areArraysEqual(new Object[]{}, null));
 }

 @Test
 public void testAreArraysEqual_differentLengths() {
 Object[] array1 = new Object[]{1, 2};
 Object[] array2 = new Object[]{1, 2, 3};
 Assert.assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_sameLengths_equalElements() {
 Object[] array1 = new Object[]{1, 2};
 Object[] array2 = new Object[]{1, 2};
 Assert.assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_sameLengths_differentElements() {
 Object[] array1 = new Object[]{1, 2};
 Object[] array2 = new Object[]{1, 3};
 Assert.assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testIsArray_null() {
 Assert.assertFalse(Equality.isArray(null));
 }

 @Test
 public void testIsArray_notArray() {
 Assert.assertFalse(Equality.isArray(new Object()));
 }

 @Test
 public void testIsArray_array() {
 Object[] array = new Object[]{1};
 Assert.assertTrue(Equality.isArray(array));
 }
}