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
 JsonPointer empty = JsonPointer.EMPTY;
 assertTrue(empty.matches());
 assertNull(empty.getMatchingProperty());
 assertFalse(empty.mayMatchProperty());
 assertFalse(empty.mayMatchElement());
 assertNull(empty.tail());
 assertEquals("", empty.toString());
 }

 @Test
 public void testSimplePointer() {
 String input = "/foo";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo", pointer.toString());
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 }

 @Test
 public void testPropertyPointer() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/bar", pointer.toString());
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 }

 @Test
 public void testElementPointer() {
 String input = "/foo/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/0", pointer.toString());
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchElement());
 assertFalse(pointer.mayMatchProperty());
 assertNull(pointer.tail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "~foo";
 JsonPointer pointer = JsonPointer.compile(input);
 }

 @Test
 public void testMultipleSegments() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/bar/baz", pointer.toString());
 assertFalse(pointer.matches());
 assertEquals("baz", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 }

 @Test
 public void testTailPointer() {
 String input = "/foo/bar/baz";
 JsonPointer pointer1 = JsonPointer.compile(input);
 JsonPointer pointer2 = pointer1.tail();
 assertEquals("/bar/baz", pointer2.toString());
 assertFalse(pointer2.matches());
 assertNull(pointer2.getMatchingProperty());
 assertFalse(pointer2.mayMatchElement());
 assertNull(pointer2.tail());
 }

 @Test
 public void testEmptyInput() {
 String input = "";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/", pointer.toString());
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 }
}