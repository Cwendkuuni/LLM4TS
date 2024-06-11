package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 // Test constructor for empty instance
 @Test
 public void testEmptyConstructor() {
 JsonPointer jsonPointer = new JsonPointer();
 assertTrue(jsonPointer.matches());
 assertEquals("", jsonPointer.toString());
 assertNull(jsonPointer.getMatchingProperty());
 assertNull(jsonPointer.getMatchingIndex());
 }

 // Test constructor for non-empty instance
 @Test
 public void testNonEmptyConstructor() {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = new JsonPointer(pointerString, "a", new JsonPointer(pointerString.substring(3), "b", JsonPointer.EMPTY));
 assertTrue(jsonPointer.matches());
 assertEquals(pointerString, jsonPointer.toString());
 assertEquals("a", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 // Test method compile with valid input
 @Test
 public void testCompileValidInput() throws IllegalArgumentException {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = JsonPointer.compile(pointerString);
 assertTrue(jsonPointer.matches());
 assertEquals(pointerString, jsonPointer.toString());
 assertEquals("a", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 // Test method compile with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testCompileInvalidInput() throws IllegalArgumentException {
 JsonPointer.compile("invalidPointerString");
 }

 // Test method valueOf with valid input
 @Test
 public void testValueOfValidInput() throws IllegalArgumentException {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = JsonPointer.valueOf(pointerString);
 assertTrue(jsonPointer.matches());
 assertEquals(pointerString, jsonPointer.toString());
 assertEquals("a", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 // Test method valueOf with null input
 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNullInput() throws IllegalArgumentException {
 JsonPointer.valueOf(null);
 }

 // Test method tail for empty instance
 @Test
 public void testTailEmptyInstance() {
 JsonPointer jsonPointer = new JsonPointer();
 assertNull(jsonPointer.tail());
 }

 // Test method tail for non-empty instance
 @Test
 public void testTailNonEmptyInstance() {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = new JsonPointer(pointerString, "a", new JsonPointer(pointerString.substring(3), "b", JsonPointer.EMPTY));
 JsonPointer expected = new JsonPointer(pointerString.substring(3), "b", JsonPointer.EMPTY);
 JsonPointer actual = jsonPointer.tail();
 assertSame(expected, actual);
 }

 // Test method matches for empty instance
 @Test
 public void testMatchesEmptyInstance() {
 JsonPointer jsonPointer = new JsonPointer();
 assertTrue(jsonPointer.matches());
 }

 // Test method matches for non-empty instance
 @Test
 public void testMatchesNonEmptyInstance() {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = new JsonPointer(pointerString, "a", new JsonPointer(pointerString.substring(3), "b", JsonPointer.EMPTY));
 assertFalse(jsonPointer.matches());
 }

 // Test method mayMatchProperty for empty instance
 @Test
 public void testMayMatchPropertyEmptyInstance() {
 JsonPointer jsonPointer = new JsonPointer();
 assertFalse(jsonPointer.mayMatchProperty());
 }

 // Test method mayMatchProperty for non-empty instance
 @Test
 public void testMayMatchPropertyNonEmptyInstance() {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = new JsonPointer(pointerString, "a", new JsonPointer(pointerString.substring(3), "b", JsonPointer.EMPTY));
 assertTrue(jsonPointer.mayMatchProperty());
 }

 // Test method mayMatchElement for empty instance
 @Test
 public void testMayMatchElementEmptyInstance() {
 JsonPointer jsonPointer = new JsonPointer();
 assertFalse(jsonPointer.mayMatchElement());
 }

 // Test method mayMatchElement for non-empty instance
 @Test
 public void testMayMatchElementNonEmptyInstance() {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = new JsonPointer(pointerString, "a", new JsonPointer(pointerString.substring(3), "b", JsonPointer.EMPTY));
 assertTrue(jsonPointer.mayMatchElement());
 }

 // Test method matchProperty for empty instance
 @Test
 public void testMatchPropertyEmptyInstance() {
 JsonPointer jsonPointer = new JsonPointer();
 assertNull(jsonPointer.matchProperty(""));
 }

 // Test method matchProperty for non-matching property
 @Test
 public void testMatchPropertyNonMatchingProperty() {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = new JsonPointer(pointerString, "a", new JsonPointer(pointerString.substring(3), "b", JsonPointer.EMPTY));
 assertNull(jsonPointer.matchProperty("c"));
 }

 // Test method matchProperty for matching property
 @Test
 public void testMatchPropertyMatchingProperty() {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = new JsonPointer(pointerString, "a", new JsonPointer(pointerString.substring(3), "b", JsonPointer.EMPTY));
 JsonPointer expected = new JsonPointer("/b", null, JsonPointer.EMPTY);
 JsonPointer actual = jsonPointer.matchProperty("b");
 assertSame(expected, actual);
 }

 // Test method matchElement for negative index
 @Test
 public void testMatchElementNegativeIndex() {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = new JsonPointer(pointerString, "a", new JsonPointer(pointerString.substring(3), "b", JsonPointer.EMPTY));
 assertNull(jsonPointer.matchElement(-1));
 }

 // Test method matchElement for non-matching index
 @Test
 public void testMatchElementNonMatchingIndex() {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = new JsonPointer(pointerString, "a", new JsonPointer(pointerString.substring(3), "b", JsonPointer.EMPTY));
 assertNull(jsonPointer.matchElement(2));
 }

 // Test method matchElement for matching index
 @Test
 public void testMatchElementMatchingIndex() {
 String pointerString = "/a/0/b/1";
 JsonPointer jsonPointer = new JsonPointer(pointerString, "a", new JsonPointer(pointerString.substring(3), "b", JsonPointer.EMPTY));
 JsonPointer expected = new JsonPointer("/b", null, JsonPointer.EMPTY);
 JsonPointer actual = jsonPointer.matchElement(1);
 assertSame(expected, actual);
 }
}