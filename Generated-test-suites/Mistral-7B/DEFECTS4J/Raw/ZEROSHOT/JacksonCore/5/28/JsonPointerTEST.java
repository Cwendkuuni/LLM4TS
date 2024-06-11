package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertEquals("", emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.tail());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testBasicPointer() {
 String input = "/test/property";
 JsonPointer pointer = JsonPointer.compile(input);
 assertTrue(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerInput() {
 String input = "testproperty";
 JsonPointer pointer = JsonPointer.compile(input);
 fail("Should throw IllegalArgumentException for invalid pointer input");
 }

 @Test
 public void testPointerWithTail() {
 String input = "/test/property/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNotNull(tail);
 assertEquals("/0", tail.toString());
 }

 @Test
 public void testPointerWithMultipleSegments() {
 String input = "/test/array/1/subarray/2";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertEquals("array", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNotNull(tail);
 assertEquals("/subarray/2", tail.toString());
 }

 @Test
 public void testQuotedPointer() {
 String input = "/test/property/~1~";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNull(tail);
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testPointerWithMultipleQuotes() {
 String input = "/test/property/~1~/~2~";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNull(tail);
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testPointerWithInnerQuotes() {
 String input = "/test/property/~0~/subarray/~1~";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNotNull(tail);
 assertEquals("/subarray/~1~", tail.toString());
 }

 @Test
 public void testPointerWithInnerSlash() {
 String input = "/test/property/~0~/subarray/1/";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNotNull(tail);
 assertEquals("/subarray/1/", tail.toString());
 }

 @Test
 public void testPointerWithInnerSlashAndQuotes() {
 String input = "/test/property/~0~/subarray/~1~/~2~";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNotNull(tail);
 assertEquals("/subarray/~1~/~2~", tail.toString());
 }
}