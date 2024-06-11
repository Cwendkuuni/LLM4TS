
import org.junit.Test;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import static org.junit.Assert.*;

public class MathUtilsTest {

  @Test
  public void testHash() {
    double value = 10.5;
    int expected = new Double(value).hashCode();
    int result = MathUtils.hash(value);
    assertEquals(expected, result);
  }

  @Test
  public void testHashArray() {
    double[] value = {1.2, 2.3, 3.4};
    int expected = Arrays.hashCode(value);
    int result = MathUtils.hash(value);
    assertEquals(expected, result);
  }

  @Test
  public void testNormalizeAngle() {
    double a = 10.5;
    double center = 5.0;
    double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
    double result = MathUtils.normalizeAngle(a, center);
    assertEquals(expected, result, 0.000001);
  }

  @Test
  public void testReduce() {
    double a = 10.5;
    double period = 5.0;
    double offset = 2.0;
    double p = Math.abs(period);
    double expected = a - p * Math.floor((a - offset) / p) - offset;
    double result = MathUtils.reduce(a, period, offset);
    assertEquals(expected, result, 0.000001);
  }

  @Test
  public void testCopySignByte() {
    byte magnitude = 10;
    byte sign = -1;
    byte expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (byte)(-magnitude);
    try {
      byte result = MathUtils.copySign(magnitude, sign);
      assertEquals(expected, result);
    } catch (MathArithmeticException ex) {
      assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
    }
  }

  @Test
  public void testCopySignShort() {
    short magnitude = 1000;
    short sign = -1;
    short expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (short)(-magnitude);
    try {
      short result = MathUtils.copySign(magnitude, sign);
      assertEquals(expected, result);
    } catch (MathArithmeticException ex) {
      assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
    }
  }

  @Test
  public void testCopySignInt() {
    int magnitude = 100000;
    int sign = -1;
    int expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : -magnitude;
    try {
      int result = MathUtils.copySign(magnitude, sign);
      assertEquals(expected, result);
    } catch (MathArithmeticException ex) {
      assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
    }
  }

  @Test
  public void testCopySignLong() {
    long magnitude = 1000000000;
    long sign = -1;
    long expected = (magnitude >= 0L && sign >= 0L) || (magnitude < 0L && sign < 0L) ? magnitude : -magnitude;
    try {
      long result = MathUtils.copySign(magnitude, sign);
      assertEquals(expected, result);
    } catch (MathArithmeticException ex) {
      assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
    }
  }

  @Test
  public void testCheckFinite() {
    double x = 10.5;
    try {
      MathUtils.checkFinite(x);
    } catch (NotFiniteNumberException ex) {
      fail("NotFiniteNumberException thrown");
    }
  }

  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteWithInfinity() {
    double x = Double.POSITIVE_INFINITY;
    MathUtils.checkFinite(x);
  }

  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteWithNaN() {
    double x = Double.NaN;
    MathUtils.checkFinite(x);
  }

  @Test
  public void testCheckFiniteArray() {
    double[] val = {1.2, 2.3, 3.4};
    try {
      MathUtils.checkFinite(val);
    } catch (NotFiniteNumberException ex) {
      fail("NotFiniteNumberException thrown");
    }
  }

  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteArrayWithInfinity() {
    double[] val = {1.2, Double.POSITIVE_INFINITY, 3.4};
    MathUtils.checkFinite(val);
  }

  @Test(expected = NotFiniteNumberException.class)
  public void testCheckFiniteArrayWithNaN() {
    double[] val = {1.2, Double.NaN, 3.4};
    MathUtils.checkFinite(val);
  }

  @Test
  public void testCheckNotNull() {
    Object o = new Object();
    try {
      MathUtils.checkNotNull(o);
    } catch (NullArgumentException ex) {
      fail("NullArgumentException thrown");
    }
  }

  @Test(expected = NullArgumentException.class)
  public void testCheckNotNullWithNull() {
    Object o = null;
    MathUtils.checkNotNull(o);
  }

  @Test
  public void testCheckNotNullWithPattern() {
    Object o = new Object();
    Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
    Object[] args = {1};
    try {
      MathUtils.checkNotNull(o, pattern, args);
    } catch (NullArgumentException ex) {
      fail("NullArgumentException thrown");
    }
  }

  @Test(expected = NullArgumentException.class)
  public void testCheckNotNullWithPatternAndNull() {
    Object o = null;
    Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
    Object[] args = {1};
    MathUtils.checkNotNull(o, pattern, args);
  }
}

