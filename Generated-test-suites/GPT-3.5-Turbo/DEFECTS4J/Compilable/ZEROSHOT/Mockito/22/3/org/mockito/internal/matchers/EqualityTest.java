package org.mockito.internal.matchers;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

  @Test
  public void testAreEqual_bothNull_returnsTrue() {
    boolean result = Equality.areEqual(null, null);
    assertTrue(result);
  }

  @Test
  public void testAreEqual_oneNull_returnsFalse() {
    boolean result = Equality.areEqual(null, new Object());
    assertFalse(result);

    result = Equality.areEqual(new Object(), null);
    assertFalse(result);
  }

  @Test
  public void testAreEqual_bothArray_returnsTrue() {
    boolean result = Equality.areEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3});
    assertTrue(result);
  }

  @Test
  public void testAreEqual_oneArray_returnsFalse() {
    boolean result = Equality.areEqual(new Object[]{1, 2, 3}, new Object());
    assertFalse(result);

    result = Equality.areEqual(new Object(), new Object[]{1, 2, 3});
    assertFalse(result);
  }

  @Test
  public void testAreEqual_equalObjects_returnsTrue() {
    boolean result = Equality.areEqual("hello", "hello");
    assertTrue(result);
  }

  @Test
  public void testAreEqual_differentObjects_returnsFalse() {
    boolean result = Equality.areEqual("hello", "world");
    assertFalse(result);
  }

  @Test
  public void testAreArraysEqual_equalArrays_returnsTrue() {
    boolean result = Equality.areArraysEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3});
    assertTrue(result);
  }

  @Test
  public void testAreArraysEqual_differentLengths_returnsFalse() {
    boolean result = Equality.areArraysEqual(new Object[]{1, 2, 3}, new Object[]{1, 2});
    assertFalse(result);
  }

  @Test
  public void testAreArrayLengthsEqual_equalLengths_returnsTrue() {
    boolean result = Equality.areArrayLengthsEqual(new Object[]{}, new Object[]{});
    assertTrue(result);
  }

  @Test
  public void testAreArrayLengthsEqual_differentLengths_returnsFalse() {
    boolean result = Equality.areArrayLengthsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2});
    assertFalse(result);
  }

  @Test
  public void testAreArrayElementsEqual_equalElements_returnsTrue() {
    boolean result = Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3});
    assertTrue(result);
  }

  @Test
  public void testAreArrayElementsEqual_differentElements_returnsFalse() {
    boolean result = Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2});
    assertFalse(result);
  }

  @Test
  public void testIsArray_isArray_returnsTrue() {
    boolean result = Equality.isArray(new Object[]{1, 2, 3});
    assertTrue(result);
  }

  @Test
  public void testIsArray_isNotArray_returnsFalse() {
    boolean result = Equality.isArray(new Object());
    assertFalse(result);
  }
}