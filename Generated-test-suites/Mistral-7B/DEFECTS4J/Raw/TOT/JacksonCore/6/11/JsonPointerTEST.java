package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 // Test for empty pointer
 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertEquals("", emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.tail());
 }

 // Test for simple pointer
 @Test
 public void testSimplePointer() {
 JsonPointer simplePointer = JsonPointer.compile("/test");
 assertTrue(simplePointer.matches());
 assertEquals("test", simplePointer.getMatchingProperty());
 assertNull(simplePointer.tail());
 }

 // Test for pointer with property
 @Test
 public void testPropertyPointer() {
 JsonPointer propertyPointer = JsonPointer.compile("/test/property");
 assertTrue(propertyPointer.matches());
 assertEquals("property", propertyPointer.getMatchingProperty());
 assertNull(propertyPointer.tail());
 }

 // Test for pointer with index
 @Test
 public void testIndexPointer() {
 JsonPointer indexPointer = JsonPointer.compile("/test/0");
 assertTrue(indexPointer.matches());
 assertEquals(0, indexPointer.getMatchingIndex());
 assertNull(indexPointer.tail());
 }

 // Test for pointer with property and index
 @Test
 public void testPropertyAndIndexPointer() {
 JsonPointer propertyAndIndexPointer = JsonPointer.compile("/test/property/0");
 assertTrue(propertyAndIndexPointer.matches());
 assertEquals("property", propertyAndIndexPointer.getMatchingProperty());
 assertEquals(0, propertyAndIndexPointer.getMatchingIndex());
 assertNull(propertyAndIndexPointer.tail());
 }

 // Test for pointer with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputPointer() {
 JsonPointer invalidInputPointer = JsonPointer.compile("test");
 }

 // Test for pointer with no leading slash
 @Test(expected = IllegalArgumentException.class)
 public void testNoLeadingSlashPointer() {
 JsonPointer noLeadingSlashPointer = JsonPointer.compile("test/");
 }

 // Test for pointer with multiple segments
 @Test
 public void testMultipleSegmentsPointer() {
 JsonPointer multipleSegmentsPointer = JsonPointer.compile("/test/property1/property2");
 assertEquals("property1", multipleSegmentsPointer.getMatchingProperty());
 assertNull(multipleSegmentsPointer.tail());
 JsonPointer nextSegment = multipleSegmentsPointer.tail();
 assertEquals("property2", nextSegment.getMatchingProperty());
 assertNull(nextSegment.tail());
 }

 // Test for pointer with quote
 @Test
 public void testQuotePointer() {
 JsonPointer quotePointer = JsonPointer.compile("/test/property~1");
 assertEquals("property~1", quotePointer.getMatchingProperty());
 assertNull(quotePointer.tail());
 }

 // Test for pointer with multiple quotes
 @Test
 public void testMultipleQuotesPointer() {
 JsonPointer multipleQuotesPointer = JsonPointer.compile("/test/property~1/property~2");
 assertEquals("property~1", multipleQuotesPointer.getMatchingProperty());
 assertNull(multipleQuotesPointer.tail());
 JsonPointer nextSegment = multipleQuotesPointer.tail();
 assertEquals("property~2", nextSegment.getMatchingProperty());
 assertNull(nextSegment.tail());
 }

 // Test for pointer with invalid escape
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEscapePointer() {
 JsonPointer invalidEscapePointer = JsonPointer.compile("/test/property~");
 }

 // Test for pointer with empty segments
 @Test
 public void testEmptySegmentsPointer() {
 JsonPointer emptySegmentsPointer = JsonPointer.compile("/test/property//");
 assertEquals("property", emptySegmentsPointer.getMatchingProperty());
 assertNull(emptySegmentsPointer.tail());
 }

}