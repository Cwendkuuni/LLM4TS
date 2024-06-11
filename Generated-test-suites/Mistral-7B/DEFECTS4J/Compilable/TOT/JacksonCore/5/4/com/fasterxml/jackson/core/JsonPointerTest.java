package com.fasterxml.jackson.core;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.valueOf("/test/property");
 assertTrue(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("/test/property", pointer.toString());
 }

 @Test
 public void testPointerWithIndex() {
 JsonPointer pointer = JsonPointer.valueOf("/test/0/element");
 assertTrue(pointer.matches());
 assertEquals("element", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("/test/0/element", pointer.toString());
 }

 @Test(expected=IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer.compile("test");
 }

 @Test
 public void testParseTail() {
 JsonPointer pointer = JsonPointer.compile("/test/property/element/~1/~0/property");
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertEquals("/test/property/element", pointer.tail().toString());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer = JsonPointer.compile("/test/property/0/element");
 JsonPointer subPointer = JsonPointer.compile("/test/property");
 assertTrue(pointer.matchProperty("property").equals(subPointer));
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer = JsonPointer.compile("/test/property/0/element");
 assertTrue(pointer.matchElement(0).equals(JsonPointer.EMPTY));
 JsonPointer subPointer = JsonPointer.compile("/test/property/1/element");
 assertTrue(pointer.matchElement(1).equals(subPointer));
 }

 @Test
 public void testTail() {
 JsonPointer pointer = JsonPointer.compile("/test/property/0/element");
 JsonPointer subPointer = JsonPointer.compile("/test/property/1/element");
 assertTrue(pointer.tail().equals(subPointer));
 }
}