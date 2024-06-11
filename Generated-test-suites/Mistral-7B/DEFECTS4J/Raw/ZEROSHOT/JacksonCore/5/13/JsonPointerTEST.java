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
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/foo");
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 }

 @Test
 public void testPointerWithProperty() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer pointer = JsonPointer.compile("foo");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testPointerWithElement() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 assertFalse(pointer.matches());
 assertEquals("", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 }

 @Test
 public void testPointerWithMultipleElements() {
 JsonPointer pointer = JsonPointer.compile("/foo/0/baz/1");
 assertFalse(pointer.matches());
 assertEquals("baz", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 }

 @Test
 public void testTailPointer() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 JsonPointer tailPointer = pointer.tail();
 assertEquals("/foo", tailPointer.toString());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 JsonPointer matchingPointer = new JsonPointer("/foo", null, JsonPointer.EMPTY);
 JsonPointer result = pointer.matchProperty("bar");
 assertEquals(matchingPointer, result);
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 JsonPointer matchingPointer = new JsonPointer("/foo", null, JsonPointer.EMPTY);
 JsonPointer result = pointer.matchElement(0);
 assertEquals(matchingPointer, result);
 }

 @Test
 public void testEquals() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
 JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
 JsonPointer pointer3 = JsonPointer.compile("/foo/baz");
 assertTrue(pointer1.equals(pointer2));
 assertFalse(pointer1.equals(pointer3));
 }

 @Test
 public void testHashCode() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
 JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
 JsonPointer pointer3 = JsonPointer.compile("/foo/baz");
 assertEquals(pointer1.hashCode(), pointer2.hashCode());
 assertNotSame(pointer1.hashCode(), pointer3.hashCode());
 }
}