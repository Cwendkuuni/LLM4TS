package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
     @Test
     public void testMatches_emptyPointer() {
          JsonPointer pointer = new JsonPointer();
          assertTrue(pointer.matches());
     }
     
     @Test
     public void testGetMatchingProperty() {
          JsonPointer pointer = new JsonPointer();
          String property = pointer.getMatchingProperty();
          assertEquals("", property);
     }
     
     @Test
     public void testGetMatchingIndex() {
          JsonPointer pointer = new JsonPointer();
          int index = pointer.getMatchingIndex();
          assertEquals(-1, index);
     }
     
     @Test
     public void testMayMatchProperty() {
          JsonPointer pointer = new JsonPointer();
          boolean mayMatch = pointer.mayMatchProperty();
          assertTrue(mayMatch);
     }
     
     @Test
     public void testMayMatchElement() {
          JsonPointer pointer = new JsonPointer();
          boolean mayMatch = pointer.mayMatchElement();
          assertFalse(mayMatch);
     }
     
     @Test
     public void testMatchProperty_matchingProperty() {
          JsonPointer pointer = new JsonPointer("fullString", "segment", null);
          JsonPointer next = pointer.matchProperty("segment");
          assertEquals(null, next);
     }
     
     @Test
     public void testMatchProperty_nonMatchingProperty() {
          JsonPointer pointer = new JsonPointer("fullString", "segment", null);
          JsonPointer next = pointer.matchProperty("nonMatchingSegment");
          assertEquals(null, next);
     }
     
     @Test
     public void testMatchElement_matchingIndex() {
          JsonPointer pointer = new JsonPointer("fullString", "segment", null);
          JsonPointer next = pointer.matchElement(0);
          assertEquals(null, next);
     }
     
     @Test
     public void testMatchElement_nonMatchingIndex() {
          JsonPointer pointer = new JsonPointer("fullString", "segment", null);
          JsonPointer next = pointer.matchElement(1);
          assertEquals(null, next);
     }
     
     @Test
     public void testTail() {
          JsonPointer pointer = new JsonPointer("fullString", "segment", null);
          JsonPointer tail = pointer.tail();
          assertEquals(null, tail);
     }
     
     @Test
     public void testToString() {
          JsonPointer pointer = new JsonPointer("fullString", "segment", null);
          String toString = pointer.toString();
          assertEquals("fullString", toString);
     }
     
     @Test
     public void testHashCode() {
          JsonPointer pointer = new JsonPointer("fullString", "segment", null);
          int hashCode = pointer.hashCode();
          assertEquals(1234567, hashCode);
     }
     
     @Test
     public void testEquals_samePointers() {
          JsonPointer pointer1 = new JsonPointer("fullString", "segment", null);
          JsonPointer pointer2 = new JsonPointer("fullString", "segment", null);
          assertTrue(pointer1.equals(pointer2));
     }
     
     @Test
     public void testEquals_differentPointers() {
          JsonPointer pointer1 = new JsonPointer("fullString", "segment", null);
          JsonPointer pointer2 = new JsonPointer("differentString", "segment", null);
          assertFalse(pointer1.equals(pointer2));
     }
     
     @Test
     public void testCompile_emptyInput() {
          JsonPointer pointer = JsonPointer.compile("");
          assertTrue(pointer.matches());
     }
     
     @Test
     public void testCompile_nonEmptyInput() {
          JsonPointer pointer = JsonPointer.compile("/segment");
          assertEquals("segment", pointer.getMatchingProperty());
     }
     
     @Test(expected = IllegalArgumentException.class)
     public void testCompile_invalidInput() {
          JsonPointer pointer = JsonPointer.compile("segment");
     }
     
     @Test
     public void testValueOf() {
          JsonPointer pointer = JsonPointer.valueOf("/segment");
          assertEquals("segment", pointer.getMatchingProperty());
     }
     
     @Test
     public void test_parseIndex_validIndex() {
          int index = JsonPointer._parseIndex("123");
          assertEquals(123, index);
     }
     
     @Test
     public void test_parseIndex_invalidIndex() {
          int index = JsonPointer._parseIndex("abc");
          assertEquals(-1, index);
     }
     
     @Test
     public void test_parseTail_multipleSegments() {
          JsonPointer pointer = JsonPointer._parseTail("/segment1/segment2");
          assertEquals("segment1", pointer.getMatchingProperty());
          assertNotNull(pointer.tail());
          assertEquals("segment2", pointer.tail().getMatchingProperty());
     }
     
     @Test
     public void test_parseTail_escapedCharacters() {
          JsonPointer pointer = JsonPointer._parseTail("/segment~0");
          assertEquals("segment~", pointer.getMatchingProperty());
     }
}