package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

 private static JsonPointer createJsonPointer(String pointerString) {
 return JsonPointer.compile(pointerString);
 }

 @Test
 public void testEmptyConstructor() {
 JsonPointer emptyPointer = new JsonPointer();
 assertEquals("", emptyPointer.toString());
 assertNull(emptyPointer.tail());
 assertFalse(emptyPointer.matches());
 }

 @Test
 public void testConstructorWithSegments() {
 String pointerString = "/a/b/0";
 JsonPointer pointer = createJsonPointer(pointerString);
 assertEquals("/a/b/0", pointer.toString());
 assertEquals("/b", pointer.tail().toString());
 assertFalse(pointer.matches());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String invalidInput = "invalid";
 try {
 JsonPointer pointer = createJsonPointer(invalidInput);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("Invalid input: JSON Pointer expression must start with '/': \"invalid\"", e.getMessage());
 }
 }

 @Test
 public void testMatches() {
 JsonPointer emptyPointer = new JsonPointer();
 JsonPointer pointerA = new JsonPointer("/a", null, emptyPointer);
 JsonPointer pointerB = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerC = new JsonPointer("/a/b/c", null, emptyPointer);
 JsonPointer pointerD = new JsonPointer("/a/b/1", null, emptyPointer);
 JsonPointer pointerE = new JsonPointer("/a/c", null, emptyPointer);

 assertTrue(emptyPointer.matches());
 assertFalse(pointerA.matches());
 assertFalse(pointerB.matches());
 assertFalse(pointerC.matches());
 assertFalse(pointerD.matches());
 assertFalse(pointerE.matches());
 }

 @Test
 public void testGetMatchingProperty() {
 JsonPointer pointerA = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerB = new JsonPointer("/a/c", null, emptyPointer);
 assertEquals("b", pointerA.getMatchingProperty());
 assertNull(pointerB.getMatchingProperty());
 }

 @Test
 public void testGetMatchingIndex() {
 JsonPointer pointerA = new JsonPointer("/a/0", null, emptyPointer);
 JsonPointer pointerB = new JsonPointer("/a/1", null, emptyPointer);
 JsonPointer pointerC = new JsonPointer("/a", null, emptyPointer);
 assertEquals(0, pointerA.getMatchingIndex());
 assertEquals(1, pointerB.getMatchingIndex());
 assertSemanticEquals(0, pointerC.getMatchingIndex()); // using custom assertion for null vs -1 comparison
 }

 @Test
 public void testMayMatchProperty() {
 JsonPointer pointerA = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerB = new JsonPointer("/a", null, emptyPointer);
 assertTrue(pointerA.mayMatchProperty());
 assertFalse(pointerB.mayMatchProperty());
 }

 @Test
 public void testMayMatchElement() {
 JsonPointer pointerA = new JsonPointer("/a/0", null, emptyPointer);
 JsonPointer pointerB = new JsonPointer("/a", null, emptyPointer);
 assertTrue(pointerA.mayMatchElement());
 assertFalse(pointerB.mayMatchElement());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointerA = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerB = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerC = new JsonPointer("/a/c", null, emptyPointer);
 assertEquals(pointerB, pointerA.matchProperty("/b"));
 assertNull(pointerA.matchProperty("/c"));
 assertNull(pointerB.matchProperty("/c"));
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointerA = new JsonPointer("/a/0", null, emptyPointer);
 JsonPointer pointerB = new JsonPointer("/a/1", null, emptyPointer);
 JsonPointer pointerC = new JsonPointer("/a", null, emptyPointer);
 assertEquals(pointerA, pointerA.matchElement(0));
 assertEquals(pointerB, pointerB.matchElement(1));
 assertNull(pointerC.matchElement(0));
 }

 @Test
 public void testTail() {
JsonPointer pointerA = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerB = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerC = new JsonPointer("/a/b/c", null, emptyPointer);
 assertEquals(pointerB, pointerA.tail());
 assertNull(pointerB.tail());
 assertNull(pointerC.tail());
 }

 @Test
 public void testToString() {
 JsonPointer emptyPointer = new JsonPointer();
 JsonPointer pointerA = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerB = new JsonPointer("/a/b/c", null, emptyPointer);
 assertEquals("", emptyPointer.toString());
 assertEquals("/a/b", pointerA.toString());
 assertEquals("/a/b/c", pointerB.toString());
 }

 @Test
 public void testHashCode() {
 JsonPointer pointerA = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerB = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerC = new JsonPointer("/a/c", null, emptyPointer);
 JsonPointer pointerD = new JsonPointer("/a/d", null, emptyPointer);
 assertEquals(pointerA.hashCode(), pointerA.hashCode());
 assertEquals(pointerB.hashCode(), pointerB.hashCode());
 assertNotEquals(pointerA.hashCode(), pointerB.hashCode());
 assertNotEquals(pointerA.hashCode(), pointerC.hashCode());
 assertNotEquals(pointerA.hashCode(), pointerD.hashCode());
 }

 @Test
 public void testEquals() {
 JsonPointer pointerA = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerB = new JsonPointer("/a/b", null, emptyPointer);
 JsonPointer pointerC = new JsonPointer("/a/c", null, emptyPointer);
 JsonPointer pointerD = new JsonPointer("/a/b", null, emptyPointer);
 assertEquals(pointerA, pointerA);
 assertEquals(pointerB, pointerB);
 assertNotEquals(pointerA, pointerC);
 assertNotEquals(pointerA, pointerD);
 }

 @Test
 public void testParseIndex() {
 int index = JsonPointer._parseIndex("/a/0");
 assertEquals(0, index);
 int index2 = JsonPointer._parseIndex("/a/1000000000");
 assertEquals(-1, index2);
 }

 @Test
 public void testParseTail() {
 String pointerStringA = "/a/b";
 JsonPointer pointerA = createJsonPointer(pointerStringA);
 String pointerStringB = "/a/b/c";
 JsonPointer pointerB = createJsonPointer(pointerStringB);
 String pointerStringC = "/a~1/b/c~1/d";
 JsonPointer pointerC = createJsonPointer(pointerStringC);
 assertEquals(pointerB, pointerA.tail());
 assertEquals(pointerC, pointerB.tail());
 }
}