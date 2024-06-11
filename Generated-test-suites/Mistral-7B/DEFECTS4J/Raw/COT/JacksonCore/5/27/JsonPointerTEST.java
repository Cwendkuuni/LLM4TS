package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 // Test constructor with empty string
 @Test
 public void testEmptyConstructor() {
 JsonPointer emptyPointer = new JsonPointer();
 assertTrue(emptyPointer.matches());
 assertEquals("", emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertEquals("", emptyPointer.toString());
 }

 // Test constructor with valid JSON Pointer
 @Test
 public void testValidConstructor() {
 String input = "/foo/bar/1";
 JsonPointer pointer = JsonPointer.compile(input);
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 assertEquals(input, pointer.toString());
 }

 // Test constructor with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "baz";
 JsonPointer.compile(input);
 }

 // Test matches method
 @Test
 public void testMatches() {
 String input = "/foo/bar";
 JsonPointer pointer1 = JsonPointer.compile(input);
 JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
 assertTrue(pointer1.matches());
 assertTrue(pointer2.matches());
 JsonPointer pointer3 = JsonPointer.compile("/foo/baz");
 assertFalse(pointer1.matches());
 assertFalse(pointer3.matches());
 }

 // Test getMatchingProperty method
 @Test
 public void testGetMatchingProperty() {
 String input = "/foo/bar/1";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("foo", pointer.getMatchingProperty());
 }

 // Test getMatchingIndex method
 @Test
 public void testGetMatchingIndex() {
 String input = "/foo/bar/1";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(1, pointer.getMatchingIndex());
 }

 // Test mayMatchProperty method
 @Test
 public void testMayMatchProperty() {
 String input1 = "/foo/bar";
 JsonPointer pointer1 = JsonPointer.compile(input1);
 assertTrue(pointer1.mayMatchProperty());
 String input2 = "/foo";
 JsonPointer pointer2 = JsonPointer.compile(input2);
 assertTrue(pointer2.mayMatchProperty());
 String input3 = "/foo/bar/1";
 JsonPointer pointer3 = JsonPointer.compile(input3);
 assertTrue(pointer3.mayMatchProperty());
 String input4 = "/foo/qux";
 JsonPointer pointer4 = JsonPointer.compile(input4);
 assertFalse(pointer3.mayMatchProperty());
 }

 // Test mayMatchElement method
 @Test
 public void testMayMatchElement() {
 String input = "/foo/bar/1";
 JsonPointer pointer = JsonPointer.compile(input);
 assertTrue(pointer.mayMatchElement());
 JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
 assertTrue(pointer2.mayMatchElement());
 JsonPointer pointer3 = JsonPointer.compile("/foo/qux");
 assertFalse(pointer.mayMatchElement());
 }

 // Test matchProperty method
 @Test
 public void testMatchProperty() {
 String input1 = "/foo/bar";
 JsonPointer pointer1 = JsonPointer.compile(input1);
 JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
 assertEquals(pointer2, pointer1.matchProperty("bar"));
 JsonPointer pointer3 = JsonPointer.compile("/baz/bar");
 assertNull(pointer1.matchProperty("bar"));
 }

 // Test matchElement method
 @Test
 public void testMatchElement() {
 String input = "/foo/bar/1";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(pointer, pointer.matchElement(1));
 JsonPointer pointer2 = JsonPointer.compile("/foo/bar/2");
 assertNull(pointer.matchElement(1));
 }

 // Test tail method
 @Test
 public void testTail() {
 String input = "/foo/bar/1";
 JsonPointer pointer = JsonPointer.compile(input);
 JsonPointer tail = pointer.tail();
 assertEquals("/foo/bar", pointer.toString());
 assertEquals("/bar", tail.toString());
 }

 // Test equals method
 @Test
 public void testEquals() {
 String input1 = "/foo/bar";
 JsonPointer pointer1 = JsonPointer.compile(input1);String input2 = "/foo/bar";JsonPointer pointer2 = JsonPointer.compile(input2);JsonPointer pointer3 = JsonPointer.compile("/foo/qux");String input3 = "/foo/bar/1";JsonPointer pointer4 = JsonPointer.compile(input3);assertEquals(pointer1, pointer2);assertNotEquals(pointer1, pointer3);assertEquals(pointer1, pointer1);assertEquals(pointer1, pointer4); }

 // Test hashCode method
 @Test
 public void testHashCode() {
 String input1 = "/foo/bar";
 JsonPointer pointer1 = JsonPointer.compile(input1);
 String input2 = "/foo/bar";
 JsonPointer pointer2 = JsonPointer.compile(input2);
 String input3 = "/foo/qux";
 JsonPointer pointer3 = JsonPointer.compile(input3);
 assertNotSame(pointer1.hashCode(), pointer2.hashCode());
 assertNotSame(pointer1.hashCode(), pointer3.hashCode());
 assertSame(pointer1.hashCode(), pointer1.hashCode());
 }

 // Test toString method
 @Test
 public void testToString() {
 String input1 = "/foo/bar";
 JsonPointer pointer1 = JsonPointer.compile(input1);
 assertEquals(input1, pointer1.toString());
 }

}