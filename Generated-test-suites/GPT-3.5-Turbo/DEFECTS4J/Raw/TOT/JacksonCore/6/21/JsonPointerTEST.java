package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

  @Test
  public void testMatches() {
    // Test cases for matches()
    JsonPointer jsonPointer = JsonPointer.compile("");

    assertTrue(jsonPointer.matches());

    jsonPointer = JsonPointer.compile("/book/0/title");

    assertFalse(jsonPointer.matches());
  }

  @Test
  public void testGetMatchingProperty() {
    // Test cases for getMatchingProperty()
    JsonPointer jsonPointer = JsonPointer.compile("/book");

    assertEquals("book", jsonPointer.getMatchingProperty());
  }

  @Test
  public void testGetMatchingIndex() {
    // Test cases for getMatchingIndex()
    JsonPointer jsonPointer = JsonPointer.compile("/books/0");

    assertEquals(0, jsonPointer.getMatchingIndex());
  }

  @Test
  public void testMayMatchProperty() {
    // Test cases for mayMatchProperty()
    JsonPointer jsonPointer = JsonPointer.compile("/book");

    assertTrue(jsonPointer.mayMatchProperty());
  }

  @Test
  public void testMayMatchElement() {
    // Test cases for mayMatchElement()
    JsonPointer jsonPointer = JsonPointer.compile("/books/0");

    assertTrue(jsonPointer.mayMatchElement());
  }

  @Test
  public void testMatchProperty() {
    // Test cases for matchProperty()
    JsonPointer jsonPointer = JsonPointer.compile("/book");

    JsonPointer nextSegment = jsonPointer.matchProperty("book");

    assertNotNull(nextSegment);

    nextSegment = jsonPointer.matchProperty("author");

    assertNull(nextSegment);
  }

  @Test
  public void testMatchElement() {
    // Test cases for matchElement()
    JsonPointer jsonPointer = JsonPointer.compile("/books/0");

    JsonPointer nextSegment = jsonPointer.matchElement(0);

    assertNotNull(nextSegment);

    nextSegment = jsonPointer.matchElement(1);

    assertNull(nextSegment);
  }

  @Test
  public void testTail() {
    // Test cases for tail()
    JsonPointer jsonPointer = JsonPointer.compile("/books/0");

    JsonPointer nextSegment = jsonPointer.tail();

    assertNotNull(nextSegment);
  }

  @Test
  public void testToString() {
    // Test cases for toString()
    JsonPointer jsonPointer = JsonPointer.compile("/books/0");

    assertEquals("/books/0", jsonPointer.toString());
  }

  @Test
  public void testHashCode() {
    // Test cases for hashCode()
    JsonPointer jsonPointer = JsonPointer.compile("/books/0");

    assertEquals("/books/0".hashCode(), jsonPointer.hashCode());
  }

  @Test
  public void testEquals() {
    // Test cases for equals()
    JsonPointer jsonPointer1 = JsonPointer.compile("/books/0");
    JsonPointer jsonPointer2 = JsonPointer.compile("/books/0");
    JsonPointer jsonPointer3 = JsonPointer.compile("/authors/0");

    assertTrue(jsonPointer1.equals(jsonPointer2));
    assertFalse(jsonPointer1.equals(jsonPointer3));
  }
}