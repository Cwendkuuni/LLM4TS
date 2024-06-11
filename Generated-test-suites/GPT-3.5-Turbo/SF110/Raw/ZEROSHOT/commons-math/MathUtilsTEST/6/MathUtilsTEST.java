import org.junit.Test;
import org.apache.commons.math3.util.*;

public class MathUtilsTest {
  
  @Test
  public void testHash_double() {
    double value = 1.5;
    int expected = Double.valueOf(value).hashCode();
    int actual = MathUtils.hash(value);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testHash_doubleArray() {
    double[] value = {1.5, 2.5, 3.5};
    int expected = Arrays.hashCode(value);
    int actual = MathUtils.hash(value);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testNormalizeAngle() {
    double a = 1.5;
    double center = 0.5;
    double expected = a - MathUtils.TWO_PI * FastMath.floor((a + 3.141592653589793 - center) / MathUtils.TWO_PI);
    double actual = MathUtils.normalizeAngle(a, center);
    assertEquals(expected, actual, 0.000001);
  }
  
  @Test
  public void testReduce() {
    double a = 5.5;
    double period = 2.0;
    double offset = 1.0;
    double p = FastMath.abs(period);
    double expected = a - p * FastMath.floor((a - offset) / p) - offset;
    double actual = MathUtils.reduce(a, period, offset);
    assertEquals(expected, actual, 0.000001);
  }
  
  @Test
  public void testCopySign_byte() {
    byte magnitude = 5;
    byte sign = -1;
    byte expected = (byte)-(magnitude);
    try {
      byte actual = MathUtils.copySign(magnitude, sign);
      assertEquals(expected, actual);
    } catch (MathArithmeticException e) {
      fail("Expected no exception but got " + e.toString());
    }
  }
  
  @Test
  public void testCopySign_byte_exception() {
    byte magnitude = -128;
    byte sign = 1;
    try {
      byte actual = MathUtils.copySign(magnitude, sign);
      fail("Expected MathArithmeticException but got no exception");
    } catch (MathArithmeticException e) {
      assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
      assertTrue(e.getArguments().length == 0);
    }
  }
  
  @Test
  public void testCopySign_short() {
    short magnitude = 5;
    short sign = -1;
    short expected = (short)-(magnitude);
    try {
      short actual = MathUtils.copySign(magnitude, sign);
      assertEquals(expected, actual);
    } catch (MathArithmeticException e) {
      fail("Expected no exception but got " + e.toString());
    }
  }
  
  @Test
  public void testCopySign_short_exception() {
    short magnitude = -32768;
    short sign = 1;
    try {
      short actual = MathUtils.copySign(magnitude, sign);
      fail("Expected MathArithmeticException but got no exception");
    } catch (MathArithmeticException e) {
      assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
      assertTrue(e.getArguments().length == 0);
    }
  }
  
  @Test
  public void testCopySign_int() {
    int magnitude = 5;
    int sign = -1;
    int expected = -(magnitude);
    try {
      int actual = MathUtils.copySign(magnitude, sign);
      assertEquals(expected, actual);
    } catch (MathArithmeticException e) {
      fail("Expected no exception but got " + e.toString());
    }
  }
  
  @Test
  public void testCopySign_int_exception() {
    int magnitude = Integer.MIN_VALUE;
    int sign = 1;
    try {
      int actual = MathUtils.copySign(magnitude, sign);
      fail("Expected MathArithmeticException but got no exception");
    } catch (MathArithmeticException e) {
      assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
      assertTrue(e.getArguments().length == 0);
    }
  }
  
  @Test
  public void testCopySign_long() {
    long magnitude = 5L;
    long sign = -1L;
    long expected = -(magnitude);
    try {
      long actual = MathUtils.copySign(magnitude, sign);
      assertEquals(expected, actual);
    } catch (MathArithmeticException e) {
      fail("Expected no exception but got " + e.toString());
    }
  }
  
  @Test
  public void testCopySign_long_exception() {
    long magnitude = Long.MIN_VALUE;
    long sign = 1L;
    try {
      long actual = MathUtils.copySign(magnitude, sign);
      fail("Expected MathArithmeticException but got no exception");
    } catch (MathArithmeticException e) {
      assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
      assertTrue(e.getArguments().length == 0);
    }
  }
  
  @Test
  public void testCheckFinite_double() {
    double x = 1.5;
    try {
      MathUtils.checkFinite(x);
    } catch (NotFiniteNumberException e) {
      fail("Expected no exception but got " + e.toString());
    }
  }
  
  @Test
  public void testCheckFinite_double_infinite() {
    double x = Double.POSITIVE_INFINITY;
    try {
      MathUtils.checkFinite(x);
      fail("Expected NotFiniteNumberException but got no exception");
    } catch (NotFiniteNumberException e) {
      assertEquals(x, e.getArgument());
      assertTrue(e.getArguments().length == 0);
    }
  }
  
  @Test
  public void testCheckFinite_double_nan() {
    double x = Double.NaN;
    try {
      MathUtils.checkFinite(x);
      fail("Expected NotFiniteNumberException but got no exception");
    } catch (NotFiniteNumberException e) {
      assertEquals(x, e.getArgument());
      assertTrue(e.getArguments().length == 0);
    }
  }
  
  @Test
  public void testCheckFinite_doubleArray() {
    double[] val = {1.5, 2.5, 3.5};
    try {
      MathUtils.checkFinite(val);
    } catch (NotFiniteNumberException e) {
      fail("Expected no exception but got " + e.toString());
    }
  }
  
  @Test
  public void testCheckFinite_doubleArray_infinite() {
    double[] val = {1.5, 2.5, Double.POSITIVE_INFINITY};
    try {
      MathUtils.checkFinite(val);
      fail("Expected NotFiniteNumberException but got no exception");
    } catch (NotFiniteNumberException e) {
      assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
      assertEquals(Double.POSITIVE_INFINITY, e.getArgument());
      assertTrue(e.getArguments().length == 1);
      assertEquals(2, e.getArguments()[0]);
    }
  }
  
  @Test
  public void testCheckFinite_doubleArray_nan() {
    double[] val = {1.5, 2.5, Double.NaN};
    try {
      MathUtils.checkFinite(val);
      fail("Expected NotFiniteNumberException but got no exception");
    } catch (NotFiniteNumberException e) {
      assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
      assertEquals(Double.NaN, e.getArgument());
      assertTrue(e.getArguments().length == 1);
      assertEquals(2, e.getArguments()[0]);
    }
  }
  
  @Test
  public void testCheckNotNull_withPattern() {
    Object o = new Object();
    Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
    Object[] args = {1};
    try {
      MathUtils.checkNotNull(o, pattern, args);
    } catch (NullArgumentException e) {
      fail("Expected no exception but got " + e.toString());
    }
  }
  
  @Test
  public void testCheckNotNull_withPattern_null() {
    Object o = null;
    Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
    Object[] args = {1};
    try {
      MathUtils.checkNotNull(o, pattern, args);
      fail("Expected NullArgumentException but got no exception");
    } catch (NullArgumentException e) {
      assertTrue(e.getPattern() == pattern);
      assertTrue(e.getArguments().length == 1);
      assertEquals(1, e.getArguments()[0]);
    }
  }
  
  @Test
  public void testCheckNotNull_noPattern() {
    Object o = new Object();
    try {
      MathUtils.checkNotNull(o);
    } catch (NullArgumentException e) {
      fail("Expected no exception but got " + e.toString());
    }
  }
  
  @Test
  public void testCheckNotNull_noPattern_null() {
    Object o = null;
    try {
      MathUtils.checkNotNull(o);
      fail("Expected NullArgumentException but got no exception");
    } catch (NullArgumentException e) {
      assertTrue(e.getPattern() == null);
      assertTrue(e.getArguments().length == 0);
    }
  }
}