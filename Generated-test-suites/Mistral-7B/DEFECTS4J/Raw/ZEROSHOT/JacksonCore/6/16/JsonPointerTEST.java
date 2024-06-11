package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 JsonPointer emptyPointer = JsonPointer.EMPTY;
 JsonPointer pointer1 = new JsonPointer("/test/property1");
 JsonPointer pointer2 = new JsonPointer("/test/property1/property2");
 JsonPointer pointer3 = new JsonPointer("/test/property1/0");
 JsonPointer pointer4 = null;

 @Test
 public void testEmptyPointer() {
 JsonPointer ptr = emptyPointer;
 assertTrue(ptr.matches());
 assertEquals("", ptr.getMatchingProperty());
 assertEquals(-1, ptr.getMatchingIndex());
 assertFalse(ptr.mayMatchProperty());
 assertFalse(ptr.mayMatchElement());
 assertNull(ptr.tail());
 assertEquals("", ptr.toString());
 }

 @Test
 public void testPointer1() {
 JsonPointer ptr = pointer1;
 assertFalse(ptr.matches());
 assertEquals("property1", ptr.getMatchingProperty());
 assertEquals(-1, ptr.getMatchingIndex());
 assertTrue(ptr.mayMatchProperty());
 assertFalse(ptr.mayMatchElement());
 assertNull(ptr.tail());
 assertEquals("/test/property1", ptr.toString());
 }

 @Test
 public void testPointer2() {
 JsonPointer ptr = pointer2;
 assertFalse(ptr.matches());
 assertEquals("property2", ptr.getMatchingProperty());
 assertEquals(-1, ptr.getMatchingIndex());
 assertTrue(ptr.mayMatchProperty());
 assertFalse(ptr.mayMatchElement());
 assertNotNull(ptr.tail());
 assertEquals("/test/property1", ptr.tail().toString());
 assertEquals("/test/property1/property2", ptr.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer ptr = JsonPointer.compile("testproperty");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testPointer3() {
 JsonPointer ptr = pointer3;
 assertFalse(ptr.matches());
 assertEquals("", ptr.getMatchingProperty());
 assertEquals(0, ptr.getMatchingIndex());
 assertTrue(ptr.mayMatchProperty());
 assertFalse(ptr.mayMatchElement());
 assertNull(ptr.tail());
 assertEquals("/test/property1/0", ptr.toString());
 }

 @Test
 public void testNullPointer() {
 JsonPointer ptr = pointer4;
 assertNull(ptr);
 }
}