package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

   @Test
   public void testEmptyJsonPointer() {
       JsonPointer pointer = JsonPointer.compile("");
       assertTrue(pointer.matches());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testInvalidJsonPointer() {
       JsonPointer pointer = JsonPointer.compile("invalid");
       assertFalse(pointer.matches());
   }

   @Test
   public void testPropertyMatch() {
       JsonPointer pointer = JsonPointer.compile("/property");
       assertFalse(pointer.matches());
       assertEquals("property", pointer.getMatchingProperty());
       assertTrue(pointer.mayMatchProperty());
       assertFalse(pointer.mayMatchElement());
       assertNull(pointer.matchProperty("differentProperty"));
       assertNotNull(pointer.matchProperty("property"));
   }

   @Test
   public void testElementMatch() {
       JsonPointer pointer = JsonPointer.compile("/0");
       assertFalse(pointer.matches());
       assertEquals(0, pointer.getMatchingIndex());
       assertFalse(pointer.mayMatchProperty());
       assertTrue(pointer.mayMatchElement());
       assertNull(pointer.matchElement(1));
       assertNotNull(pointer.matchElement(0));
   }

   @Test
   public void testCompositePointer() {
       JsonPointer pointer = JsonPointer.compile("/property/0");
       assertFalse(pointer.matches());
       assertEquals("property", pointer.getMatchingProperty());
       assertFalse(pointer.mayMatchProperty());
       assertTrue(pointer.mayMatchElement());
       JsonPointer nextSegment = pointer.matchElement(0);
       assertNotNull(nextSegment);
       assertEquals(0, nextSegment.getMatchingIndex());
   }

   @Test
   public void testEscapeCharacters() {
       JsonPointer pointer = JsonPointer.compile("/foo~0bar/0~1");
       assertFalse(pointer.matches());
       assertEquals("foo~bar", pointer.getMatchingProperty());
       assertEquals(0, pointer.getMatchingIndex());
   }
   
}