package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

  @Test
  public void testAreEqual_WithNullObjects_ReturnsTrue() {
    // Arrange
    Object o1 = null;
    Object o2 = null;

    // Act
    boolean result = Equality.areEqual(o1, o2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testAreEqual_WithArrayObjects_ReturnsTrue() {
    // Arrange
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3};

    // Act
    boolean result = Equality.areEqual(arr1, arr2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testAreEqual_WithDifferentArrayLength_ReturnsFalse() {
    // Arrange
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3, 4};

    // Act
    boolean result = Equality.areEqual(arr1, arr2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testAreEqual_WithDifferentArrayElements_ReturnsFalse() {
    // Arrange
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 4};

    // Act
    boolean result = Equality.areEqual(arr1, arr2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testAreEqual_WithEqualObjects_ReturnsTrue() {
    // Arrange
    Object o1 = "Hello";
    Object o2 = "Hello";

    // Act
    boolean result = Equality.areEqual(o1, o2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testAreEqual_WithDifferentObjects_ReturnsFalse() {
    // Arrange
    Object o1 = "Hello";
    Object o2 = "World";

    // Act
    boolean result = Equality.areEqual(o1, o2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testAreEqual_WithStringAndNullObject_ReturnsFalse() {
    // Arrange
    Object o1 = "Hello";
    Object o2 = null;

    // Act
    boolean result = Equality.areEqual(o1, o2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testAreEqual_WithNullAndStringObject_ReturnsFalse() {
    // Arrange
    Object o1 = null;
    Object o2 = "Hello";

    // Act
    boolean result = Equality.areEqual(o1, o2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testAreEqual_WithDifferentTypes_ReturnsFalse() {
    // Arrange
    Object o1 = "Hello";
    Object o2 = 123;

    // Act
    boolean result = Equality.areEqual(o1, o2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testAreArraysEqual_WithNullArrays_ReturnsTrue() {
    // Arrange
    Object o1 = null;
    Object o2 = null;

    // Act
    boolean result = Equality.areArraysEqual(o1, o2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testAreArraysEqual_WithEqualArrays_ReturnsTrue() {
    // Arrange
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3};

    // Act
    boolean result = Equality.areArraysEqual(arr1, arr2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testAreArraysEqual_WithDifferentArrayLength_ReturnsFalse() {
    // Arrange
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3, 4};

    // Act
    boolean result = Equality.areArraysEqual(arr1, arr2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testAreArraysEqual_WithDifferentArrayElements_ReturnsFalse() {
    // Arrange
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 4};

    // Act
    boolean result = Equality.areArraysEqual(arr1, arr2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testAreArrayLengthsEqual_WithEqualArrayLengths_ReturnsTrue() {
    // Arrange
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {4, 5, 6};

    // Act
    boolean result = Equality.areArrayLengthsEqual(arr1, arr2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testAreArrayLengthsEqual_WithDifferentArrayLengths_ReturnsFalse() {
    // Arrange
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3, 4};

    // Act
    boolean result = Equality.areArrayLengthsEqual(arr1, arr2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testAreArrayElementsEqual_WithEqualArrayElements_ReturnsTrue() {
    // Arrange
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3};

    // Act
    boolean result = Equality.areArrayElementsEqual(arr1, arr2);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testAreArrayElementsEqual_WithDifferentArrayElements_ReturnsFalse() {
    // Arrange
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 4};

    // Act
    boolean result = Equality.areArrayElementsEqual(arr1, arr2);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsArray_WithNullArray_ReturnsFalse() {
    // Arrange
    Object arr = null;

    // Act
    boolean result = Equality.isArray(arr);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsArray_WithArrayObject_ReturnsTrue() {
    // Arrange
    int[] arr = {1, 2, 3};

    // Act
    boolean result = Equality.isArray(arr);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsArray_WithNonArrayObject_ReturnsFalse() {
    // Arrange
    Object obj = "Hello";

    // Act
    boolean result = Equality.isArray(obj);

    // Assert
    assertFalse(result);
  }

}