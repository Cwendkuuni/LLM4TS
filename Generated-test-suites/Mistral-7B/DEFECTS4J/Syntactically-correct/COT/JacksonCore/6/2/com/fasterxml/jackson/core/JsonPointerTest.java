package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 // Basic test cases for the constructor methods
 @Test
 public void testEmptyConstructor() {
 JsonPointer jsonPointer = new JsonPointer();
 assertEquals("", jsonPointer.toString());
 assertNull(jsonPointer.tail());
 assertFalse(jsonPointer.matches());
 }

 @Test
 public void testConstructorWithSegments() {
 String input = "/a/b/c";
 JsonPointer jsonPointer = new JsonPointer(input, "b", new JsonPointer("/d/e"));
 assertEquals("/a/b/c", jsonPointer.toString());
 assertEquals("/d/e", jsonPointer.tail().toString());
 assertFalse(jsonPointer.matches());
 }

 // Test case for the compile factory method
 @Test
 public void testCompileMethod() throws IllegalArgumentException {
 String input = "/a/b/c";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals("/a/b/c", jsonPointer.toString());
 assertEquals("/d/e", jsonPointer.tail().toString());
 assertFalse(jsonPointer.matches());
 }

 // Test case for the mayMatchProperty and mayMatchElement methods
 @Test
 public void testMayMatchPropertyAndElement() {
 JsonPointer jsonPointer = new JsonPointer("/a/b/c");
 assertTrue(jsonPointer.mayMatchProperty());
 assertTrue(jsonPointer.mayMatchElement());
 }

 JsonPointer jsonPointer1 = new JsonPointer("/a");
 JsonPointer jsonPointer2 = new JsonPointer("/a/b");
 JsonPointer jsonPointer3 = new JsonPointer("/a/b/c");

 // Test cases for the matchProperty and matchElement methods
 @Test
 public void testMatchProperty() {
 assertTrue(jsonPointer1.matchProperty("a").matches());
 assertNull(jsonPointer2.matchProperty("a"));
 assertNull(jsonPointer3.matchProperty("a"));
 }

 @Test
 public void testMatchElement() {
 // No matching index for empty JsonPointer
 assertNull(jsonPointer1.matchElement(0));

 // Matching index for JsonPointer with index
 assertEquals(0, jsonPointer3.matchElement(0).getMatchingIndex());
 assertNull(jsonPointer3.matchElement(1));
 }

 // Test case for the toString method
 @Test
 public void testToString() {
 JsonPointer jsonPointer = new JsonPointer("/a/b/c");
 assertEquals("/a/b/c", jsonPointer.toString());
 }

 // Test case for the hashCode and equals methods
 @Test
 public void testHashCodeAndEquals() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer2 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer3 = new JsonPointer("/a/d/e");
 JsonPointer jsonPointer4 = jsonPointer1;

 assertEquals(jsonPointer1.hashCode(), jsonPointer4.hashCode());
 assertTrue(jsonPointer1.equals(jsonPointer4));
 assertFalse(jsonPointer1.equals(jsonPointer2));
 assertFalse(jsonPointer1.equals(jsonPointer3));
 }

 // Test cases for the parseIndex and parseTail methods (internal methods)
 @Test(expected = IllegalArgumentException.class)
 public void testParseIndexInvalidInput() {
 JsonPointer.parseIndex("invalidInput");
 }

 @Test
 public void testParseIndexValidInput() {
 assertEquals(0, JsonPointer._parseIndex("0"));
 assertEquals(10, JsonPointer._parseIndex("10"));
 assertEquals(-1, JsonPointer._parseIndex("11"));
 assertEquals(-1, JsonPointer._parseIndex("xyz"));
 }

 @Test
 public void testParseTail() {
 String input = "/a/b/c/d";
 JsonPointer jsonPointer = JsonPointer._parseTail(input);
 assertEquals("/d", jsonPointer.toString());

 input = "/a/b/c/d~1";
 jsonPointer = JsonPointer._parseTail(input);
 assertEquals("/d~1", jsonPointer.toString());
 }

 // Test cases for the parseQuotedTail method (internal method)
 @Test
 public void testParseQuotedTail() {
 String input = "/a/b/c/d~1~2";
 JsonPointer jsonPointer = JsonPointer._parseQuotedTail(input, 3);
 assertEquals("/d~1", jsonPointer.toString());
 }

 // Test case for the appendEscape method (internal method)
 @Test
 public void testAppendEscape() {
 StringBuilder sb = new StringBuilder();
 JsonPointer.appendEscape(sb, '0');
 assertEquals("~", sb.toString());

 sb.setLength(0);
 JsonPointer.appendEscape(sb, '1');
 assertEquals("/", sb.toString());
 }

 // Test cases for the fromSegment factory method (TODO)
 // Uncomment the code below when the fromSegment factory method is implemented
 /*
 @Test
 public void testFromSegment() {
 String[] segments = new String[]{"/a", "/b", "/c"};
 JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
 assertEquals("/a/b/c", jsonPointer.toString());
 assertEquals("/", jsonPointer.tail().toString());
 }
 */
}