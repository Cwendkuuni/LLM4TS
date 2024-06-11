package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer empty = JsonPointer.EMPTY;
 assertTrue(empty.matches());
 assertNull(empty.getMatchingProperty());
 assertEquals(-1, empty.getMatchingIndex());
 assertFalse(empty.mayMatchProperty());
 assertFalse(empty.mayMatchElement());
 assertEquals("", empty.toString());
 }

 @Test
 public void testConstructFromString() throws IllegalArgumentException {
 // Test empty pointer
 JsonPointer ptr1 = JsonPointer.compile("/");
 assertTrue(ptr1.matches());
 assertNull(ptr1.getMatchingProperty());
 assertEquals(-1, ptr1.getMatchingIndex());
 assertFalse(ptr1.mayMatchProperty());
 assertFalse(ptr1.mayMatchElement());
 assertEquals("/", ptr1.toString());

 // Test property pointer
 JsonPointer ptr2 = JsonPointer.compile("/prop");
 assertTrue(ptr2.matches());
 assertEquals("prop", ptr2.getMatchingProperty());
 assertEquals(-1, ptr2.getMatchingIndex());
 assertFalse(ptr2.mayMatchElement());
 assertTrue(ptr2.mayMatchProperty());
 assertEquals("/prop", ptr2.toString());

 // Test array index pointer
 JsonPointer ptr3 = JsonPointer.compile("/arr/0");
 assertTrue(ptr3.matches());
 assertEquals("arr", ptr3.getMatchingProperty());
 assertEquals(0, ptr3.getMatchingIndex());
 assertFalse(ptr3.mayMatchProperty());
 assertTrue(ptr3.mayMatchElement());
 assertEquals("/arr/0", ptr3.toString());

 // Test property and index pointer
 JsonPointer ptr4 = JsonPointer.compile("/arr/1/prop");
 assertTrue(ptr4.matches());
 assertEquals("arr", ptr4.getMatchingProperty());
 assertEquals(1, ptr4.getMatchingIndex());
 assertFalse(ptr4.mayMatchProperty());
 assertTrue(ptr4.mayMatchElement());
 assertEquals("/arr/1/prop", ptr4.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer.compile("invalid");
 }

 @Test
 public void testTailMethod() {
 JsonPointer ptr5 = JsonPointer.compile("/arr/0/prop");
 JsonPointer tail1 = ptr5.tail();
 JsonPointer tail2 = tail1.tail();
 JsonPointer tail3 = tail2.tail();
 assertEquals(ptr5, tail1.tail());
 assertEquals(ptr5, tail2.tail());
 assertEquals(ptr5, tail3.tail());
 assertNull(tail3.tail());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer ptr6 = JsonPointer.compile("/arr/0/prop");
 JsonPointer match1 = ptr6.matchProperty("arr");
 JsonPointer match2 = ptr6.matchProperty("prop");
 assertEquals(ptr6, match1);
 assertEquals(ptr6, match2);
 }

 @Test
 public void testMatchElement() {
 JsonPointer ptr7 = JsonPointer.compile("/arr/0/prop");
 JsonPointer match1 = ptr7.matchElement(0);
 JsonPointer match2 = ptr7.matchElement(1);
 assertEquals(ptr7, match1);
 assertNull(match2);
 }

 @Test
 public void testHashCodeAndEquals() {
 JsonPointer ptr1 = JsonPointer.compile("/arr/0/prop");
 JsonPointer ptr2 = JsonPointer.compile("/arr/0/prop");
 JsonPointer ptr3 = JsonPointer.compile("/arr/1/prop");
 JsonPointer ptr4 = new JsonPointer("/arr/0/prop", "", ptr2);
 assertEquals(ptr1.hashCode(), ptr2.hashCode());
 assertEquals(ptr1, ptr2);
 assertNotEquals(ptr1, ptr3);
 assertNotEquals(ptr1, ptr4);
 }
}