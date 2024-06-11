package org.mockito.internal.matchers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqual_ObjectsAreEqual() {
 assertTrue(Equality.areEqual(new Object(), new Object()));
 }

 @Test
 public void testAreEqual_ObjectsAreNotEqual_Null() {
 assertFalse(Equality.areEqual(null, null));
 assertFalse(Equality.areEqual(null, new Object()));
 assertFalse(Equality.areEqual(new Object(), null));
 }

 @Test
 public void testAreEqual_ObjectsAreNotEqual_DifferentClasses() {
 assertFalse(Equality.areEqual(new Object(), new String("foo")));
 }

 @Test
 public void testAreEqual_ArraysAreEqual() {
 Object[] array1 = new Object[] {new Integer(1), new Integer(2)};
 Object[] array2 = new Object[] {new Integer(1), new Integer(2)};
 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqual_ArraysAreNotEqual_DifferentLengths() {
 Object[] array1 = new Object[] {new Integer(1), new Integer(2)};
 Object[] array2 = new Object[] {new Integer(1), new Integer(2), new Integer(3)};
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqual_ArraysAreNotEqual_DifferentElements() {
 Object[] array1 = new Object[] {new Integer(1), new Integer(2)};
 Object[] array2 = new Object[] {new Integer(1), new Integer(3)};
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqual_ArraysAreNotEqual_NullArray() {
 Object[] array1 = new Object[] {new Integer(1), new Integer(2)};
 assertFalse(Equality.areEqual(array1, null));
 }

 @Test
 public void testAreArraysEqual_ArraysAreEqual() {
 Object[] array1 = new Object[] {new Integer(1), new Integer(2)};
 Object[] array2 = new Object[] {new Integer(1), new Integer(2)};
 assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_ArraysAreNotEqual_DifferentLengths() {
 Object[] array1 = new Object[] {new Integer(1), new Integer(2)};
 Object[] array2 = new Object[] {new Integer(1), new Integer(2), new Integer(3)};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_ArraysAreNotEqual_DifferentElements() {
 Object[] array1 = new Object[] {new Integer(1), new Integer(2)};
 Object[] array2 = new Object[] {new Integer(1), new Integer(3)};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testIsArray_IsArray() {
 Object[] array = new Object[0];
 assertTrue(Equality.isArray(array));
 }

 @Test
 public void testIsArray_IsNotArray() {
 String str = "foo";
 assertFalse(Equality.isArray(str));
 }
}