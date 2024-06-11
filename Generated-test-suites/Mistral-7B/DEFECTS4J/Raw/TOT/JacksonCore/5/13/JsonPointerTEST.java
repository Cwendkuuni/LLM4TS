package com.fasterxml.jackson.core.test;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertEquals("", emptyPointer.toString());
 assertFalse(emptyPointer.matches());
 assertNull(emptyPointer.tail());
 assertNull(emptyPointer.getMatchingProperty());
 assertNull(emptyPointer.getMatchingIndex());
 }

 @Test
 public void testBasicPointer() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertEquals("/foo/bar", tail.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer.compile("invalid");
 }

 @Test
 public void testPropertyMatch() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNull(tail);
 }

 @Test
 public void testElementMatch() {
 String input = "/foo/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 assertFalse(pointer.matches());
 assertEquals(null, pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals(0, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNull(tail);
 }

 @Test
 public void testElementMatchNegative() {
 String input = "/foo/-1";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 assertFalse(pointer.matches());
 assertEquals(null, pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertFalse(pointer.getMatchingIndex() >= 0);
 JsonPointer tail = pointer.tail();
 assertNull(tail);
 }

 @Test
 public void testPropertyMatchWithTrailingSlash() {
 String input = "/foo/bar/";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNull(tail);
 }

 @Test
 public void testElementMatchWithTrailingSlash() {
 String input = "/foo/0/";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 assertFalse(pointer.matches());
 assertEquals(null, pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals(0, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNull(tail);
 }

 @Test
 public void testComplexPointer() {
 String input = "/foo/0/bar/2";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals(2, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertEquals("/foo/0/bar", tail.toString());
 }

 @Test
 public void testComplexPointerNoMatch() {
 String input = "/foo/0/bar/3";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertFalse(pointer.getMatchingIndex() >= 0);
 JsonPointer tail = pointer.tail();
 assertNull(tail);
 }

 @Test
 public void testComplexPointerNoMatchProperty() {
 String input = "/foo/baz/bar/2";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals(2, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertEquals("/foo/baz", tail.toString());
 }
}