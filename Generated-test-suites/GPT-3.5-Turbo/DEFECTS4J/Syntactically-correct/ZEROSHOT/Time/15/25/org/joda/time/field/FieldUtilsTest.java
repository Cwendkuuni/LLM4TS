import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldUtilsTest {
  
  @Test(expected = ArithmeticException.class)
  public void testSafeNegateException() {
    int result = FieldUtils.safeNegate(Integer.MIN_VALUE);
  }
  
  @Test
  public void testSafeNegate() {
    int result = FieldUtils.safeNegate(10);
    assertEquals(-10, result);
  }
  
  @Test
  public void testSafeAdd() {
    int result = FieldUtils.safeAdd(10, 20);
    assertEquals(30, result);
  }
  
  @Test(expected = ArithmeticException.class)
  public void testSafeAddOverflow() {
    int result = FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
  }
  
  @Test
  public void testSafeAddPositiveNegative() {
    int result = FieldUtils.safeAdd(10, -20);
    assertEquals(-10, result);
  }
  
  @Test
  public void testSafeAddNegativePositive() {
    int result = FieldUtils.safeAdd(-10, 20);
    assertEquals(10, result);
  }
  
  @Test
  public void testSafeAddNegativeNegative() {
    int result = FieldUtils.safeAdd(-10, -20);
    assertEquals(-30, result);
  }
  
  @Test
  public void testSafeAddZero() {
    int result = FieldUtils.safeAdd(0, 0);
    assertEquals(0, result);
  }
  
  @Test
  public void testSafeMultiplyInt() {
    int result = FieldUtils.safeMultiply(10, 3);
    assertEquals(30, result);
  }
  
  @Test
  public void testSafeMultiplyIntZero() {
    int result = FieldUtils.safeMultiply(10, 0);
    assertEquals(0, result);
  }
  
  @Test(expected = ArithmeticException.class)
  public void testSafeMultiplyIntOverflow() {
    int result = FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
  }
  
  @Test
  public void testSafeMultiplyLong() {
    long result = FieldUtils.safeMultiply(10L, 3L);
    assertEquals(30L, result);
  }
  
  @Test
  public void testSafeMultiplyLongNegative() {
    long result = FieldUtils.safeMultiply(-10L, 3L);
    assertEquals(-30L, result);
  }
  
  @Test
  public void testSafeMultiplyLongZero() {
    long result = FieldUtils.safeMultiply(10L, 0L);
    assertEquals(0L, result);
  }
  
  @Test(expected = ArithmeticException.class)
  public void testSafeMultiplyLongOverflow() {
    long result = FieldUtils.safeMultiply(Long.MAX_VALUE, 2L);
  }
  
  @Test
  public void testSafeSubtract() {
    long result = FieldUtils.safeSubtract(20L, 10L);
    assertEquals(10L, result);
  }
  
  @Test(expected = ArithmeticException.class)
  public void testSafeSubtractOverflow() {
    long result = FieldUtils.safeSubtract(Long.MIN_VALUE, 1L);
  }
  
  @Test
  public void testSafeMultiplyToInt() {
    int result = FieldUtils.safeMultiplyToInt(10L, 3L);
    assertEquals(30, result);
  }
  
  @Test(expected = ArithmeticException.class)
  public void testSafeMultiplyToIntOverflow() {
    int result = FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 2L);
  }
  
  @Test
  public void testSafeMultiplyToLong() {
    long result = FieldUtils.safeMultiply(10L, 3);
    assertEquals(30L, result);
  }
  
  @Test
  public void testSafeMultiplyToLongNegative() {
    long result = FieldUtils.safeMultiply(-10L, 3);
    assertEquals(-30L, result);
  }
  
  @Test
  public void testSafeMultiplyToLongZero() {
    long result = FieldUtils.safeMultiply(10L, 0);
    assertEquals(0L, result);
  }
  
  @Test(expected = ArithmeticException.class)
  public void testSafeMultiplyToLongOverflow() {
    long result = FieldUtils.safeMultiply(Long.MAX_VALUE, 2);
  }
  
  @Test
  public void testSafeToInt() {
    int result = FieldUtils.safeToInt(10L);
    assertEquals(10, result);
  }
  
  @Test(expected = ArithmeticException.class)
  public void testSafeToIntOverflow() {
    int result = FieldUtils.safeToInt(Long.MAX_VALUE);
  }
  
  @Test
  public void testVerifyValueBounds() {
    FieldUtils.verifyValueBounds(null, 10, 5, 15);
  }
  
  @Test(expected = IllegalFieldValueException.class)
  public void testVerifyValueBoundsException() {
    FieldUtils.verifyValueBounds(null, 20, 5, 15);
  }
  
  @Test
  public void testGetWrappedValue() {
    int result = FieldUtils.getWrappedValue(5, 2, 1, 10);
    assertEquals(7, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetWrappedValueInvalidRange() {
    int result = FieldUtils.getWrappedValue(5, 2, 10, 1);
  }
  
  @Test
  public void testEquals() {
    boolean result = FieldUtils.equals("test", "test");
    assertEquals(true, result);
  }
  
  @Test
  public void testEqualsNull() {
    boolean result = FieldUtils.equals(null, "test");
    assertEquals(false, result);
  }
  
  @Test
  public void testEqualsFalse() {
    boolean result = FieldUtils.equals("test", "sample");
    assertEquals(false, result);
  }

}