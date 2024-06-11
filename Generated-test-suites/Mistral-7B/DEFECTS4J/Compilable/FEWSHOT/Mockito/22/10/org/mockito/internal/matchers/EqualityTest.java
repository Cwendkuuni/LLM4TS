package org.mockito.internal.matchers;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualityTest {

 @Test
 public void testAreEqual() {
 int a = 1;
 int b = 1;
 float c = 1.0f;
 float d = 1.0f;
 Object e = new Object();
 Object f = new Object();
 String g = "test";
 String h = "test";
 Object[] i = new Object[]{a, b};
 Object[] j = new Object[]{a, b};

 assertTrue(Equality.areEqual(a, a));
 assertTrue(Equality.areEqual(a, b)); // primitives are compared by value
 assertTrue(Equality.areEqual(c, c));
 assertTrue(Equality.areEqual(c, d)); // floats are compared by value, not by bit representation
 assertTrue(Equality.areEqual(e, e));
 assertTrue(Equality.areEqual(e, f)); // objects are compared by reference
 assertTrue(Equality.areEqual(g, g));
 assertTrue(Equality.areEqual(g, h)); // strings are compared by content, not by reference

 assertFalse(Equality.areEqual(null, a));
 assertFalse(Equality.areEqual(a, null));
 assertFalse(Equality.areEqual(a, "test"));
 }

 @Test
 public void testAreArraysEqual() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 Object[] array3 = new Object[]{1, 2, 3, 4};
 Object[] array4 = new Object[]{2, 1, 3};

 assertTrue(Equality.areArraysEqual(array1, array1));
 assertTrue(Equality.areArraysEqual(array1, array2));
 assertFalse(Equality.areArraysEqual(array1, array3));
 assertFalse(Equality.areArraysEqual(array1, array4));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 Object[] array1 = new Object[3];
 Object[] array2 = new Object[2];
 Object[] array3 = new Object[3];

 assertTrue(Equality.areArrayLengthsEqual(array1, array1));
 assertFalse(Equality.areArrayLengthsEqual(array1, array2));
 assertTrue(Equality.areArrayLengthsEqual(array1, array3));
 }

 @Test
 public void testAreArrayElementsEqual() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 Object[] array3 = new Object[]{1, 2, 4};
 Object[] array4 = new Object[]{2, 1, 3};

 for (int i = 0; i < array1.length; i++) {
 assertTrue(Equality.areEqual(array1[i], array2[i]));
 }
 assertFalse(Equality.areArraysEqual(array1, array3));
 assertFalse(Equality.areArraysEqual(array1, array4));
 }

 @Test
 public void testIsArray() {
 int a = 1;
 float b = 1.0f;
 Object[] c = new Object[]{1, 2, 3};

 assertFalse(Equality.isArray(a));
 assertFalse(Equality.isArray(b));
 assertTrue(Equality.isArray(c));
 }
}