package org.mockito.internal.matchers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Equality.areArrayElementsEqual;
import static org.mockito.internal.matchers.Equality.areArrayLengthsEqual;
import static org.mockito.internal.matchers.Equality.areEqual;
import static org.mockito.internal.matchers.Equality.isArray;

import org.junit.Test;

public class EqualityTest {

 @Test
 public void testAreEqual() {
 assertTrue(areEqual(1, 1));
 assertTrue(areEqual("foo", "foo"));
 assertTrue(areEqual(new int[]{1, 2}, new int[]{1, 2}));
 assertFalse(areEqual(1, 2));
 assertFalse(areEqual("foo", "bar"));
 assertFalse(areEqual(new int[]{1, 2}, new int[]{1, 3}));
 }

 @Test
 public void testAreArraysEqual() {
 assertArrayEquals(new int[]{1, 2}, new int[]{1, 2});
 assertArrayEquals(new String[]{"foo", "bar"}, new String[]{"foo", "bar"});
 assertFalse(new int[]{1, 2}. equals(new int[]{1, 3}));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 assertTrue(areArrayLengthsEqual(new int[]{1, 2}, new int[]{1, 2}));
 assertFalse(areArrayLengthsEqual(new int[]{1, 2}, new int[]{1, 3}));
 }

 @Test
 public void testIsArray() {
 assertTrue(isArray(new int[]{}));
 assertTrue(isArray(new String[]{}));
 assertFalse(isArray(1));
 assertFalse(isArray(""));
 }
}