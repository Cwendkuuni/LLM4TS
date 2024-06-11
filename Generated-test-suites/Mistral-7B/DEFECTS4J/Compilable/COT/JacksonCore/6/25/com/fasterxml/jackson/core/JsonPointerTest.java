package com.fasterxml.jackson.core;
import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

 private JsonPointer emptyPointer = JsonPointer.EMPTY;
 private JsonPointer singleSegmentPointer;
 private JsonPointer multiSegmentPointer;
 private JsonPointer invalidInputPointer;

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = emptyPointer;
 Assert.assertTrue(pointer.matches());
 Assert.assertEquals("", pointer.getMatchingProperty());
 Assert.assertFalse(pointer.mayMatchProperty());
 Assert.assertFalse(pointer.mayMatchElement());
 Assert.assertNull(pointer.tail());
 Assert.assertEquals("", pointer.toString());
 }

 @Test
 public void testSingleSegmentPointer() {
 String input = "/foo";
 singleSegmentPointer = JsonPointer.compile(input);
 Assert.assertFalse(singleSegmentPointer.matches());
 Assert.assertEquals("foo", singleSegmentPointer.getMatchingProperty());
 Assert.assertTrue(singleSegmentPointer.mayMatchProperty());
 Assert.assertFalse(singleSegmentPointer.mayMatchElement());
 Assert.assertNull(singleSegmentPointer.tail());
 Assert.assertEquals(input, singleSegmentPointer.toString());
 }

 @Test
 public void testMultiSegmentPointer() {
 String input = "/foo/bar/baz";
 multiSegmentPointer = JsonPointer.compile(input);
 Assert.assertFalse(multiSegmentPointer.matches());
 Assert.assertEquals("foo", multiSegmentPointer.getMatchingProperty());
 Assert.assertTrue(multiSegmentPointer.mayMatchProperty());
 Assert.assertFalse(multiSegmentPointer.mayMatchElement());
 Assert.assertEquals("/bar/baz", multiSegmentPointer.tail().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputPointer() {
 String input = "~bar";
 invalidInputPointer = JsonPointer.compile(input);
 }

 @Test
 public void testMatchProperty() {
 JsonPointer propertyPointer = new JsonPointer("/foo/bar", "bar", null);
 JsonPointer testPointer = singleSegmentPointer;
 Assert.assertNull(propertyPointer.matchProperty(null));
 Assert.assertNull(propertyPointer.matchProperty("qux"));
 Assert.assertEquals(propertyPointer, propertyPointer.matchProperty("bar"));
 Assert.assertNull(testPointer.matchProperty("bar"));
 }

 @Test
 public void testMatchElement() {
 JsonPointer propertyPointer = new JsonPointer("/foo/0", "", null);
 JsonPointer testPointer = new JsonPointer("/foo/1", "", null);
 Assert.assertNull(propertyPointer.matchElement(-1));
 Assert.assertNull(propertyPointer.matchElement(0));
 Assert.assertEquals(propertyPointer, propertyPointer.matchElement(0));
 Assert.assertNull(testPointer.matchElement(-1));
 Assert.assertNull(testPointer.matchElement(0));
 Assert.assertNull(testPointer.matchElement(1));
 }

 @Test
 public void testTail() {
 JsonPointer propertyPointer = singleSegmentPointer;
 JsonPointer testPointer = multiSegmentPointer;
 Assert.assertNull(propertyPointer.tail());
 Assert.assertEquals(multiSegmentPointer, testPointer.tail());
 }

 @Test
 public void testEquals() {
 JsonPointer pointer1 = new JsonPointer("/foo/bar", "bar", null);
 JsonPointer pointer2 = new JsonPointer("/foo/bar", "bar", null);
 JsonPointer pointer3 = new JsonPointer("/foo/baz", "bar", null);
 JsonPointer pointer4 = new JsonPointer("/foo/bar", null, null);
 JsonPointer pointer5 = new JsonPointer("/foo/bar", "bar", multiSegmentPointer);
 Assert.assertEquals(pointer1, pointer1);
 Assert.assertEquals(pointer1, pointer2);
 Assert.assertNotEquals(pointer1, pointer3);
 Assert.assertNotEquals(pointer1, pointer4);
 Assert.assertNotEquals(pointer1, pointer5);
 }

 @Test
 public void testHashCode() {
 JsonPointer pointer1 = new JsonPointer("/foo/bar", "bar", null);
 JsonPointer pointer2 = new JsonPointer("/foo/bar", "bar", null);
 JsonPointer pointer3 = new JsonPointer("/foo/baz", "bar", null);
 JsonPointer pointer4 = new JsonPointer("/foo/bar", null, null);
 JsonPointer pointer5 = new JsonPointer("/foo/bar", "bar", multiSegmentPointer);
 Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
 Assert.assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
 Assert.assertNotEquals(pointer1.hashCode(), pointer4.hashCode());
 Assert.assertNotEquals(pointer1.hashCode(), pointer5.hashCode());
 }

 @Test
 public void testToString() {
 JsonPointer pointer1 = new JsonPointer("/foo/bar", "bar", null);
 JsonPointer pointer2 = new JsonPointer("/foo/baz", "bar", null);
 JsonPointer pointer3 = new JsonPointer("/foo/bar", null, null);
 JsonPointer pointer4 = new JsonPointer("/foo/bar", "bar", multiSegmentPointer);
 Assert.assertEquals("/foo/bar", pointer1.toString());
 Assert.assertEquals("/foo/baz", pointer2.toString());
 Assert.assertEquals("/foo", pointer3.toString());
 Assert.assertEquals("/foo/bar", pointer4.toString());
 }

}