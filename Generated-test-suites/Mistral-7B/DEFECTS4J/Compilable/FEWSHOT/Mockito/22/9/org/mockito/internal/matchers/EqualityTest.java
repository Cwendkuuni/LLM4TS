package org.mockito.internal.matchers;

import static org.junit.Assert.*;

import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqualNull() {
 assertFalse(Equality.areEqual(null, null));
 assertFalse(Equality.areEqual(null, new Object()));
 assertFalse(Equality.areEqual(new Object(), null));
 }

 @Test
 public void testAreEqualObjects() {
 assertTrue(Equality.areEqual(new Object(), new Object()));
 assertTrue(Equality.areEqual("foo", "foo"));
 assertTrue(Equality.areEqual(1, 1));
 }

 @Test
 public void testAreEqualArrays() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 Object[] array3 = new Object[]{1, 2, 3, 4};

 assertTrue(Equality.areEqual(array1, array2));
 assertFalse(Equality.areEqual(array1, array3));
 }

 @Test
 public void testAreEqualArraysDifferentLengths() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2};

 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysSameElementsDifferentOrder() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{2, 3, 1};

 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysSameElementsSameOrder() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};

 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqualArraysDifferentTypes() {
 Object[] array1 = new Object[]{1, 2, 3};
 String[] array2 = new String[]{"1", "2", "3"};

 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testIsArray() {
 assertTrue(Equality.isArray((Object[]) null));
 assertTrue(Equality.isArray(new int[0]));
 assertTrue(Equality.isArray(new String[0]));
 assertFalse(Equality.isArray(new Object()));
 }
}