package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

  @Test
  public void testCompile_ValidInput_ReturnsValidJsonPointer() {
    // Arrange
    String input = "/example";

    // Act
    JsonPointer result = JsonPointer.compile(input);

    // Assert
    assertNotNull(result);
    assertEquals(input, result.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompile_InvalidInput_ThrowsIllegalArgumentException() {
    // Arrange
    String input = "example";

    // Act
    JsonPointer.compile(input);

    // Assert
    // Exception expected
  }

  @Test
  public void testValueOf_ValidInput_ReturnsValidJsonPointer() {
    // Arrange
    String input = "/example";

    // Act
    JsonPointer result = JsonPointer.valueOf(input);

    // Assert
    assertNotNull(result);
    assertEquals(input, result.toString());
  }

  @Test
  public void testMatches_EmptyPointer_ReturnsTrue() {
    // Arrange
    JsonPointer pointer = JsonPointer.EMPTY;

    // Act
    boolean result = pointer.matches();

    // Assert
    assertTrue(result);
  }

  @Test
  public void testMatches_NonEmptyPointer_ReturnsFalse() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/example");

    // Act
    boolean result = pointer.matches();

    // Assert
    assertFalse(result);
  }

  @Test
  public void testGetMatchingProperty_ValidPointer_ReturnsMatchingProperty() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/example");

    // Act
    String result = pointer.getMatchingProperty();

    // Assert
    assertNotNull(result);
    assertEquals("example", result);
  }

  @Test
  public void testGetMatchingProperty_InvalidPointer_ReturnsNull() {
    // Arrange
    JsonPointer pointer = JsonPointer.EMPTY;

    // Act
    String result = pointer.getMatchingProperty();

    // Assert
    assertNull(result);
  }

  @Test
  public void testGetMatchingIndex_ValidPointer_ReturnsMatchingIndex() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/[0]");

    // Act
    int result = pointer.getMatchingIndex();

    // Assert
    assertEquals(0, result);
  }

  @Test
  public void testGetMatchingIndex_InvalidPointer_ReturnsNegativeOne() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/example");

    // Act
    int result = pointer.getMatchingIndex();

    // Assert
    assertEquals(-1, result);
  }

  @Test
  public void testMayMatchProperty_ValidPointer_ReturnsTrue() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/example");

    // Act
    boolean result = pointer.mayMatchProperty();

    // Assert
    assertTrue(result);
  }

  @Test
  public void testMayMatchProperty_InvalidPointer_ReturnsFalse() {
    // Arrange
    JsonPointer pointer = JsonPointer.EMPTY;

    // Act
    boolean result = pointer.mayMatchProperty();

    // Assert
    assertFalse(result);
  }

  @Test
  public void testMayMatchElement_ValidPointer_ReturnsTrue() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/[0]");

    // Act
    boolean result = pointer.mayMatchElement();

    // Assert
    assertTrue(result);
  }

  @Test
  public void testMayMatchElement_InvalidPointer_ReturnsFalse() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/example");

    // Act
    boolean result = pointer.mayMatchElement();

    // Assert
    assertFalse(result);
  }

  @Test
  public void testMatchProperty_ValidProperty_ReturnsNextSegment() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/example/next");
    String name = "example";

    // Act
    JsonPointer result = pointer.matchProperty(name);

    // Assert
    assertNotNull(result);
    assertEquals("/next", result.toString());
  }

  @Test
  public void testMatchProperty_InvalidProperty_ReturnsNull() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/example/next");
    String name = "wrong";

    // Act
    JsonPointer result = pointer.matchProperty(name);

    // Assert
    assertNull(result);
  }

  @Test
  public void testMatchElement_ValidIndex_ReturnsNextSegment() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/[0]/next");
    int index = 0;

    // Act
    JsonPointer result = pointer.matchElement(index);

    // Assert
    assertNotNull(result);
    assertEquals("/next", result.toString());
  }

  @Test
  public void testMatchElement_InvalidIndex_ReturnsNull() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/[0]/next");
    int index = 1;

    // Act
    JsonPointer result = pointer.matchElement(index);

    // Assert
    assertNull(result);
  }

  @Test
  public void testTail_ReturnsNextSegment() {
    // Arrange
    JsonPointer pointer = JsonPointer.compile("/example/next");

    // Act
    JsonPointer result = pointer.tail();

    // Assert
    assertNotNull(result);
    assertEquals("/next", result.toString());
  }

  @Test
  public void testToString_ReturnsPointerString() {
    // Arrange
    String input = "/example";
    JsonPointer pointer = JsonPointer.compile(input);

    // Act
    String result = pointer.toString();

    // Assert
    assertEquals(input, result);
  }

  @Test
  public void testHashCode_SamePointers_ReturnsSameHashCode() {
    // Arrange
    JsonPointer pointer1 = JsonPointer.compile("/example");
    JsonPointer pointer2 = JsonPointer.compile("/example");

    // Act
    int hashCode1 = pointer1.hashCode();
    int hashCode2 = pointer2.hashCode();

    // Assert
    assertEquals(hashCode1, hashCode2);
  }

  @Test
  public void testEquals_SamePointers_ReturnsTrue() {
    // Arrange
    JsonPointer pointer1 = JsonPointer.compile("/example");
    JsonPointer pointer2 = JsonPointer.compile("/example");

    // Act
    boolean result = pointer1.equals(pointer2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testEquals_DifferentPointers_ReturnsFalse() {
    // Arrange
    JsonPointer pointer1 = JsonPointer.compile("/example1");
    JsonPointer pointer2 = JsonPointer.compile("/example2");

    // Act
    boolean result = pointer1.equals(pointer2);

    // Assert
    assertFalse(result);
  }

}