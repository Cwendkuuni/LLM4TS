package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 // Test constructor for empty instance
 @Test
 public void testEmptyConstructor() {
 JsonPointer jsonPointer = new JsonPointer();
 assertTrue(jsonPointer.matches());
 assertEquals("", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
 assertEquals("", jsonPointer.toString());
 }

 // Test constructor for non-empty instance
 @Test
 public void testNonEmptyConstructor() {
 String fullString = "/a/b/c";
 String segment = "b";
 JsonPointer jsonPointer1 = new JsonPointer(fullString, segment, null);
 assertTrue(jsonPointer1.matches());
 assertEquals("b", jsonPointer1.getMatchingProperty());
 assertEquals(-1, jsonPointer1.getMatchingIndex());
 assertEquals(JsonPointer.EMPTY, jsonPointer1.tail());
 assertEquals("/a/b", jsonPointer1.toString());

 JsonPointer jsonPointer2 = jsonPointer1.tail();
 assertFalse(jsonPointer2.matches());
 assertEquals("c", jsonPointer2.getMatchingProperty());
 assertEquals(-1, jsonPointer2.getMatchingIndex());
 assertEquals(JsonPointer.EMPTY, jsonPointer2.tail());
 assertEquals("/c", jsonPointer2.toString());
 }

 // Test compile method
 @Test(expected = IllegalArgumentException.class)
 public void testCompileInvalidInput() {
 String input = "abc";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 fail("Exception expected: Invalid input: JSON Pointer expression must start with '/': \"abc\"");
 }

 @Test
 public void testCompileValidInput() {
 String input = "/a/b/c";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals(JsonPointer.compile(input), jsonPointer);
 assertEquals("/a/b/c", jsonPointer.toString());
 }

 // Test factory method fromSegment
 //TODO: Implement this method and write test cases for it

 // Test equals method
 @Test
 public void testEquals() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer2 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer3 = new JsonPointer("/a/d/e");
 assertTrue(jsonPointer1.equals(jsonPointer1));
 assertTrue(jsonPointer1.equals(jsonPointer2));
 assertFalse(jsonPointer1.equals(jsonPointer3));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer2 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer3 = new JsonPointer("/a/d/e");
 int hashCode1 = jsonPointer1.hashCode();
 int hashCode2 = jsonPointer2.hashCode();
 int hashCode3 = jsonPointer3.hashCode();
 assertEquals(hashCode1, hashCode1);
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 // Test matches method
 @Test
 public void testMatches() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer2 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer3 = new JsonPointer("/a/b/d");
 assertTrue(jsonPointer1.matches());
 assertFalse(jsonPointer2.matches());
 assertFalse(jsonPointer3.matches());
 }

 // Test getMatchingProperty method
 @Test
 public void testGetMatchingProperty() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer2 = new JsonPointer("/a/b/d");
 assertEquals("b", jsonPointer1.getMatchingProperty());
 assertEquals(null, jsonPointer2.getMatchingProperty());
 }

 // Test getMatchingIndex method
 @Test
 public void testGetMatchingIndex() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/0");
 JsonPointer jsonPointer2 = new JsonPointer("/a/b/1");
 JsonPointer jsonPointer3 = new JsonPointer("/a/b");
 assertEquals(0, jsonPointer1.getMatchingIndex());
 assertEquals(1, jsonPointer2.getMatchingIndex());
 assertEquals(-1, jsonPointer3.getMatchingIndex());
 }

 // Test mayMatchProperty method
 @Test
 public void testMayMatchProperty() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer2 = new JsonPointer("/a/b");
 assertTrue(jsonPointer1.mayMatchProperty());
 assertFalse(jsonPointer2.mayMatchProperty());
 }

 // Test mayMatchElement method
 @Test
 public void testMayMatchElement() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/0");
 JsonPointer jsonPointer2 = new JsonPointer("/a/b");
 assertTrue(jsonPointer1.mayMatchElement());
 assertFalse(jsonPointer2.mayMatchElement());
 }

 // Test matchProperty method
 @Test
 public void testMatchProperty() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer2 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer3 = new JsonPointer("/a/d/e");
 JsonPointer jsonPointer4 = new JsonPointer("/a/b");
 JsonPointer jsonPointer5 = JsonPointer.EMPTY;
 assertEquals(jsonPointer2, jsonPointer1.matchProperty("c"));
 assertNull(jsonPointer2.matchProperty("d"));
 assertNull(jsonPointer3.matchProperty("c"));
 assertNull(jsonPointer4.matchProperty("c"));
 assertNull(jsonPointer5.matchProperty("c"));
 }

 // Test matchElement method
 @Test
 public void testMatchElement() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/0");
 JsonPointer jsonPointer2 = new JsonPointer("/a/b/1");
 JsonPointer jsonPointer3 = new JsonPointer("/a/b");
 JsonPointer jsonPointer4 = JsonPointer.EMPTY;
 assertEquals(jsonPointer1, jsonPointer1.matchElement(0));
 assertNull(jsonPointer1.matchElement(-1));
 assertNull(jsonPointer2.matchElement(0));
 assertNull(jsonPointer2.matchElement(1));
 assertNull(jsonPointer3.matchElement(0));
 assertNull(jsonPointer4.matchElement(0));
 }

 // Test tail method
 @Test
 public void testTail() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/c");
 JsonPointer jsonPointer2 = jsonPointer1.tail();
 assertEquals(jsonPointer2, jsonPointer1.tail());
 assertEquals("/c", jsonPointer2.toString());
 }

 // Test toString method
 @Test
 public void testToString() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/c");
 assertEquals("/a/b/c", jsonPointer1.toString());
 }

 // Test parsing indexes greater than INT_MAX
 @Test(expected = IllegalArgumentException.class)
 public void testParseIndexGreaterThanIntMax() {
 JsonPointer jsonPointer = JsonPointer.compile("/a/b/2147483648");
 fail("Exception expected: Invalid input: JSON Pointer expression must start with '/': \"/a/b/2147483648\"");
 }

 // Test parsing indexes less than zero
 @Test(expected = IllegalArgumentException.class)
 public void testParseIndexLessThanZero() {
 JsonPointer jsonPointer = JsonPointer.compile("/a/b/-1");
 fail("Exception expected: Invalid input: JSON Pointer expression must start with '/': \"/a/b/-1\"");
 }

 // Test parsing invalid indexes
 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidIndexes() {
 JsonPointer jsonPointer = JsonPointer.compile("/a/b/x");
 fail("Exception expected: Invalid input: JSON Pointer expression must start with '/': \"/a/b/x\"");
 }
}