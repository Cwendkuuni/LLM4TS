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
 assertTrue(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualArraysOfSameSizeAndSameElements() {
 Object[] array1 = new Object[]{new Object(), new Object()};
 Object[] array2 = new Object[]{new Object(), new Object()};
 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysOfSameSizeButDifferentElements() {
 Object[] array1 = new Object[]{new Object(), new Object()};
 Object[] array2 = new Object[]{new Object(), new Object(), new Object()};
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysOfDifferentSizesButEqualElements() {
 Object[] array1 = new Object[]{new Object()};
 Object[] array2 = new Object[]{new Object(), new Object()};
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysOfDifferentTypesButEqualElements() {
 Object[] array1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] array2 = new Object[]{new Integer(1), new Integer(2)};
 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysOfDifferentTypesButNotEqualElements() {
 Object[] array1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] array2 = new Object[]{new Integer(1), new Float(2)};
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualNulls() {
 assertNull(Equality.areArraysEqual(null, null));
 }

 @Test
 public void testAreArraysEqualArraysOfSameSizeAndSameElements() {
 Object[] array1 = new Object[]{new Object(), new Object()};
 Object[] array2 = new Object[]{new Object(), new Object()};
 assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysOfSameSizeButDifferentElements() {
 Object[] array1 = new Object[]{new Object(), new Object()};
 Object[] array2 = new Object[]{new Object(), new Object(), new Object()};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysOfDifferentSizesButEqualElements() {
 Object[] array1 = new Object[]{new Object()};
 Object[] array2 = new Object[]{new Object(), new Object()};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysOfDifferentTypesButEqualElements() {
 Object[] array1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] array2 = new Object[]{new Integer(1), new Integer(2)};
 assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysOfDifferentTypesButNotEqualElements() {
 Object[] array1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] array2 = new Object[]{new Integer(1), new Float(2)};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testIsArrayArray() {
 assertTrue(Equality.isArray(new Object[]{}));
 assertTrue(Equality.isArray(new int[]{1, 2}));
 }

 @Test
 public void testIsArrayNull() {
 assertFalse(Equality.isArray(null));
 }

 @Test
 public void testAreArrayLengthsEqualNulls() {
 assertNull(Equality.areArrayLengthsEqual(null, null));
 }

 @Test
 public void testAreArrayLengthsEqualArraysOfSameLength() {
 Object[] array1 = new Object[2];
 Object[] array2 = new Object[2];
 assertTrue(Equality.areArrayLengthsEqual(array1, array2));
 }

 @Test
 public void testAreArrayLengthsEqualArraysOfDifferentLengths() {
 Object[] array1 = new Object[2];
 Object[] array2 = new Object[3];
 assertFalse(Equality.areArrayLengthsEqual(array1, array2));
 }

 @Test
 public void testAreArrayElementsEqualNulls() {
 assertNull(Equality.areArrayElementsEqual(null, null));
 }

 @Test
 public void testAreArrayElementsEqualArraysOfSameSizeAndSameElements() {
 Object[] array1 = new Object[]{new Object(), new Object()};
 Object[] array2 = new Object[]{new Object(), new Object()};
 assertTrue(Equality.areArrayElementsEqual(array1, array2));
 }

 @Test
 public void testAreArrayElementsEqualArraysOfSameSizeButDifferentElements() {
 Object[] array1 = new Object[]{new Object(), new Object()};
 Object[] array2 = new Object[]{new Object(), new Object(), new Object()};
 assertFalse(Equality.areArrayElementsEqual(array1, array2));
 }

 @Test
 public void testAreArrayElementsEqualArraysOfDifferentSizesButEqualElements() {
 Object[] array1 = new Object[]{new Object()};
 Object[] array2 = new Object[]{new Object(), new Object()};
 assertFalse(Equality.areArrayElementsEqual(array1, array2));
 }

 @Test
 public void testAreArrayElementsEqualArraysOfDifferentTypesButEqualElements() {
 Object[] array1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] array2 = new Object[]{new Integer(1), new Integer(2)};
 assertTrue(Equality.areArrayElementsEqual(array1, array2));
 }

 @Test
 public void testAreArrayElementsEqualArraysOfDifferentTypesButNotEqualElements() {
 Object[] array1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] array2 = new Object[]{new Integer(1), new Float(2)};
 assertFalse(Equality.areArrayElementsEqual(array1, array2));
 }
}