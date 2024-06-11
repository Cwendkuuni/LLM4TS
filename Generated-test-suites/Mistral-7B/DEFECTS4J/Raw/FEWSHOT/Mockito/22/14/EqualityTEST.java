package org.mockito.internal.matchers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqualNull() {
 final Object nullObject1 = null;
 final Object nullObject2 = null;
 assertTrue(Equality.areEqual(nullObject1, nullObject2));
 }

 @Test
 public void testAreEqualObjects() {
 final Object obj1 = new Object();
 final Object obj2 = new Object();
 assertFalse(Equality.areEqual(obj1, obj2));

 final Object obj1SameAsObj2 = obj2;
 assertTrue(Equality.areEqual(obj1, obj1SameAsObj2));
 }

 @Test
 public void testAreEqualArrays() {
 final Object[] array1 = new Object[] { new Object(), new Object() };
 final Object[] array2 = new Object[] { new Object(), new Object() };
 assertTrue(Equality.areEqual(array1, array2));

 final Object[] array1Prime = new Object[] { new Object(), new Object(), new Object() };
 assertFalse(Equality.areEqual(array1, array1Prime));
 }

 @Test
 public void testAreEqualArraysWithPrimitiveType() {
 final byte[] byteArray1 = new byte[] { 1, 2, 3 };
 final byte[] byteArray2 = new byte[] { 1, 2, 3 };
 assertTrue(Equality.areEqual(byteArray1, byteArray2));
 }

 @Test
 public void testAreEqualArraysWithDifferentLengths() {
 final Object[] array1 = new Object[] { new Object(), new Object() };
 final Object[] array2 = new Object[] { new Object(), new Object(), new Object() };
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithNullElements() {
 final Object[] array1 = new Object[] { new Object(), null, new Object() };
 final Object[] array2 = new Object[] { new Object(), new Object(), new Object() };
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithNullArray() {
 final Object[] array1 = new Object[] { new Object(), new Object() };
 assertFalse(Equality.areEqual(array1, null));
 }

 @Test
 public void testAreEqualArraysWithSameElementButDifferentOrder() {
 final Object[] array1 = new Object[] { new Object(), new Object() };
 final Object[] array2 = new Object[] { new Object(), new Object() };
 assertFalse(Equality.areEqual(array1, array2));

 final Object[] shuffledArray1 = new Object[] { array1[1], array1[0] };
 final Object[] shuffledArray2 = new Object[] { array2[1], array2[0] };
 assertTrue(Equality.areEqual(shuffledArray1, shuffledArray2));
 }

 @Test
 public void testAreEqualArraysWithArraysAsElements() {
 final Object[] array1 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3, 4 } };
 final Object[] array2 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3, 4 } };
 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithArraysAsElementsDifferentLength() {
 final Object[] array1 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3 } };
 final Object[] array2 = new Object[] { new Object[] { 1, 2, 3 } };
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithArraysAsElementsNullArray() {
 final Object[] array1 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3 } };
 final Object[] array2 = new Object[] { new Object[] { 1, 2 }, null };
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithArraysAsElementsNullElement() {
 final Object[] array1 = new Object[] { new Object[] { 1, 2, new Object[] { 3 } }, new Object[] { 4 } };
 final Object[] array2 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3, null } };
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithArraysAsElementsDifferentOrder() {
 final Object[] array1 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3 } };
 final Object[] array2 = new Object[] { new Object[] { 3 }, new Object[] { 1, 2 } };
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithArraysAsElementsDifferentArrayClass() {
 final Object array1[] = new Object[] { new int[1][], new int[1][] };
 final Object array2[] = new Object[] { new int[]{ new int[1][] }, new int[]{ new int[1][] } };
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysWithArraysAsElementsSameArrayInstance() {
 final Object[] array1 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3 } };
 final Object[] array2 = array1;
 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testArraysAreEqual() {
 final Object[] array1 = new Object[] { new Object(), new Object() };
 final Object[] array2 = new Object[] { new Object(), new Object() };
 assertTrue(Equality.areArraysEqual(array1, array2));

 final Object[] array1Prime = new Object[] { new Object(), new Object(), new Object() };
 assertFalse(Equality.areArraysEqual(array1, array1Prime));
 }

 @Test
 public void testArraysAreEqualWithPrimitiveType() {
 final byte[] byteArray1 = new byte[] { 1, 2, 3 };
 final byte[] byteArray2 = new byte[] { 1, 2, 3 };
 assertTrue(Equality.areArraysEqual(byteArray1, byteArray2));
 }

 @Test
 public void testArraysAreEqualWithNullArrays() {
 final Object[] array1 = new Object[] { new Object(), new Object() };
 assertFalse(Equality.areArraysEqual(array1, null));
 }

 @Test
 public void testArraysAreEqualWithNullElements() {
 final Object[] array1 = new Object[] { new Object(), null, new Object() };
 assertFalse(Equality.areArraysEqual(array1, new Object[] { new Object(), new Object(), new Object() }));
 }

 @Test
 public void testArraysAreEqualWithDifferentLengths() {
 final Object[] array1 = new Object[] { new Object(), new Object() };
 final Object[] array2 = new Object[] { new Object(), new Object(), new Object() };
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testArraysAreEqualWithArraysAsElements() {
 final Object[] array1 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3, 4 } };
 final Object[] array2 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3, 4 } };
 assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testArraysAreEqualWithArraysAsElementsDifferentOrder() {
 final Object[] array1 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3 } };
 final Object[] array2 = new Object[] { new Object[] { 3 }, new Object[] { 1, 2 } };
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testArraysAreEqualWithArraysAsElementsDifferentArrayClass() {
 final Object array1[] = new Object[] { new int[1][], new int[1][] };
 final Object array2[] = new Object[] { new int[]{ new int[1][] }, new int[]{ new int[1][] } };
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testArraysAreEqualWithArraysAsElementsSameElementButDifferentOrder() {
 final Object[] array1 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3 } };
 final Object[] array2 = new Object[] { new Object[] { 3 }, new Object[] { 1, 2 } };
 assertFalse(Equality.areArraysEqual(array1, array2));

 final Object[] shuffledArray1 = new Object[] { array1[1], array1[0] };
 final Object[] shuffledArray2 = new Object[] { array2[1], array2[0] };
 assertTrue(Equality.areArraysEqual(shuffledArray1, shuffledArray2));
 }

 @Test
 public void testArraysAreEqualWithArraysAsElementsSameInstance() {
 final Object[] array1 = new Object[] { new Object[] { 1, 2 }, new Object[] { 3 } };
 final Object[] array2 = array1;
 assertTrue(Equality.areArraysEqual(array1, array2));
 }
}