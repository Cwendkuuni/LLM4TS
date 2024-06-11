import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.*;
import java.util.*;

public class MathUtilsTest {
  
  @Test
  public void testHashWithPositiveValue() {
    double value = 1.23;
    int expected = Double.valueOf(value).hashCode();
    int actual = MathUtils.hash(value);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testHashWithNegativeValue() {
    double value = -1.23;
    int expected = Double.valueOf(value).hashCode();
    int actual = MathUtils.hash(value);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testHashWithEmptyArray() {
    double[] value = {};
    int expected = Arrays.hashCode(value);
    int actual = MathUtils.hash(value);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testHashWithNonEmptyArray() {
    double[] value = {1.23, 2.34, -3.45};
    int expected = Arrays.hashCode(value);
    int actual = MathUtils.hash(value);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testNormalizeAngleWithPositiveAngle() {
    double a = 1.23;
    double center = 0.0;
    double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
    double actual = MathUtils.normalizeAngle(a, center);
    Assert.assertEquals(expected, actual, 0.0);
  }
  
  @Test
  public void testNormalizeAngleWithNegativeAngle() {
    double a = -1.23;
    double center = 0.0;
    double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
    double actual = MathUtils.normalizeAngle(a, center);
    Assert.assertEquals(expected, actual, 0.0);
  }
  
  @Test
  public void testReduceWithPositiveValues() {
    double a = 1.23;
    double period = 3.45;
    double offset = 0.0;
    double p = FastMath.abs(period);
    double expected = a - p * FastMath.floor((a - offset) / p) - offset;
    double actual = MathUtils.reduce(a, period, offset);
    Assert.assertEquals(expected, actual, 0.0);
  }
  
  @Test
  public void testReduceWithNegativeValues() {
    double a = -1.23;
    double period = -3.45;
    double offset = 0.0;
    double p = FastMath.abs(period);
    double expected = a - p * FastMath.floor((a - offset) / p) - offset;
    double actual = MathUtils.reduce(a, period, offset);
    Assert.assertEquals(expected, actual, 0.0);
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignByteOverflow() {
    byte magnitude = -128;
    byte sign = 1;
    MathUtils.copySign(magnitude, sign);
  }
  
  @Test
  public void testCopySignByteWithPositiveValues() {
    byte magnitude = 123;
    byte sign = 1;
    byte expected = magnitude;
    byte actual = MathUtils.copySign(magnitude, sign);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testCopySignByteWithNegativeValues() {
    byte magnitude = -123;
    byte sign = -1;
    byte expected = magnitude;
    byte actual = MathUtils.copySign(magnitude, sign);
    Assert.assertEquals(expected, actual);
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignShortOverflow() {
    short magnitude = -32768;
    short sign = 1;
    MathUtils.copySign(magnitude, sign);
  }
  
  @Test
  public void testCopySignShortWithPositiveValues() {
    short magnitude = 1234;
    short sign = 1;
    short expected = magnitude;
    short actual = MathUtils.copySign(magnitude, sign);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testCopySignShortWithNegativeValues() {
    short magnitude = -1234;
    short sign = -1;
    short expected = magnitude;
    short actual = MathUtils.copySign(magnitude, sign);
    Assert.assertEquals(expected, actual);
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignIntOverflow() {
    int magnitude = Integer.MIN_VALUE;
    int sign = 1;
    MathUtils.copySign(magnitude, sign);
  }
  
  @Test
  public void testCopySignIntWithPositiveValues() {
    int magnitude = 123456;
    int sign = 1;
    int expected = magnitude;
    int actual = MathUtils.copySign(magnitude, sign);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testCopySignIntWithNegativeValues() {
    int magnitude = -123456;
    int sign = -1;
    int expected = magnitude;
    int actual = MathUtils.copySign(magnitude, sign);
    Assert.assertEquals(expected, actual);
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignLongOverflow() {
    long magnitude = Long.MIN_VALUE;
    long sign = 1;
    MathUtils.copySign(magnitude, sign);
  }
  
  @Test
  public void testCopySignLongWithPositiveValues() {
    long magnitude = 123456789;
    long sign = 1;
    long expected = magnitude;
    long actual = MathUtils.copySign(magnitude, sign);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testCopySignLongWithNegativeValues() {
    long magnitude = -123456789;
    long sign = -1;
    long expected = magnitude;
    long actual = MathUtils.copySign(magnitude, sign);
    Assert.assertEquals(expected, actual);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteWithPositiveInfinity() {
    double x = Double.POSITIVE_INFINITY;
    MathUtils.checkFinite(x);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteWithNegativeInfinity() {
    double x = Double.NEGATIVE_INFINITY;
    MathUtils.checkFinite(x);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteWithNaN() {
    double x = Double.NaN;
    MathUtils.checkFinite(x);
  }
  
  @Test
  public void testCheckFiniteWithFiniteNumber() {
    double x = 1.23;
    MathUtils.checkFinite(x);
    // No exception is thrown
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteArrayWithPositiveInfinity() {
    double[] val = {1.23, Double.POSITIVE_INFINITY};
    MathUtils.checkFinite(val);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteArrayWithNegativeInfinity() {
    double[] val = {1.23, Double.NEGATIVE_INFINITY};
    MathUtils.checkFinite(val);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteArrayWithNaN() {
    double[] val = {1.23, Double.NaN};
    MathUtils.checkFinite(val);
  }
  
  @Test
  public void testCheckFiniteArrayWithFiniteNumbers() {
    double[] val = {1.23, 2.34};
    MathUtils.checkFinite(val);
    // No exception is thrown
  }
  
  @Test(expected = NullArgumentException.class)
  public void testCheckNotNullWithNullObjectAndPattern() {
    Object o = null;
    LocalizedFormats pattern = LocalizedFormats.NULL_NOT_ALLOWED;
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
    LocalizedFormats pattern = LocalizedFormats.NULL_NOT_ALLOWED;
    MathUtils.checkNotNull(o, pattern);
    // No exception is thrown
  }
  
  @Test
  public void testCheckNotNullWithNonNullObject() {
    Object o = new Object();
    MathUtils.checkNotNull(o);
    // No exception is thrown
  }

}