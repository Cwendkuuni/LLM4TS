package org.mockito.internal.matchers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Equality.areArrayElementsEqual;
import static org.mockito.internal.matchers.Equality.areArrayLengthsEqual;
import static org.mockito.internal.matchers.Equality.areArraysEqual;
import static org.mockito.internal.matchers.Equality.isArray;

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
 public void testAreEqualArrays() {
 Object[] arr1 = new Object[0];
 Object[] arr2 = new Object[0];
 assertTrue(Equality.areEqual(arr1, arr2));

 Object[] arr1a = {new Object(), new Object()};
 Object[] arr2a = {new Object(), new Object()};
 assertTrue(Equality.areEqual(arr1a, arr2a));

 Object[] arr1b = {new Object(), "foo"};
 Object[] arr2b = {new Object(), "foo"};
 assertTrue(Equality.areEqual(arr1b, arr2b));

 Object[] arr1c = {new Object(), new Object(), new Object()};
 Object[] arr2c = {new Object(), new Object(), new Object()};
 assertTrue(Equality.areEqual(arr1c, arr2c));

 Object[] arr1d = {new Object(), "foo"};
 Object[] arr2d = {new Object(), "bar"};
 assertFalse(Equality.areEqual(arr1d, arr2d));
 }

 @Test
 public void testAreEqualArraysLengthsNotEqual() {
 Object[] arr1 = new Object[3];
 Object[] arr2 = new Object[2];
 assertFalse(Equality.areEqual(arr1, arr2));
 }

 @Test
 public void testAreArraysEqual() {
 Object[] arr1 = new Object[0];
 Object[] arr2 = new Object[0];
 assertTrue(areArraysEqual(arr1, arr2));

 Object[] arr1a = {new Object(), new Object()};
 Object[] arr2a = {new Object(), new Object()};
 assertTrue(areArraysEqual(arr1a, arr2a));

 Object[] arr1b = {new Object(), "foo"};
 Object[] arr2b = {new Object(), "foo"};
 assertTrue(areArraysEqual(arr1b, arr2b));

 Object[] arr1c = {new Object(), new Object(), new Object()};
 Object[] arr2c = {new Object(), new Object(), new Object()};
 assertTrue(areArraysEqual(arr1c, arr2c));

 Object[] arr1d = {new Object(), "foo"};
 Object[] arr2d = {new Object(), "bar"};
 assertFalse(areArraysEqual(arr1d, arr2d));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 Object[] arr1 = new Object[3];
 Object[] arr2 = new Object[2];
 assertFalse(areArrayLengthsEqual(arr1, arr2));

 Object[] arr1a = new Object[3];
 Object[] arr2a = new Object[3];
 assertTrue(areArrayLengthsEqual(arr1a, arr2a));
 }

 @Test
 public void testIsArray() {
 assertTrue(isArray(new Object[0]));
 assertTrue(isArray(new int[0][0]));
 assertFalse(isArray(new Object()));
 }
}