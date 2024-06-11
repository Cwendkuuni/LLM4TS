package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 }

 @Test
 public void testValidPointer() {
 String pointerString = "/foo/bar/1";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals("/foo/bar/1", pointer.toString());
 assertTrue(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertFalse(pointer.mayMatchProperty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String invalidPointerString = "foo";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(invalidPointerString);
 } catch (IllegalArgumentException e) {
 // Expected result: pointer remains null
 }
 assertEquals(null, pointer);
 }

 @Test
 public void testMatchProperty() {
 String pointerString = "/foo/bar/1";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 String property = "bar";
 JsonPointer matchedPointer = pointer.matchProperty(property);
 assertEquals(property, matchedPointer.getMatchingProperty());
 assertTrue(matchedPointer.matches());
 }

 @Test
 public void testNoMatchProperty() {
 String pointerString = "/foo/bar/1";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 String property = "qux";
 JsonPointer matchedPointer = pointer.matchProperty(property);
 assertNull(matchedPointer);
 }

 @Test
 public void testMatchElement() {
 String pointerString = "/foo/[0]/bar/1";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer matchedPointer = pointer.matchElement(0);
 assertEquals("bar", matchedPointer.getMatchingProperty());
 assertTrue(matchedPointer.matches());
 }

 @Test
 public void testNoMatchElement() {
 String pointerString = "/foo/[0]/bar/1";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer matchedPointer = pointer.matchElement(-1);
 assertNull(matchedPointer);
 }

 @Test
 public void testTail() {
 String pointerString = "/foo/bar/1";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer tail = pointer.tail();
 assertEquals("/bar/1", tail.toString());
 }
}