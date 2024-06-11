package com.fasterxml.jackson.core;
import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchElement());
 assertFalse(pointer.mayMatchProperty());
 assertEquals("/foo/bar", pointer.toString());
 }

 @Test
 public void testPointerWithElement() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/foo/0", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer pointer = JsonPointer.compile("~foo");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerWithSlash() {
 JsonPointer pointer = JsonPointer.compile("foo/");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testPointerWithQuotedSegment() {
 JsonPointer pointer = JsonPointer.compile("/foo/~0/baz");
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/foo/~0/baz", pointer.toString());
 }

 @Test
 public void testPointerTail() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/bar/baz");
 JsonPointer pointer2 = pointer1.tail();
 assertEquals("/bar/baz", pointer2.toString());
 }

 @Test
 public void testPointerMatchProperty() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 JsonPointer propertyPointer = JsonPointer.compile("/foo/bar/baz");
 JsonPointer matchingPointer = pointer.matchProperty("bar");
 assertEquals(propertyPointer, matchingPointer);
 }

 @Test
 public void testPointerMatchElement() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 JsonPointer elementPointer = JsonPointer.compile("/foo/1");
 JsonPointer matchingPointer = pointer.matchElement(0);
 assertEquals(elementPointer, matchingPointer);
 }

 @Test
 public void testPointerNull() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertNull(pointer.tail());
 }
}