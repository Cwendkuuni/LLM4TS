package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/test/name");
 assertTrue(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/test/name", pointer.toString());
 }

 @Test
 public void testPointerWithIndex() {
 JsonPointer pointer = JsonPointer.compile("/test/0");
 assertFalse(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals("/test/0", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer pointer = JsonPointer.compile("test");
 fail("Expected IllegalArgumentException but got: " + pointer);
 }

 @Test
 public void testPointerWithMultipleSegments() {
 JsonPointer pointer = JsonPointer.compile("/test/name/age/0");
 assertFalse(pointer.matches());
 assertEquals("name", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals("/test/name/age/0", pointer.toString());
 }

 @Test
 public void testPointerWithQuotedSegment() {
 JsonPointer pointer = JsonPointer.compile("/test/name/age/~1o");
 assertFalse(pointer.matches());
 assertEquals("age", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals("/test/name/age/~1o", pointer.toString());
 }

 @Test
 public void testPointerWithInvalidSegment() {
 JsonPointer pointer = JsonPointer.compile("/test/name/age/~x");
 fail("Expected IllegalArgumentException but got: " + pointer);
 }

 @Test
 public void testPointerWithEmptySegments() {
 JsonPointer pointer = JsonPointer.compile("/test/");
 assertTrue(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/test", pointer.toString());
 }

 @Test
 public void testPointerWithTrailingSlash() {
 JsonPointer pointer = JsonPointer.compile("/test/");
 assertTrue(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/test/", pointer.toString());
 }
}