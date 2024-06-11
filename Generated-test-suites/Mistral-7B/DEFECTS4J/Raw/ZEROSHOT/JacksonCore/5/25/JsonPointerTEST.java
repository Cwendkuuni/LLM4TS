package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 private static JsonPointer createJsonPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 fail("Input \"" + input + "\" should parse to a valid JsonPointer.");
 }
 return null;
 }

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @Test
 public void testBasicPointer() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = createJsonPointer(input);
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/foo/bar/baz", pointer.toString());

 pointer = pointer.tail();
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/foo/bar", pointer.toString());

 pointer = pointer.tail();
 assertEquals("baz", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/foo/bar", pointer.toString());
 }

 @Test
 public void testPropertyPointer() {
 String input = "/foo/0/prop";
 JsonPointer pointer = createJsonPointer(input);
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/foo/0/prop", pointer.toString());
 }

 @Test
 public void testElementPointer() {
 String input = "/foo/1/bar";
 JsonPointer pointer = createJsonPointer(input);
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/foo/1/bar", pointer.toString());
 }

 @Test
 public void testBasicChainPointer() {
 String input = "/foo/0/prop/1/bar";
 JsonPointer pointer = createJsonPointer(input);
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/foo/0/prop", pointer.toString());

 pointer = pointer.tail();
 assertEquals("prop", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/foo/0/prop", pointer.toString());

 pointer = pointer.tail();
 assertEquals("1", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/foo/0", pointer.toString());

 pointer = pointer.tail();
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/foo", pointer.toString());
 }

 @Test
 public void testIllegalInput() {
 String input = "~/foo";
 try {
 JsonPointer pointer = createJsonPointer(input);
 fail("Input \"" + input + "\" should not parse to a valid JsonPointer.");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 input = "/foo/";
 try {
 JsonPointer pointer = createJsonPointer(input);
 fail("Input \"" + input + "\" should not parse to a valid JsonPointer.");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 input = "/foo/bar/baz/";
 try {
 JsonPointer pointer = createJsonPointer(input);
 fail("Input \"" + input + "\" should not parse to a valid JsonPointer.");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 input = "foo";
 try {
 JsonPointer pointer = createJsonPointer(input);
 fail("Input \"" + input + "\" should not parse to a valid JsonPointer.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testChainNullPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 pointer = pointer.tail();
 assertNull(pointer);
 }

 @Test
 public void testChainEmptyPropertyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 pointer = pointer.matchProperty("");
 assertNull(pointer);
 }

 @Test
 public void testChainNullPropertyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 pointer = pointer.matchProperty(null);
 assertNull(pointer);
 }

 @Test
 public void testChainInvalidIndex() {
 JsonPointer pointer = JsonPointer.EMPTY;
 pointer = pointer.matchElement(-1);
 assertNull(pointer);
 }
}