package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 private static final String VALID_JSON_POINTER = "/test/property1/2";
 private static final String VALID_EMPTY_JSON_POINTER = "/";
 private static final String INVALID_JSON_POINTER = "~test/property1/2";
 private static final String INVALID_EMPTY_JSON_POINTER = "test/";

 @Test
 public void testEmptyJsonPointerConstruction() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 assertEquals("", emptyJsonPointer.toString());
 assertNull(emptyJsonPointer.tail());
 assertFalse(emptyJsonPointer.matches());
 }

 @Test
 public void testJsonPointerConstructionWithValidInput() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER);
 assertEquals(VALID_JSON_POINTER, jsonPointer.toString());
 assertNull(jsonPointer.tail());
 assertFalse(jsonPointer.matches());
 assertEquals("property1", jsonPointer.getMatchingProperty());
 assertEquals(2, jsonPointer.getMatchingIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidInput() {
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(INVALID_JSON_POINTER);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertNull(jsonPointer);
 }

 @Test
 public void testJsonPointerCompilationWithEmptyInput() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_EMPTY_JSON_POINTER);
 assertEquals(VALID_EMPTY_JSON_POINTER, jsonPointer.toString());
 assertNull(jsonPointer.tail());
 assertTrue(jsonPointer.matches());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidEmptyInput() {
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(INVALID_EMPTY_JSON_POINTER);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertNull(jsonPointer);
 }

 @Test
 public void testJsonPointerTail() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER);
 JsonPointer tailJsonPointer = jsonPointer.tail();
 assertEquals("test/property1", jsonPointer.toString());
 assertEquals("property1/2", tailJsonPointer.toString());
 assertFalse(jsonPointer.matches());
 assertFalse(tailJsonPointer.matches());
 }

 @Test
 public void testJsonPointerMatches() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER);
 assertTrue(jsonPointer.matches());
 }

 @Test
 public void testJsonPointerGetMatchingProperty() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER);
 assertEquals("property1", jsonPointer.getMatchingProperty());
 }

 @Test
 public void testJsonPointerGetMatchingIndex() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER);
 assertEquals(2, jsonPointer.getMatchingIndex());
 }

 @Test
 public void testJsonPointerMayMatchProperty() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER);
 assertTrue(jsonPointer.mayMatchProperty());
 }

 @Test
 public void testJsonPointerMayMatchElement() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER);
 assertTrue(jsonPointer.mayMatchElement());
 }

 @Test
 public void testJsonPointerMatchProperty() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER);
 JsonPointer propertyJsonPointer = new JsonPointer("/test/property1");
 assertTrue(jsonPointer.matchProperty("property1") == propertyJsonPointer);
 assertNull(jsonPointer.matchProperty("nonExistentProperty"));
 }

 @Test
 public void testJsonPointerMatchElement() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER);
 JsonPointer elementJsonPointer = new JsonPointer("/test/property1/1");
 assertTrue(jsonPointer.matchElement(1) == elementJsonPointer);
 assertNull(jsonPointer.matchElement(0));
 }

}