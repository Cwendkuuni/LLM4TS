
import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {
  
  // Testing hash(double value) method
  @Test
  public void testHash() {
    double value = 10.5;
    int expected = Double.valueOf(value).hashCode();
    int actual = MathUtils.hash(value);
    assertEquals(expected, actual);
  }
  
  // Testing hash(double[] value) method
  @Test
  public void testHashArray() {
    double[] value = {1.5, 2.5, 3.5};
    int expected = Arrays.hashCode(value);
    int actual = MathUtils.hash(value);
    assertEquals(expected, actual);
  }
  
  // Testing normalizeAngle(double a, double center) method
  @Test
  public void testNormalizeAngle() {
    double a = 5.2;
    double center = 3.8;
    double expected = MathUtils.normalizeAngle(a, center);
    double actual = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
    assertEquals(expected, actual, 0.00001);
  }
  
  // Testing reduce(double a, double period, double offset) method
  @Test
  public void testReduce() {
    double a = 7.6;
    double period = 2.5;
    double offset = 1.4;
    double p = FastMath.abs(period);
    double expected = a - p * FastMath.floor((a - offset) / p) - offset;
    double actual = MathUtils.reduce(a, period, offset);
    assertEquals(expected, actual, 0.00001);
  }
  
  // Testing copySign(byte magnitude, byte sign) method
  @Test
  public void testCopySignByte() throws MathArithmeticException {
    byte magnitude = 5;
    byte sign = -3;
    byte expected = MathUtils.copySign(magnitude, sign);
    byte actual = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (-magnitude);
    assertEquals(expected, actual);
  }
  
  // Testing copySign(short magnitude, short sign) method
  @Test
  public void testCopySignShort() throws MathArithmeticException {
    short magnitude = 100;
    short sign = -50;
    short expected = MathUtils.copySign(magnitude, sign);
    short actual = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (-magnitude);
    assertEquals(expected, actual);
  }
  
  // Testing copySign(int magnitude, int sign) method
  @Test
  public void testCopySignInt() throws MathArithmeticException {
    int magnitude = 1000;
    int sign = -500;
    int expected = MathUtils.copySign(magnitude, sign);
    int actual = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : -magnitude;
    assertEquals(expected, actual);
  }
  
  // Testing copySign(long magnitude, long sign) method
  @Test
  public void testCopySignLong() throws MathArithmeticException {
    long magnitude = 1000000L;
    long sign = -500000L;
    long expected = MathUtils.copySign(magnitude, sign);
    long actual = (magnitude >= 0L && sign >= 0L) || (magnitude < 0L && sign < 0L) ? magnitude : -magnitude;
    assertEquals(expected, actual);
  }
  
  // Testing checkFinite(double x) method
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteDouble() throws NotFiniteNumberException {
    double x = Double.POSITIVE_INFINITY;
    MathUtils.checkFinite(x);
  }
  
  // Testing checkFinite(double[] val) method
  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteArray() throws NotFiniteNumberException {
    double[] val = {1.0, 2.0, Double.POSITIVE_INFINITY};
    MathUtils.checkFinite(val);
  }
  
  // Testing checkNotNull(Object o, Localizable pattern, Object... args) method
  @Test(expected = NullArgumentException.class)
  public void testCheckNotNullWithPattern() throws NullArgumentException {
    Object o = null;
    Localizable pattern = LocalizedFormats.VALUE_IS_NULL;
    MathUtils.checkNotNull(o, pattern);
  }
  
  // Testing checkNotNull(Object o) method
  @Test(expected = NullArgumentException.class)
  public void testCheckNotNull() throws NullArgumentException {
    Object o = null;
    MathUtils.checkNotNull(o);
  }
}

