package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertEquals("", emptyPointer.toString());
 assertEquals(emptyPointer, emptyPointer.tail());
 assertNull(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertNull(emptyPointer.getMatchingIndex());
 }

 @Test
 public void testSimplePointer() {
 String input = "/a/b/c";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/a/b/c", pointer.toString());
 assertEquals("/c", pointer.tail().toString());
 assertNull(pointer.matches());
 assertEquals("b", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 }

 @Test
 public void testPointerWithIndex() {
 String input = "/a/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/a/0", pointer.toString());
 assertEquals("/", pointer.tail().toString());
 assertNull(pointer.matches());
 assertEquals(null, pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "abcd";
 JsonPointer pointer = JsonPointer.compile(input);
 }

 @Test
 public void testMatchingProperty() {
 String input = "/a/b/c/d/e/f/g/h/i[0]/j";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/h/i[0]/j", pointer.tail().toString());
 assertEquals("g", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 }

 @Test
 public void testMatchingElement() {
 String input = "/a/b/c[0]/d/e/f/g[1]/h";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/g[1]/h", pointer.tail().toString());
 assertEquals("f", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 }

 @Test
 public void testMatchingPropertyAndElement() {
 String input = "/a/b[0]/c/d[1]/e";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/d[1]/e", pointer.tail().toString());
 assertEquals("b", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 }

 @Test
 public void testNestedMatchingPropertyAndElement() {
 String input = "/a/b[0]/c[0]/d[1]/e[0]";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/d[1]/e[0]", pointer.tail().toString());
 assertEquals("c", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 }

}