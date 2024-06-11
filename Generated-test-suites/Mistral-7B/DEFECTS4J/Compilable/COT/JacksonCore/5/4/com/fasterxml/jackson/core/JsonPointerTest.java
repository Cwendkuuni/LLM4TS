package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 private JsonPointer emptyPointer = JsonPointer.EMPTY;
 private JsonPointer validPointer;

 @Test
 public void testCompilerWithEmptyString() {
 validPointer = JsonPointer.compile("");
 assertEquals(JsonPointer.EMPTY, validPointer);
 }

 @Test
 public void testCompilerWithValidJsonPointer() {
 validPointer = JsonPointer.compile("/foo/bar/1");
 assertFalse(emptyPointer.matches());
 assertEquals("/foo/bar/1", validPointer.toString());
 assertEquals("bar", validPointer.getMatchingProperty());
 assertEquals(1, validPointer.getMatchingIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompilerWithInvalidJsonPointer() {
 String invalidInput = "InvalidInput";
 JsonPointer invalidPointer = null;
 try {
 invalidPointer = JsonPointer.compile(invalidInput);
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid input: JSON Pointer expression must start with '/': \"" + invalidInput + "\"", e.getMessage());
 }
 }

 @Test
 public void testMatcherWithNullPointer() {
 assertNull(emptyPointer.matchProperty(null));
 }

 @Test
 public void testMatcherWithNullPropertyName() {
 assertNull(emptyPointer.matchProperty(null));
 }

 @Test
 public void testMatcherWithEmptyPropertyName() {
 JsonPointer emptyPropertyPointer = new JsonPointer("/", "", emptyPointer);
 JsonPointer emptyPropertyMatched = emptyPropertyPointer.matchProperty("");
 assertEquals(emptyPointer, emptyPropertyMatched);
 }

 @Test
 public void testMatcherWithNonMatchingPropertyName() {
 JsonPointer nonMatchingPropertyPointer = new JsonPointer("/foo/bar", "baz", emptyPointer);
 assertNull(nonMatchingPropertyPointer.matchProperty("bar"));
 }

 @Test
 public void testMatcherWithMatchingPropertyNameIndex() {
 JsonPointer matchingPropertyPointer = new JsonPointer("/foo/bar/1", "bar", emptyPointer);
 JsonPointer matchedPropertyPointer = matchingPropertyPointer.matchElement(1);
 assertEquals(matchingPropertyPointer, matchedPropertyPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatcherWithNegativeIndex() {
 JsonPointer negativeIndexPointer = new JsonPointer("/foo/bar/1", "bar", emptyPointer);
 assertNull(negativeIndexPointer.matchElement(-1));
 }

 @Test
 public void testTailMethodWithEmptyPointer() {
 assertEquals(emptyPointer, emptyPointer.tail());
 }

 @Test
 public void testTailMethodWithNonEmptyPointer() {
 JsonPointer nonEmptyPointer = new JsonPointer("/foo/bar/1", "bar", emptyPointer);
 assertEquals(emptyPointer, nonEmptyPointer.tail());
 }

 @Test
 public void testToStringMethodWithEmptyPointer() {
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testToStringMethodWithNonEmptyPointer() {
 assertEquals("/foo/bar/1", validPointer.toString());
 }

 @Test
 public void testEqualsMethodWithSamePointer() {
 JsonPointer samePointer = new JsonPointer("/foo/bar/1", "bar", emptyPointer);
 assertEquals(validPointer, samePointer);
 }

 @Test
 public void testEqualsMethodWithDifferentPointer() {
 JsonPointer differentPointer = new JsonPointer("/foo/qux/1", "qux", emptyPointer);
 assertNotEquals(validPointer, differentPointer);
 }

 @Test
 public void testEqualsMethodWithNullObject() {
 assertNotEquals(validPointer, null);
 }

 @Test
 public void testEqualsMethodWithDifferentClass() {
 String differentClass = "DifferentClass";
 assertNotEquals(validPointer, differentClass);
 }

 @Test
 public void testHashCodeMethodWithSameObject() {
 int hashCode = validPointer.hashCode();
 JsonPointer sameObject = new JsonPointer("/foo/bar/1", "bar", emptyPointer);
 assertEquals(hashCode, sameObject.hashCode());
 }

 @Test
 public void testHashCodeMethodWithDifferentObject() {
 JsonPointer differentObject = new JsonPointer("/foo/qux/1", "qux", emptyPointer);
 int hashCode = validPointer.hashCode();
 int differentHashCode = differentObject.hashCode();
 assertNotEquals(hashCode, differentHashCode);
 }
}