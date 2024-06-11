package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

 private JsonPointer createJsonPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 return null;
 }

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertEquals("", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertEquals(0, pointer.hashCode());
 }

 @Test
 public void testInvalidInput() {
 String input = "abcd";
 JsonPointer pointer = createJsonPointer(input);
 assertNull(pointer);
 }

 @Test
 public void testValidInput() {
 String input = "/a/b/c";
 JsonPointer pointer = createJsonPointer(input);
 assertTrue(pointer.matches());
 assertEquals("a", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertEquals(input.hashCode(), pointer.hashCode());
 }

 @Test
 public void testValidInputWithProperty() {
 String input = "/a/b/0";
 JsonPointer pointer = createJsonPointer(input);
 assertTrue(pointer.matches());
 assertEquals("b", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertEquals(input.hashCode(), pointer.hashCode());
 }

 @Test
 public void testValidInputWithElement() {
 String input = "/a/0/b";
 JsonPointer pointer = createJsonPointer(input);
 assertTrue(pointer.matches());
 assertEquals("", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertEquals(input.hashCode(), pointer.hashCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithNoSlash() {
 String input = "abcd";
 JsonPointer pointer = createJsonPointer(input);
 fail("Unexpected pointer creation: " + pointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithInvalidCharacter() {
 String input = "/a/~b/c";
 JsonPointer pointer = createJsonPointer(input);
 fail("Unexpected pointer creation: " + pointer);
 }

 @Test
 public void testTailPointer() {
 String input = "/a/b/c";
 JsonPointer pointer = createJsonPointer(input);
 JsonPointer tailPointer = pointer.tail();
 assertEquals(input.substring(input.lastIndexOf('/') + 1), tailPointer.getMatchingProperty());
 assertTrue(tailPointer.matches());
 }

 @Test
 public void testTailEmptyPointer() {
 String input = "/a/b/c";
 JsonPointer pointer = createJsonPointer(input);
 JsonPointer tailPointer = pointer.tail();
 tailPointer = tailPointer.tail();
 assertEquals("", tailPointer.getMatchingProperty());
 assertTrue(tailPointer.matches());
 }

 @Test
 public void testToString() {
 String input = "/a/b/c";
 JsonPointer pointer = createJsonPointer(input);
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testHashCodeEquality() {
 String input1 = "/a/b/c";
 String input2 = "/a/b/c";
 JsonPointer pointer1 = createJsonPointer(input1);
 JsonPointer pointer2 = createJsonPointer(input2);
 assertEquals(input1.hashCode(), pointer1.hashCode());
 assertEquals(input2.hashCode(), pointer2.hashCode());
 assertTrue(pointer1.equals(pointer2));
 }

 @Test
 public void testHashCodeInequality() {
 String input1 = "/a/b/c";
 String input2 = "/a/b/d";
 JsonPointer pointer1 = createJsonPointer(input1);
 JsonPointer pointer2 = createJsonPointer(input2);
 assertFalse(pointer1.equals(pointer2));
 assertNotSame(pointer1.hashCode(), pointer2.hashCode());
 }
}