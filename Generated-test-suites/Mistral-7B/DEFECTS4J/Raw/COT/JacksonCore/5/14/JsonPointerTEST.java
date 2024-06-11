package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

 private static final String EMPTY_POINTER = "";
 private static final String ROOT_POINTER = "/";
 private static final String PROPERTY_POINTER = "/propertyName";
 private static final String PROPERTY_AND_INDEX_POINTER = "/propertyName/0";
 private static final String INVALID_POINTER = "/invalid";
 private static final String OUT_OF_RANGE_INDEX_POINTER = "/array/100";
 private static final String ESCAPED_POINTER = "/~0~1~2";

 @Test
 public void testEmptyPointer() {
     JsonPointer emptyPointer = JsonPointer.EMPTY;
     assertTrue(emptyPointer.matches());
     assertFalse(emptyPointer.mayMatchProperty());
     assertFalse(emptyPointer.mayMatchElement());
     assertEquals("", emptyPointer.toString());
     assertEquals(emptyPointer, emptyPointer);
 }

 @Test
 public void testRootPointer() {
     JsonPointer rootPointer = JsonPointer.valueOf(ROOT_POINTER);
     assertFalse(rootPointer.matches());
     assertFalse(rootPointer.mayMatchProperty());
     assertFalse(rootPointer.mayMatchElement());
     assertEquals(ROOT_POINTER, rootPointer.toString());
     assertEquals(rootPointer, rootPointer);
 }

 @Test
 public void testPropertyPointer() {
     JsonPointer propertyPointer = JsonPointer.valueOf(PROPERTY_POINTER);
     assertTrue(propertyPointer.mayMatchProperty());
     assertEquals("propertyName", propertyPointer.getMatchingProperty());
     assertFalse(propertyPointer.mayMatchElement());
     assertNull(propertyPointer.tail());
     assertEquals(PROPERTY_POINTER, propertyPointer.toString());
     assertEquals(propertyPointer, propertyPointer);
 }

 @Test
 public void testPropertyAndIndexPointer() {
     JsonPointer propertyAndIndexPointer = JsonPointer.valueOf(PROPERTY_AND_INDEX_POINTER);
     assertTrue(propertyAndIndexPointer.mayMatchProperty());
     assertEquals("propertyName", propertyAndIndexPointer.getMatchingProperty());
     assertTrue(propertyAndIndexPointer.mayMatchElement());
     assertEquals(0, propertyAndIndexPointer.getMatchingIndex());
     assertSame(propertyAndIndexPointer, propertyAndIndexPointer.tail());
     assertEquals(PROPERTY_AND_INDEX_POINTER, propertyAndIndexPointer.toString());
     assertEquals(propertyAndIndexPointer, propertyAndIndexPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
     JsonPointer invalidPointer = null;
     try {
         invalidPointer = JsonPointer.compile(INVALID_POINTER);
     } catch (IllegalArgumentException e) {
         assertEquals("Invalid input: JSON Pointer expression must start with '/': "+"\""+INVALID_POINTER+"\"", e.getMessage());
     }
     assertNull(invalidPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutOfRangeIndexPointer() {
     JsonPointer outOfRangeIndexPointer = null;
     try {
         outOfRangeIndexPointer = JsonPointer.compile(OUT_OF_RANGE_INDEX_POINTER);
     } catch (IllegalArgumentException e) {
         assertEquals("Invalid input: JSON Pointer expression must start with '/': "+"\""+OUT_OF_RANGE_INDEX_POINTER+"\"", e.getMessage());
     }
     assertNull(outOfRangeIndexPointer);
 }

 @Test
 public void testEscapedPointer() {
     JsonPointer escapedPointer = JsonPointer.valueOf(ESCAPED_POINTER);
     assertTrue(escapedPointer.mayMatchProperty());
     assertEquals("~0~1~2", escapedPointer.getMatchingProperty());
     assertFalse(escapedPointer.mayMatchElement());
     assertNull(escapedPointer.tail());
     assertEquals(ESCAPED_POINTER, escapedPointer.toString());
     assertEquals(escapedPointer, escapedPointer);
 }

}