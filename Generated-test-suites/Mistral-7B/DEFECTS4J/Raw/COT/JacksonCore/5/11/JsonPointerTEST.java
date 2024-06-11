package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 // Test constructor for empty instance
 @Test
 public void testEmptyConstructor() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 assertTrue(emptyJsonPointer.matches());
 assertEquals("", emptyJsonPointer.getMatchingProperty());
 assertEquals(-1, emptyJsonPointer.getMatchingIndex());
 }

 // Test constructor for non-empty instance
 @Test
 public void testNonEmptyConstructor() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 JsonPointer nextJsonPointer = new JsonPointer("/test/property2", "", null);
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
 assertEquals(fullString, jsonPointer.toString());
 assertFalse(jsonPointer.matches());
 assertEquals(segment, jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 }

 // Test compile factory method
 @Test
 public void testCompileFactoryMethod() throws IllegalArgumentException {
 String input = "/test/property1/0";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals(input, jsonPointer.toString());
 assertFalse(jsonPointer.matches());
 assertEquals("property1", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 }

 // Test valueOf factory method
 @Test
 public void testValueOfFactoryMethod() throws IllegalArgumentException {
 String input = "/test/property1/0";
 JsonPointer jsonPointer = JsonPointer.valueOf(input);
 assertEquals(input, jsonPointer.toString());
 assertFalse(jsonPointer.matches());
 assertEquals("property1", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 }

 // Test fromSegment factory method
 //TODO: Implement and test fromSegment factory method

 // Test matches method
 @Test
 public void testMatchesMethod() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 JsonPointer nonEmptyJsonPointer = new JsonPointer("/test/property1/0", "", null);
 assertTrue(emptyJsonPointer.matches());
 assertFalse(nonEmptyJsonPointer.matches());
 }

 // Test getMatchingProperty method with empty instance
 @Test
 public void testGetMatchingPropertyWithEmptyInstance() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 assertEquals("", emptyJsonPointer.getMatchingProperty());
 }

 // Test getMatchingProperty method with non-empty instance
 @Test
 public void testGetMatchingPropertyWithNonEmptyInstance() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 JsonPointer nextJsonPointer = new JsonPointer("/test/property2", "", null);
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
 assertEquals(segment, jsonPointer.getMatchingProperty());
 }

 // Test getMatchingIndex method with empty instance
 @Test
 public void testGetMatchingIndexWithEmptyInstance() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 assertEquals(-1, emptyJsonPointer.getMatchingIndex());
 }

 // Test getMatchingIndex method with non-empty instance
 @Test
 public void testGetMatchingIndexWithNonEmptyInstance() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 JsonPointer nextJsonPointer = new JsonPointer("/test/property2", "", null);
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 // Test mayMatchProperty method with empty instance
 @Test
 public void testMayMatchPropertyWithEmptyInstance() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 assertFalse(emptyJsonPointer.mayMatchProperty());
 }

 // Test mayMatchProperty method with non-empty instance
 @Test
 public void testMayMatchPropertyWithNonEmptyInstance() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 JsonPointer nextJsonPointer = new JsonPointer("/test/property2", "", null);
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
 assertTrue(jsonPointer.mayMatchProperty());
 }

 // Test mayMatchElement method with empty instance
 @Test
 public void testMayMatchElementWithEmptyInstance() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 assertFalse(emptyJsonPointer.mayMatchElement());
 }

 // Test mayMatchElement method with non-empty instance
 @Test
 public void testMayMatchElementWithNonEmptyInstance() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 JsonPointer nextJsonPointer = new JsonPointer("/test/property2", "", null);
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
 assertTrue(jsonPointer.mayMatchElement());
 }

 // Test matchProperty method
 @Test
 public void testMatchPropertyMethod() {
 String propertyName = "property1";
 JsonPointer jsonPointer = new JsonPointer("/test/property1/0", "", null);
 JsonPointer expectedJsonPointer = new JsonPointer("/test/property1", "", null);
 JsonPointer resultJsonPointer = jsonPointer.matchProperty(propertyName);
 assertEquals(expectedJsonPointer, resultJsonPointer);
 }

 // Test matchProperty method when no match found
 @Test
 public void testMatchPropertyMethodNoMatchFound() {
 String propertyName = "property2";
 JsonPointer jsonPointer = new JsonPointer("/test/property1/0", "", null);
 assertNull(jsonPointer.matchProperty(propertyName));
 }

 // Test matchElement method
 @Test
 public void testMatchElementMethod() {
 int index = 0;
 JsonPointer jsonPointer = new JsonPointer("/test/property1/0", "", null);
 JsonPointer expectedJsonPointer = new JsonPointer("/test/property1", "", null);
 JsonPointer resultJsonPointer = jsonPointer.matchElement(index);
 assertEquals(expectedJsonPointer, resultJsonPointer);
 }

 // Test matchElement method when no match found and index is negative
 @Test
 public void testMatchElementMethodNegativeIndex() {
 int index = -1;
 JsonPointer jsonPointer = new JsonPointer("/test/property1/0", "", null);
 assertNull(jsonPointer.matchElement(index));
 }

 // Test matchElement method when no match found and index is not equal to matching index
 @Test
 public void testMatchElementMethodDifferentIndex() {
 String fullString = "/test/property1/1";
 String segment = "property1";
 JsonPointer nextJsonPointer = new JsonPointer("/test/property2", "", null);
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
 int index = 0;
 JsonPointer expectedJsonPointer = null;
 JsonPointer resultJsonPointer = jsonPointer.matchElement(index);
 assertEquals(expectedJsonPointer, resultJsonPointer);
 }

 // Test tail method with empty instance
 @Test
 public void testTailMethodWithEmptyInstance() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 assertEquals(emptyJsonPointer, emptyJsonPointer.tail());
 }

 // Test tail method with non-empty instance
 @Test
 public void testTailMethodWithNonEmptyInstance() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 JsonPointer nextJsonPointer = new JsonPointer("/test/property2", "", null);
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
 JsonPointer expectedJsonPointer = nextJsonPointer;
 JsonPointer resultJsonPointer = jsonPointer.tail();
 assertEquals(expectedJsonPointer, resultJsonPointer);
 }

 // Test toString method
 @Test
 public void testToStringMethod() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 JsonPointer nextJsonPointer = new JsonPointer("/test/property2", "", null);
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
 assertEquals(fullString, jsonPointer.toString());
 }

 // Test hashCode method
 @Test
 public void testHashCodeMethod() {
 String fullString1 = "/test/property1/0";
 String fullString2 = "/test/property1/1";
 String segment = "property1";
 JsonPointer jsonPointer1 = new JsonPointer(fullString1, segment, null);
 JsonPointer jsonPointer2 = new JsonPointer(fullString2, segment, null);
 int expectedHashCode1 = fullString1.hashCode();
 int expectedHashCode2 = fullString2.hashCode();
 int resultHashCode1 = jsonPointer1.hashCode();
 int resultHashCode2 = jsonPointer2.hashCode();
 assertEquals(expectedHashCode1, resultHashCode1);
 assertEquals(expectedHashCode2, resultHashCode2);
 }

 // Test equals method with same object
 @Test
 public void testEqualsMethodWithSameObject() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 JsonPointer jsonPointer1 = new JsonPointer(fullString, segment, null);
 JsonPointer jsonPointer2 = jsonPointer1;
 assertTrue(jsonPointer1.equals(jsonPointer2));
 }

 // Test equals method with other object of same class
 @Test
 public void testEqualsMethodWithOtherObjectOfSameClass() {
 String fullString1 = "/test/property1/0";
 String fullString2 = "/test/property1/0";
 String segment = "property1";
 JsonPointer jsonPointer1 = new JsonPointer(fullString1, segment, null);
 JsonPointer jsonPointer2 = new JsonPointer(fullString2, segment, null);
 assertTrue(jsonPointer1.equals(jsonPointer2));
 }

 // Test equals method with other object of different class
 @Test
 public void testEqualsMethodWithOtherObjectOfDifferentClass() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, null);
 Object otherObject = new Object();
 assertFalse(jsonPointer.equals(otherObject));
 }

 // Test equals method with null object
 @Test
 public void testEqualsMethodWithNullObject() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, null);
 assertFalse(jsonPointer.equals(null));
 }

 // Test equals method with same string
 @Test
 public void testEqualsMethodWithSameString() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 String fullStringExpected = "/test/property1/0";
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, null);
 JsonPointer expectedJsonPointer = new JsonPointer(fullStringExpected, segment, null);
 assertTrue(jsonPointer.equals(expectedJsonPointer));
 }

 // Test equals method with different string
 @Test
 public void testEqualsMethodWithDifferentString() {
 String fullString1 = "/test/property1/0";
 String fullString2 = "/test/property2/0";
 String segment = "property1";
 JsonPointer jsonPointer1 = new JsonPointer(fullString1, segment, null);
 JsonPointer jsonPointer2 = new JsonPointer(fullString2, segment, null);
 assertFalse(jsonPointer1.equals(jsonPointer2));
 }
}