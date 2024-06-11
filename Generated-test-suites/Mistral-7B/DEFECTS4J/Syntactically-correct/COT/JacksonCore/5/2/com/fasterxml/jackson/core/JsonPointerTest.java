package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 private JsonPointer createJsonPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 fail("Input \"" + input + "\" should have been valid: " + e.getMessage());
 }
 }

 @Test
 public void testEmptyJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.EMPTY;
 assertTrue(jsonPointer.matches());
 assertEquals("", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
 }

 @Test
 public void testJsonPointerWithSingleSegment() {
 String input = "/foo";
 JsonPointer jsonPointer = createJsonPointer(input);
 assertFalse(jsonPointer.matches());
 assertEquals("foo", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerWithInvalidInput() {
 String input = "bar";
 createJsonPointer(input);
 }

 @Test
 public void testJsonPointerWithMultipleSegments() {
 String input = "/foo/bar";
 JsonPointer jsonPointer = createJsonPointer(input);
 assertFalse(jsonPointer.matches());
 assertEquals("foo", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 JsonPointer nextSegment = jsonPointer.tail();
 assertEquals("/bar", nextSegment.toString());
 }

 @Test
 public void testJsonPointerWithIndex() {
 String input = "/foo/0";
 JsonPointer jsonPointer = createJsonPointer(input);
 assertFalse(jsonPointer.matches());
 assertEquals("foo", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 JsonPointer nextSegment = jsonPointer.tail();
 assertEquals("/", nextSegment.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerWithNegativeIndex() {
 String input = "/foo/-1";
 createJsonPointer(input);
 }

 @Test
 public void testJsonPointerWithLongIndex() {
 String input = "/foo/1234567890";
 JsonPointer jsonPointer = createJsonPointer(input);
 assertFalse(jsonPointer.matches());
 assertEquals("foo", jsonPointer.getMatchingProperty());
 assertEquals(1234567890, jsonPointer.getMatchingIndex());
 JsonPointer nextSegment = jsonPointer.tail();
 assertEquals("/", nextSegment.toString());
 }

 @Test
 public void testJsonPointerWithQuotedSegments() {
 String input = "/foo/bar~1/baz~0";
 JsonPointer jsonPointer = createJsonPointer(input);
 assertFalse(jsonPointer.matches());
 assertEquals("bar", jsonPointer.getMatchingProperty());
 assertEquals(1, jsonPointer.getMatchingIndex());
 JsonPointer nextSegment = jsonPointer.tail();
 assertEquals("/baz", nextSegment.toString());
 }
}