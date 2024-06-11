package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/test/property");
 assertFalse(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/test/property", pointer.toString());
 }

 @Test
 public void testPropertyMatching() {
 JsonPointer pointer1 = JsonPointer.compile("/test/property1");
 JsonPointer pointer2 = JsonPointer.compile("/test/property2");
 JsonPointer pointer3 = JsonPointer.compile("/test/property");

 assertTrue(pointer1.matches());
 assertEquals("property1", pointer1.getMatchingProperty());
 assertFalse(pointer1.mayMatchElement());
 assertNull(pointer1.tail());

 assertFalse(pointer2.matches());
 assertEquals("property2", pointer2.getMatchingProperty());
 assertFalse(pointer2.mayMatchElement());
 assertNull(pointer2.tail());

 assertTrue(pointer3.matches());
 assertNull(pointer3.getMatchingProperty());
 assertNull(pointer3.tail());

 assertEquals(pointer1, pointer1.tail());
 assertEquals(pointer2, pointer2.tail());
 assertEquals(pointer3, pointer3.tail());
 }

 @Test
 public void testIndexMatching() {
 JsonPointer pointer = JsonPointer.compile("/test/0");
 assertFalse(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());

 JsonPointer array = JsonPointer.compile("/test/[0]");
 assertFalse(array.matches());
 assertEquals("test", array.getMatchingProperty());
 assertTrue(array.mayMatchProperty());
 assertTrue(array.mayMatchElement());
 assertEquals(0, array.getMatchingIndex());
 assertNull(array.tail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer1() {
 JsonPointer pointer = JsonPointer.compile("test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer2() {
 JsonPointer pointer = JsonPointer.compile("~/test/property");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer3() {
 JsonPointer pointer = JsonPointer.compile("/test/property/");
 }

 @Test
 public void testCompoundPointer() {
 JsonPointer pointer1 = JsonPointer.compile("/test/property1");
 JsonPointer pointer2 = JsonPointer.compile("/test/property2");
 JsonPointer pointer3 = JsonPointer.compile("/test/property3");
 JsonPointer pointer4 = JsonPointer.compile("/test/property4");

 JsonPointer compoundPointer = JsonPointer.compile("/test/property1/property2/property3/property4");

 assertTrue(pointer1.matches());
 assertEquals("property1", pointer1.getMatchingProperty());
 assertNull(pointer1.tail());

 assertTrue(pointer2.matches());
 assertEquals("property2", pointer2.getMatchingProperty());
 assertNull(pointer2.tail());

 assertTrue(pointer3.matches());
 assertEquals("property3", pointer3.getMatchingProperty());
 assertNull(pointer3.tail());

 assertTrue(pointer4.matches());
 assertEquals("property4", pointer4.getMatchingProperty());
 assertNull(pointer4.tail());

 assertEquals(pointer1, compoundPointer.tail().tail().tail().tail());
 }
}