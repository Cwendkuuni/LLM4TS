package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {
  
  @Test
  public void testAreEqual_NullValues_ReturnsTrue() {
    boolean result = Equality.areEqual(null, null);
    assertTrue(result);
  }
  
  @Test
  public void testAreEqual_OneNullValue_ReturnsFalse() {
    boolean result = Equality.areEqual(null, new Object());
    assertFalse(result);
  }
  
  @Test
  public void testAreEqual_BothObjectsNull_ReturnsTrue() {
    boolean result = Equality.areEqual(new Object(), null);
    assertFalse(result);
  }
  
  @Test
  public void testAreEqual_TwoEqualObjects_ReturnsTrue() {
    Object obj = new Object();
    boolean result = Equality.areEqual(obj, obj);
    assertTrue(result);
  }
  
  @Test
  public void testAreEqual_TwoDifferentObjects_ReturnsFalse() {
    boolean result = Equality.areEqual(new Object(), new Object());
    assertFalse(result);
  }
  
  @Test
  public void testAreEqual_TwoEqualArrays_ReturnsTrue() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3};
    boolean result = Equality.areEqual(arr1, arr2);
    assertTrue(result);
  }
  
  @Test
  public void testAreEqual_TwoDifferentArrays_ReturnsFalse() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {4, 5, 6};
    boolean result = Equality.areEqual(arr1, arr2);
    assertFalse(result);
  }
  
  @Test
  public void testAreArrayLengthsEqual_EqualArrays_ReturnsTrue() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {4, 5, 6};
    boolean result = Equality.areArrayLengthsEqual(arr1, arr2);
    assertTrue(result);
  }
  
  @Test
  public void testAreArrayLengthsEqual_DifferentLengthArrays_ReturnsFalse() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2};
    boolean result = Equality.areArrayLengthsEqual(arr1, arr2);
    assertFalse(result);
  }
  
  @Test
  public void testAreArrayElementsEqual_EqualArrays_ReturnsTrue() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3};
    boolean result = Equality.areArrayElementsEqual(arr1, arr2);
    assertTrue(result);
  }
  
  @Test
  public void testAreArrayElementsEqual_DifferentElementArrays_ReturnsFalse() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {4, 5, 6};
    boolean result = Equality.areArrayElementsEqual(arr1, arr2);
    assertFalse(result);
  }
  
  @Test
  public void testAreArrayElementsEqual_DifferentLengthArrays_ReturnsFalse() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2};
    boolean result = Equality.areArrayElementsEqual(arr1, arr2);
    assertFalse(result);
  }
  
  @Test
  public void testIsArray_ArrayObject_ReturnsTrue() {
    int[] arr = {1, 2, 3};
    boolean result = Equality.isArray(arr);
    assertTrue(result);
  }
  
  @Test
  public void testIsArray_NonArrayObject_ReturnsFalse() {
    Object obj = new Object();
    boolean result = Equality.isArray(obj);
    assertFalse(result);
  }
}