import org.junit.*;
import static org.junit.Assert.*;

public class MathUtilsTest {
  
  @Test
  public void testHash_WithPositiveValue() {
    double value = 1.5;
    int expected = Double.valueOf(value).hashCode();
    int actual = MathUtils.hash(value);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testHash_WithNegativeValue() {
    double value = -2.0;
    int expected = Double.valueOf(value).hashCode();
    int actual = MathUtils.hash(value);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testHash_WithZeroValue() {
    double value = 0.0;
    int expected = Double.valueOf(value).hashCode();
    int actual = MathUtils.hash(value);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testHashArray_WithValidArray() {
    double[] value = {1.5, 2.0, 3.7};
    int expected = Arrays.hashCode(value);
    int actual = MathUtils.hash(value);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testNormalizeAngle_WithPositiveAngleAndCenter() {
    double a = 3.0;
    double center = 1.0;
    double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
    double actual = MathUtils.normalizeAngle(a, center);
    assertEquals(expected, actual, 0.0001);
  }
  
  @Test
  public void testNormalizeAngle_WithNegativeAngleAndCenter() {
    double a = -2.0;
    double center = -1.0;
    double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
    double actual = MathUtils.normalizeAngle(a, center);
    assertEquals(expected, actual, 0.0001);
  }
  
  @Test
  public void testReduce_WithPositiveInput() {
    double a = 10.0;
    double period = 4.0;
    double offset = 1.5;
    double expected = a - period * Math.floor((a - offset) / period) - offset;
    double actual = MathUtils.reduce(a, period, offset);
    assertEquals(expected, actual, 0.0001);
  }
  
  @Test
  public void testReduce_WithNegativeInput() {
    double a = -5.0;
    double period = 3.0;
    double offset = -1.0;
    double expected = a - period * Math.floor((a - offset) / period) - offset;
    double actual = MathUtils.reduce(a, period, offset);
    assertEquals(expected, actual, 0.0001);
  }
  
  @Test
  public void testCopySign_WithPositiveInputs() throws MathArithmeticException {
    byte magnitude = 10;
    byte sign = 1;
    byte expected = magnitude;
    byte actual = MathUtils.copySign(magnitude, sign);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testCopySign_WithNegativeInputs() throws MathArithmeticException {
    byte magnitude = -10;
    byte sign = -1;
    byte expected = magnitude;
    byte actual = MathUtils.copySign(magnitude, sign);
    assertEquals(expected, actual);
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySign_WithPositiveMagnitudeAndOverflow() throws MathArithmeticException {
    byte magnitude = -128;
    byte sign = 1;
    MathUtils.copySign(magnitude, sign);
  }
  
  @Test
  public void testCopySign_WithPositiveMagnitudeAndMaxValueSign() throws MathArithmeticException {
    byte magnitude = -128;
    byte sign = -1;
    byte expected = 127;
    byte actual = MathUtils.copySign(magnitude, sign);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testCopySign_WithZeroMagnitudeAndPositiveSign() throws MathArithmeticException {
    byte magnitude = 0;
    byte sign = 1;
    byte expected = magnitude;
    byte actual = MathUtils.copySign(magnitude, sign);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testCopySign_WithShortMagnitude() throws MathArithmeticException {
    short magnitude = -20000;
    short sign = 8000;
    short expected = magnitude;
    short actual = MathUtils.copySign(magnitude, sign);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testCopySign_WithIntMagnitude() throws MathArithmeticException {
    int magnitude = -20000000;
    int sign = 8000000;
    int expected = magnitude;
    int actual = MathUtils.copySign(magnitude, sign);
    assertEquals(expected, actual);
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySign_WithPositiveMagnitudeAndMinValueSign() throws MathArithmeticException {
    int magnitude = -2147483648;
    int sign = 1;
    MathUtils.copySign(magnitude, sign);
  }
  
  @Test
  public void testCopySign_WithPositiveMagnitudeAndMaxValueSign() throws MathArithmeticException {
    int magnitude = -2147483648;
    int sign = -1;
    int expected = 2147483647;
    int actual = MathUtils.copySign(magnitude, sign);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testCopySign_WithLongMagnitude() throws MathArithmeticException {
    long magnitude = -9000000000000L;
    long sign = 100000000000L;
    long expected = magnitude;
    long actual = MathUtils.copySign(magnitude, sign);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testCopySign_WithPositiveMagnitudeAndMaxValueSign() throws MathArithmeticException {
    long magnitude = -9000000000000L;
    long sign = -100000000000L;
    long expected = 9000000000000L;
    long actual = MathUtils.copySign(magnitude, sign);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testCheckFinite_WithFiniteValue() throws NotFiniteNumberException {
    double value = 1.5;
    MathUtils.checkFinite(value);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFinite_WithInfiniteValue() throws NotFiniteNumberException {
    double value = Double.POSITIVE_INFINITY;
    MathUtils.checkFinite(value);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFinite_WithNaNValue() throws NotFiniteNumberException {
    double value = Double.NaN;
    MathUtils.checkFinite(value);
  }
  
  @Test
  public void testCheckFiniteArray_WithFiniteArray() throws NotFiniteNumberException {
    double[] values = {1.5, 2.0, 3.7};
    MathUtils.checkFinite(values);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteArray_WithArrayContainingInfinity() throws NotFiniteNumberException {
    double[] values = {1.5, Double.POSITIVE_INFINITY, 3.7};
    MathUtils.checkFinite(values);
  }
  
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteArray_WithArrayContainingNaN() throws NotFiniteNumberException {
    double[] values = {1.5, Double.NaN, 3.7};
    MathUtils.checkFinite(values);
  }
  
  @Test
  public void testCheckNotNull_WithNonNullObject() throws NullArgumentException {
    Object object = new Object();
    MathUtils.checkNotNull(object);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testCheckNotNull_WithNullObject_1() throws NullArgumentException {
    Object object = null;
    MathUtils.checkNotNull(object);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testCheckNotNull_WithNullObject_2() throws NullArgumentException {
    Object object = null;
    Localizable pattern = null;
    MathUtils.checkNotNull(object, pattern);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testCheckNotNull_WithNullObject_3() throws NullArgumentException {
    Object object = null;
    Localizable pattern = null;
    String argument = "argument";
    MathUtils.checkNotNull(object, pattern, argument);
  }
}