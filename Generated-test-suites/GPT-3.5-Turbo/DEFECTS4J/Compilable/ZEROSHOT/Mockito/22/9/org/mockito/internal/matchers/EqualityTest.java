package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {
  
  @Test
  public void testAreEqual_NullBoth_ReturnsTrue() {
    assertTrue(Equality.areEqual(null, null));
  }
  
  @Test
  public void testAreEqual_NullOne_ReturnsFalse() {
    assertFalse(Equality.areEqual(null, new Object()));
  }
  
  @Test
  public void testAreEqual_DifferentTypes_ReturnsFalse() {
    assertFalse(Equality.areEqual(5, "5"));
  }
  
  @Test
  public void testAreEqual_ArraysNotEqual_ReturnsFalse() {
    int[] array1 = {1, 2, 3};
    int[] array2 = {4, 5, 6};
    assertFalse(Equality.areEqual(array1, array2));
  }
  
  @Test
  public void testAreEqual_ArraysEqual_ReturnsTrue() {
    int[] array1 = {1, 2, 3};
    int[] array2 = {1, 2, 3};
    assertTrue(Equality.areEqual(array1, array2));
  }
  
  @Test
  public void testAreEqual_ObjectsNotEqual_ReturnsFalse() {
    Object obj1 = new Object();
    Object obj2 = new Object();
    assertFalse(Equality.areEqual(obj1, obj2));
  }
  
  @Test
  public void testAreEqual_ObjectsEqual_ReturnsTrue() {
    Object obj1 = new Object();
    Object obj2 = obj1;
    assertTrue(Equality.areEqual(obj1, obj2));
  }
  
  @Test
  public void testAreEqual_SameObject_ReturnsTrue() {
    Object obj1 = new Object();
    assertTrue(Equality.areEqual(obj1, obj1));
  }
  
  @Test
  public void testAreArraysEqual_DifferentLength_ReturnsFalse() {
    int[] array1 = {1, 2, 3};
    int[] array2 = {1, 2, 3, 4};
    assertFalse(Equality.areArrayLengthsEqual(array1, array2));
  }
  
  @Test
  public void testAreArraysEqual_SameLength_ReturnsTrue() {
    int[] array1 = {1, 2, 3};
    int[] array2 = {4, 5, 6};
    assertTrue(Equality.areArrayLengthsEqual(array1, array2));
  }
  
  @Test
  public void testAreArrayElementsEqual_NotEqualElement_ReturnsFalse() {
    int[] array1 = {1, 2, 3};
    int[] array2 = {4, 5, 6};
    assertFalse(Equality.areArrayElementsEqual(array1, array2));
  }
  
  @Test
  public void testAreArrayElementsEqual_EqualElements_ReturnsTrue() {
    int[] array1 = {1, 2, 3};
    int[] array2 = {1, 2, 3};
    assertTrue(Equality.areArrayElementsEqual(array1, array2));
  }
  
  @Test
  public void testIsArray_IsArray_ReturnsTrue() {
    int[] array = {1, 2, 3};
    assertTrue(Equality.isArray(array));
  }
  
  @Test
  public void testIsArray_NotArray_ReturnsFalse() {
    Object obj = new Object();
    assertFalse(Equality.isArray(obj));
  }
}