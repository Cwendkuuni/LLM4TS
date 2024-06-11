package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer jsonPointer = JsonPointer.EMPTY;
 assertEquals("", jsonPointer.toString());
 assertNull(jsonPointer.tail());
 assertFalse(jsonPointer.matches());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 }

 @Test
 public void testBasicConstructor() {
 String fullString = "/test/property1/property2";
 String segment = "property1";
 JsonPointer nextPointer = new JsonPointer("/test/property2", "property2", JsonPointer.EMPTY);
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextPointer);
 assertEquals(fullString, jsonPointer.toString());
 assertSame(nextPointer, jsonPointer.tail());
 assertEquals("property1", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 assertFalse(jsonPointer.matches());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 }

 @Test
 public void testCompileMethod() throws IllegalArgumentException {
 String input = "/test/property1/property2";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals(input, jsonPointer.toString());
 assertSame(JsonPointer.EMPTY, jsonPointer.tail());
 assertEquals("property1", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 assertFalse(jsonPointer.matches());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompileInvalidInput() throws IllegalArgumentException {
 String input = "test/property1/property2";
 JsonPointer.compile(input);
 }

 @Test
 public void testTailMethod() {
 JsonPointer jsonPointer = new JsonPointer("/test/property1/property2", "property1", JsonPointer.EMPTY);
 JsonPointer tail = jsonPointer.tail();
 assertEquals("/property2", tail.toString());
 assertNull(tail.tail());
 assertFalse(tail.matches());
 assertTrue(tail.mayMatchProperty());
 assertFalse(tail.mayMatchElement());
 }

 @Test
 public void testMatchesMethod() {
 JsonPointer jsonPointer1 = new JsonPointer("/test/property1", "property1", JsonPointer.EMPTY);
 JsonPointer jsonPointer2 = new JsonPointer("/test/property1/property2", "property1", JsonPointer.EMPTY);
 JsonPointer jsonPointer3 = new JsonPointer("/test/property2", "property2", JsonPointer.EMPTY);
 assertTrue(jsonPointer1.matches());
 assertFalse(jsonPointer2.matches());
 assertFalse(jsonPointer3.matches());
 }

 @Test
 public void testGetMatchingPropertyMethod() {
 JsonPointer jsonPointer = new JsonPointer("/test/property1/property2", "property1", JsonPointer.EMPTY);
 assertEquals("property1", jsonPointer.getMatchingProperty());
 assertNull(jsonPointer.tail().getMatchingProperty());
 }

 @Test
 public void testGetMatchingIndexMethod() {
 JsonPointer jsonPointer = new JsonPointer("/test/property1/property2", "property1", JsonPointer.EMPTY);
 assertEquals(0, jsonPointer.getMatchingIndex());
 assertNull(jsonPointer.tail().getMatchingIndex());
 }

 @Test
 public void testMatchPropertyMethod() {
 JsonPointer jsonPointer1 = new JsonPointer("/test/property1/property2", "property1", JsonPointer.EMPTY);
 JsonPointer jsonPointer2 = new JsonPointer("/test/property1", "property1", JsonPointer.EMPTY);
 JsonPointer jsonPointer3 = new JsonPointer("/test/property2", "property2", JsonPointer.EMPTY);
 JsonPointer jsonPointer4 = new JsonPointer("/test/property1/property3", "property1", JsonPointer.EMPTY);
 assertEquals(jsonPointer2, jsonPointer1.matchProperty("property1"));
 assertNull(jsonPointer1.matchProperty("property2"));
 assertNull(jsonPointer2.matchProperty("property2"));
 assertNull(jsonPointer3.matchProperty("property1"));
 assertNull(jsonPointer4.matchProperty("property1"));
 assertNull(jsonPointer4.matchProperty("property3"));
 }

 @Test
 public void testMatchElementMethod() {
 JsonPointer jsonPointer1 = new JsonPointer("/test/property1/property2/0", "property1", JsonPointer.EMPTY);
 JsonPointer jsonPointer2 = new JsonPointer("/test/property1/property2/1", "property1", JsonPointer.EMPTY);
 JsonPointer jsonPointer3 = new JsonPointer("/test/property1", "property1", JsonPointer.EMPTY);
 assertEquals(jsonPointer1, jsonPointer1.matchElement(0));
 assertEquals(jsonPointer2, jsonPointer2.matchElement(1));
 assertNull(jsonPointer1.matchElement(-1));
 assertNull(jsonPointer1.matchElement(1));
 assertNull(jsonPointer2.matchElement(0));
 assertNull(jsonPointer2.matchElement(-1));
 assertNull(jsonPointer3.matchElement(0));
 }
}