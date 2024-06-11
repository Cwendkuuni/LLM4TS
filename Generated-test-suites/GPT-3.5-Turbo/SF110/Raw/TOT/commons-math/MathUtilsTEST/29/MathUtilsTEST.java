package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.*;

public class MathUtilsTest {

  @Test
  public void testHashForDouble() {
    double value = 3.14;
    int expectedHash = new Double(value).hashCode();
    
    int actualHash = MathUtils.hash(value);
    
    assertEquals(expectedHash, actualHash);
  }
  
  @Test
  public void testHashForDoubleArray() {
    double[] value = {1.1, 2.2, 3.3};
    int expectedHash = value.hashCode();
    
    int actualHash = MathUtils.hash(value);
    
    assertEquals(expectedHash, actualHash);
  }
  
  @Test
  public void testNormalizeAngle() {
    double angle = 7.5;
    double center = 3.0;
    double expectedNormalizedAngle = angle - MathUtils.TWO_PI * Math.floor((angle + Math.PI - center) / MathUtils.TWO_PI);
    
    double actualNormalizedAngle = MathUtils.normalizeAngle(angle, center);
    
    assertEquals(expectedNormalizedAngle, actualNormalizedAngle, 0.000001);
  }
  
  @Test
  public void testReduce() {
    double a = 10.5;
    double period = 3.0;
    double offset = 2.0;
    double expectedReducedValue = a - (period * Math.floor((a - offset) / period)) - offset;
    
    double actualReducedValue = MathUtils.reduce(a, period, offset);
    
    assertEquals(expectedReducedValue, actualReducedValue, 0.000001);
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignByteOverflow() {
    byte magnitude = -128;
    byte sign = 1;
    
    MathUtils.copySign(magnitude, sign);
  }
  
  @Test
  public void testCopySignBytePositiveResult() {
    byte magnitude = 10;
    byte sign = 1;
    byte expectedResult = magnitude;
    
    byte actualResult = MathUtils.copySign(magnitude, sign);
    
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testCopySignByteNegativeResult() {
    byte magnitude = 10;
    byte sign = -1;
    byte expectedResult = (byte)(-magnitude);
    
    byte actualResult = MathUtils.copySign(magnitude, sign);
    
    assertEquals(expectedResult, actualResult);
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignShortOverflow() {
    short magnitude = -32768;
    short sign = 1;
    
    MathUtils.copySign(magnitude, sign);
  }
  
  @Test
  public void testCopySignShortPositiveResult() {
    short magnitude = 500;
    short sign = 1;
    short expectedResult = magnitude;
    
    short actualResult = MathUtils.copySign(magnitude, sign);
    
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testCopySignShortNegativeResult() {
    short magnitude = 500;
    short sign = -1;
    short expectedResult = (short)(-magnitude);
    
    short actualResult = MathUtils.copySign(magnitude, sign);
    
    assertEquals(expectedResult, actualResult);
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignIntOverflow() {
    int magnitude = Integer.MIN_VALUE;
    int sign = 1;
    
    MathUtils.copySign(magnitude, sign);
  }
  
  @Test
  public void testCopySignIntPositiveResult() {
    int magnitude = 7000;
    int sign = 1;
    int expectedResult = magnitude;
    
    int actualResult = MathUtils.copySign(magnitude, sign);
    
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testCopySignIntNegativeResult() {
    int magnitude = 7000;
    int sign = -1;
    int expectedResult = -magnitude;
    
    int actualResult = MathUtils.copySign(magnitude, sign);
    
    assertEquals(expectedResult, actualResult);
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignLongOverflow() {
    long magnitude = Long.MIN_VALUE;
    long sign = 1;
    
    MathUtils.copySign(magnitude, sign);
  }
  
  @Test
  public void testCopySignLongPositiveResult() {
    long magnitude = 1234567890L;
    long sign = 1;
    long expectedResult = magnitude;
    
    long actualResult = MathUtils.copySign(magnitude, sign);
    
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testCopySignLongNegativeResult() {
    long magnitude = 1234567890L;
    long sign = -1;
    long expectedResult = -magnitude;
    
    long actualResult = MathUtils.copySign(magnitude, sign);
    
    assertEquals(expectedResult, actualResult);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteForInfiniteValue() {
    double x = Double.POSITIVE_INFINITY;
    
    MathUtils.checkFinite(x);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteForNaNValue() {
    double x = Double.NaN;
    
    MathUtils.checkFinite(x);
  }
  
  @Test
  public void testCheckFiniteForFiniteValue() {
    double x = 3.14;
    
    MathUtils.checkFinite(x);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteForArrayWithInfiniteValue() {
    double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
    
    MathUtils.checkFinite(val);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteForArrayWithNaNValue() {
    double[] val = {1.0, Double.NaN, 3.0};
    
    MathUtils.checkFinite(val);
  }
  
  @Test
  public void testCheckFiniteForArrayWithAllFiniteValues() {
    double[] val = {1.0, 2.0, 3.0};
    
    MathUtils.checkFinite(val);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testCheckNotNullWithNullObjectAndPattern() {
    Object o = null;
    Localizable pattern = LocalizedFormats.NULL_NOT_ALLOWED;
    
    MathUtils.checkNotNull(o, pattern);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testCheckNotNullWithNullObject() {
    Object o = null;
    
    MathUtils.checkNotNull(o);
  }
  
  @Test
  public void testCheckNotNullWithNonNullObjectAndPattern() {
    Object o = new Object();
    Localizable pattern = LocalizedFormats.NULL_NOT_ALLOWED;
    
    MathUtils.checkNotNull(o, pattern);
  }
  
  @Test
  public void testCheckNotNullWithNonNullObject() {
    Object o = new Object();
    
    MathUtils.checkNotNull(o);
  }
}