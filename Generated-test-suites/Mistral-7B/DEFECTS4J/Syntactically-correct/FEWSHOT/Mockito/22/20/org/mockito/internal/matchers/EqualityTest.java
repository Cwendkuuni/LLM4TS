package org.mockito.internal.matchers;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EqualityTest {

 @Test
 public void testAreEqual() {
 Object o1 = new Object();
 Object o2 = new Object();
 Object o3 = o1;

 assertTrue(Equality.areEqual(o1, o1));
 assertTrue(Equality.areEqual(o1, o3));
 assertFalse(Equality.areEqual(o1, null));
 assertFalse(Equality.areEqual(o1, new Object()[0]));
 assertFalse(Equality.areEqual(new int[]{1, 2}, new int[]{1, 3}));
 }

 @Test
 public void testAreEqual_Nulls() {
 assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqual_Arrays() {
 Object[] o1 = new Object[]{new Object(), new Object()};
 Object[] o2 = new Object[]{new Object(), new Object()};
 Object[] o3 = new Object[]{new Object(), new Object(), new Object()};
 Object[] o4 = new Object[]{new Object(), null};

 assertTrue(Equality.areEqual(o1, o1));
 assertTrue(Equality.areEqual(o1, o2));
 assertFalse(Equality.areEqual(o1, null));
 assertFalse(Equality.areEqual(o1, o3));
 assertFalse(Equality.areEqual(o1, o4));
 }

 @Test
 public void testAreArraysEqual() {
 Object[] o1 = new Object[]{new Object(), new Object()};
 Object[] o2 = new Object[]{new Object(), new Object()};
 Object[] o3 = new Object[]{new Object(), new Object(), new Object()};
 Object[] o4 = new Object[]{new Object(), null};
 Object[] o5 = new Object[]{new Object(), new int[0]};

 assertTrue(Equality.areArraysEqual(o1, o1));
 assertTrue(Equality.areArraysEqual(o1, o2));
 assertFalse(Equality.areArraysEqual(o1, null));
 assertFalse(Equality.areArraysEqual(o1, o3));
 assertFalse(Equality.areArraysEqual(o1, o4));
 assertFalse(Equality.areArraysEqual(o1, o5));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 Object[] o1 = new Object[]{new Object(), new Object()};
 Object[] o2 = new Object[]{new Object(), new Object()};
 Object[] o3 = new Object[]{new Object(), new Object(), new Object()};
 Object[] o4 = new Object[]{new Object(), null};
 Object[] o5 = new Object[]{new Object(), new int[0]};

 assertTrue(Equality.areArrayLengthsEqual(o1, o1));
 assertTrue(Equality.areArrayLengthsEqual(o1, o2));
 assertFalse(Equality.areArrayLengthsEqual(o1, null));
 assertFalse(Equality.areArrayLengthsEqual(o1, o3));
 assertFalse(Equality.areArrayLengthsEqual(o1, o4));
 assertFalse(Equality.areArrayLengthsEqual(o1, o5));
 }

 @Test
 public void testAreArrayElementsEqual() {
 Object[] o1 = new Object[]{new Object(), new Object()};
 Object[] o2 = new Object[]{new Object(), new Object()};
 Object[] o3 = new Object[]{new Object(), new Object(), new Object()};
 Object[] o4 = new Object[]{new Object(), null};
 Object[] o5 = new Object[]{new Object(), new int[0]};
 Object[] o6 = new Object[]{new Object(), new Object(), new Object(), new Object()};

 assertTrue(Equality.areArrayElementsEqual(o1, o1));
 assertTrue(Equality.areArrayElementsEqual(o1, o2));
 assertFalse(Equality.areArrayElementsEqual(o1, null));
 assertFalse(Equality.areArrayElementsEqual(o1, o3));
 assertFalse(Equality.areArrayElementsEqual(o1, o4));
 assertFalse(Equality.areArrayElementsEqual(o1, o5));
 assertTrue(Equality.areArrayElementsEqual(o6, o6));
 }

 @Test
 public void testIsArray() {
 assertFalse(Equality.isArray(null));
 assertFalse(Equality.isArray(new Object()));
 assertTrue(Equality.isArray(new int[0]));
 assertTrue(Equality.isArray(new int[0][]));
 }
}