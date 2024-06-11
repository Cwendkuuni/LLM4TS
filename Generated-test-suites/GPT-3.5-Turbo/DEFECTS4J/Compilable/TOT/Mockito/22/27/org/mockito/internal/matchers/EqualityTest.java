package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

  @Test
  public void testAreEqual_NullInputs_ReturnsTrue() {
    assertTrue(Equality.areEqual(null, null));
  }

  @Test
  public void testAreEqual_NullAndNonNullInputs_ReturnsFalse() {
    assertFalse(Equality.areEqual(null, new Object()));
  }

  @Test
  public void testAreEqual_NonNullAndNullInputs_ReturnsFalse() {
    assertFalse(Equality.areEqual(new Object(), null));
  }

  @Test
  public void testAreEqual_NullAndDifferentTypes_ReturnsFalse() {
    assertFalse(Equality.areEqual(null, "String"));
  }

  @Test
  public void testAreEqual_SameObject_ReturnsTrue() {
    Object obj = new Object();
    assertTrue(Equality.areEqual(obj, obj));
  }

  @Test
  public void testAreEqual_EqualArraysOfIntegers_ReturnsTrue() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3};
    assertTrue(Equality.areEqual(arr1, arr2));
  }

  @Test
  public void testAreEqual_EqualArraysOfStrings_ReturnsTrue() {
    String[] arr1 = {"Hello", "World"};
    String[] arr2 = {"Hello", "World"};
    assertTrue(Equality.areEqual(arr1, arr2));
  }

  @Test
  public void testAreEqual_DifferentArraysOfIntegers_ReturnsFalse() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 4};
    assertFalse(Equality.areEqual(arr1, arr2));
  }

  @Test
  public void testAreEqual_ArraysWithDifferentLengths_ReturnsFalse() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3, 4};
    assertFalse(Equality.areEqual(arr1, arr2));
  }

  @Test
  public void testAreEqual_DifferentObjects_ReturnsFalse() {
    assertFalse(Equality.areEqual("Hello", "World"));
  }
}