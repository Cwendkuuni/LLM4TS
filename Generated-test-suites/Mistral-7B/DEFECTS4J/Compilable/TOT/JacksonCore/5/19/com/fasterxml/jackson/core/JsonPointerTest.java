package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertEquals("", emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.tail());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/foo");
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("/foo", pointer.toString());
 }

 @Test
 public void testPointerWithProperty() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/foo", pointer.toString());
 }

 @Test
 public void testPointerWithElement() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 assertFalse(pointer.matches());
 assertEquals("", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("/foo/0", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer pointer = JsonPointer.compile("invalid");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInputWithoutLeadingSlash() {
 JsonPointer pointer = JsonPointer.compile("foo");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testPointerWithQuotedSegment() {
 JsonPointer pointer = JsonPointer.compile("/foo/~0/bar");
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("/foo/~0/bar", pointer.toString());
 }

 @Test
 public void testPointerWithMultipleSegments() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
 assertFalse(pointer.matches());
 assertEquals("baz", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/foo/bar", pointer.toString());
 }

 @Test
 public void testPointerWithTrailingSlash() {
 JsonPointer pointer = JsonPointer.compile("/foo/");
 assertFalse(pointer.matches());
 assertEquals("", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("/foo/", pointer.toString());
 }

}