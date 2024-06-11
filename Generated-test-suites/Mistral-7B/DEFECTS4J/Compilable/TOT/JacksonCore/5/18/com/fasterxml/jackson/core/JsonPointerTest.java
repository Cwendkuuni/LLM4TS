package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/foo");
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/foo", pointer.toString());
 }

 @Test
 public void testPointerWithProperty() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals(JsonPointer.EMPTY, pointer.tail());
 assertEquals("/foo/bar", pointer.toString());
 }

 @Test
 public void testPointerWithIndex() {
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile("/foo/0");
 } catch (IllegalArgumentException e) {
 fail("Illegal argument exception not thrown for valid pointer: " + e.getMessage());
 }
 assertTrue(pointer.matches());
 assertEquals("0", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("/foo/0", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer pointer = null;
 pointer = JsonPointer.compile("bar");
 fail("Illegal argument exception not thrown for invalid input: " + pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSegmentStart() {
 JsonPointer pointer = null;
 pointer = JsonPointer.compile("~/foo");
 fail("Illegal argument exception not thrown for invalid segment start: " + pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEscape() {
 JsonPointer pointer = null;
 pointer = JsonPointer.compile("/foo/~1");
 fail("Illegal argument exception not thrown for invalid escape: " + pointer.toString());
 }
}