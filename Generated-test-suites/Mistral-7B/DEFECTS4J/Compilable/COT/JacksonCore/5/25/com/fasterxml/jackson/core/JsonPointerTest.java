package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 private JsonPointer createJsonPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 fail("Input \"" + input + "\" should have been valid.");
 }
 return null;
 }

 @Test
 public void testEmptyJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.EMPTY;
 assertTrue(jsonPointer.matches());
 assertNull(jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertNull(jsonPointer.tail());
 assertEquals("", jsonPointer.toString());
 }

 @Test
 public void testJsonPointerWithSingleSegment() {
 JsonPointer jsonPointer = createJsonPointer("/seg1");
 assertFalse(jsonPointer.matches());
 assertEquals("seg1", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertNull(jsonPointer.tail());
 assertEquals("/seg1", jsonPointer.toString());
 }

 @Test
 public void testJsonPointerWithMultipleSegments() {
 JsonPointer jsonPointer = createJsonPointer("/seg1/seg2");
 assertFalse(jsonPointer.matches());
 assertEquals("seg2", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertNotNull(jsonPointer.tail());
 assertEquals("/seg1", jsonPointer.tail().toString());
 assertEquals("/seg1/seg2", jsonPointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerWithInvalidInput() {
 JsonPointer jsonPointer = createJsonPointer("seg");
 fail("Expected IllegalArgumentException but got: " + jsonPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerWithInvalidInputStartingWithSlash() {
 JsonPointer jsonPointer = createJsonPointer("/seg/");
 fail("Expected IllegalArgumentException but got: " + jsonPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerWithNegativeIndex() {
 JsonPointer jsonPointer = createJsonPointer("/arr/-1");
 fail("Expected IllegalArgumentException but got: " + jsonPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerWithTooLongIndex() {
 JsonPointer jsonPointer = createJsonPointer("/arr/1234567890123");
 fail("Expected IllegalArgumentException but got: " + jsonPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerWithEmptyArrayIndex() {
 JsonPointer jsonPointer = createJsonPointer("/arr/");
 fail("Expected IllegalArgumentException but got: " + jsonPointer);
 }

 @Test
 public void testJsonPointerWithMatchingProperty() {
 JsonPointer jsonPointer = createJsonPointer("/arr/0/prop");
 assertFalse(jsonPointer.matches());
 assertEquals("prop", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertNull(jsonPointer.tail());
 assertEquals("/arr/0/prop", jsonPointer.toString());
 }

 @Test
 public void testJsonPointerWithMatchingElement() {
 JsonPointer jsonPointer = createJsonPointer("/arr/1/2");
 assertFalse(jsonPointer.matches());
 assertEquals(null, jsonPointer.getMatchingProperty());
 assertEquals(1, jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertTrue(jsonPointer.mayMatchElement());
 assertNull(jsonPointer.tail());
 assertEquals("/arr/1/2", jsonPointer.toString());
 }

 @Test
 public void testJsonPointerWithSubPointer() {
 JsonPointer jsonPointer = createJsonPointer("/arr/1/prop");
 assertFalse(jsonPointer.matches());
 assertEquals("prop", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertNotNull(jsonPointer.tail());
 assertEquals("/arr/1", jsonPointer.tail().toString());
 assertEquals("/arr/1/prop", jsonPointer.toString());
 }

}