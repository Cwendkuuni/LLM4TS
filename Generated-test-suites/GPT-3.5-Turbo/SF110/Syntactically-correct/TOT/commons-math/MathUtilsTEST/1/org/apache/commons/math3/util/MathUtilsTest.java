import static org.junit.Assert.*;
import org.junit.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;

public class MathUtilsTest {

  @Test
  public void testHash_double() {
    // Typical use case
    double value1 = 5.5;
    int expected1 = Double.valueOf(value1).hashCode();
    assertEquals(expected1, MathUtils.hash(value1));
    
    // Edge case
    double value2 = -0.0;
    int expected2 = Double.valueOf(value2).hashCode();
    assertEquals(expected2, MathUtils.hash(value2));
    
    // Error scenario
    double value3 = Double.NaN;
    int expected3 = Double.valueOf(value3).hashCode();
    assertEquals(expected3, MathUtils.hash(value3));
    
  }
  
  @Test
  public void testHash_doubleArray() {
    // Typical use case
    double[] value1 = {1.1, 2.2, 3.3};
    int expected1 = Arrays.hashCode(value1);
    assertEquals(expected1, MathUtils.hash(value1));
    
    // Edge case
    double[] value2 = {};
    int expected2 = Arrays.hashCode(value2);
    assertEquals(expected2, MathUtils.hash(value2));
    
    // Error scenario
    double[] value3 = null;
    int expected3 = Arrays.hashCode(value3);
    assertEquals(expected3, MathUtils.hash(value3));
    
  }

  @Test
  public void testNormalizeAngle() {
    // Typical use case
    double angle1 = 7.3;
    double center1 = 1.2;
    double expected1 = angle1 - 6.283185307179586 * FastMath.floor((angle1 + 3.141592653589793 - center1) / 6.283185307179586);
    assertEquals(expected1, MathUtils.normalizeAngle(angle1, center1), 0.000001);
    
    // Edge case
    double angle2 = 0.0;
    double center2 = 0.0;
    double expected2 = angle2 - 6.283185307179586 * FastMath.floor((angle2 + 3.141592653589793 - center2) / 6.283185307179586);
    assertEquals(expected2, MathUtils.normalizeAngle(angle2, center2), 0.000001);
    
    // Error scenario
    double angle3 = Double.NaN;
    double center3 = Double.NaN;
    double expected3 = angle3 - 6.283185307179586 * FastMath.floor((angle3 + 3.141592653589793 - center3) / 6.283185307179586);
    assertEquals(expected3, MathUtils.normalizeAngle(angle3, center3), 0.000001);
    
  }
  
  @Test
  public void testReduce() {
    // Typical use case
    double a1 = 15.8;
    double period1 = 5.0;
    double offset1 = 2.0;
    double p1 = FastMath.abs(period1);
    double expected1 = a1 - p1 * FastMath.floor((a1 - offset1) / p1) - offset1;
    assertEquals(expected1, MathUtils.reduce(a1, period1, offset1), 0.000001);
    
    // Edge case
    double a2 = -10.0;
    double period2 = -2.0;
    double offset2 = -5.0;
    double p2 = FastMath.abs(period2);
    double expected2 = a2 - p2 * FastMath.floor((a2 - offset2) / p2) - offset2;
    assertEquals(expected2, MathUtils.reduce(a2, period2, offset2), 0.000001);
    
    // Error scenario
    double a3 = Double.NaN;
    double period3 = Double.NaN;
    double offset3 = Double.NaN;
    double p3 = FastMath.abs(period3);
    double expected3 = a3 - p3 * FastMath.floor((a3 - offset3) / p3) - offset3;
    assertEquals(expected3, MathUtils.reduce(a3, period3, offset3), 0.000001);
    
  }
  
  @Test
  public void testCopySign_byte() {
    // Typical use case
    byte magnitude1 = 5;
    byte sign1 = 3;
    byte expected1 = (byte) (magnitude1 >= 0 && sign1 >= 0 || magnitude1 < 0 && sign1 < 0 ? magnitude1 : -magnitude1);
    try {
      assertEquals(expected1, MathUtils.copySign(magnitude1, sign1));
    } catch (MathArithmeticException e) {
      fail();
    }
    
    // Edge case
    byte magnitude2 = -128;
    byte sign2 = 127;
    try {
      assertEquals(expected2, MathUtils.copySign(magnitude2, sign2));
    } catch (MathArithmeticException e) {
      fail();
    }
    
    // Error scenario
    byte magnitude3 = 0;
    byte sign3 = -1;
    try {
      MathUtils.copySign(magnitude3, sign3);
    } catch (MathArithmeticException e) {
      assertEquals("Overflow", e.getMessage());
    }
    
  }
  
  @Test
  public void testCopySign_short() {
    // Typical use case
    short magnitude1 = 250;
    short sign1 = 125;
    short expected1 = (short) (magnitude1 >= 0 && sign1 >= 0 || magnitude1 < 0 && sign1 < 0 ? magnitude1 : -magnitude1);
    try {
      assertEquals(expected1, MathUtils.copySign(magnitude1, sign1));
    } catch (MathArithmeticException e) {
      fail();
    }
    
    // Edge case
    short magnitude2 = -32768;
    short sign2 = -32767;
    try {
      assertEquals(expected2, MathUtils.copySign(magnitude2, sign2));
    } catch (MathArithmeticException e) {
      fail();
    }
    
    // Error scenario
    short magnitude3 = 0;
    short sign3 = 1;
    try {
      MathUtils.copySign(magnitude3, sign3);
    } catch (MathArithmeticException e) {
      assertEquals("Overflow", e.getMessage());
    }
    
  }
  
  @Test
  public void testCopySign_int() {
    // Typical use case
    int magnitude1 = 1500;
    int sign1 = 1000;
    int expected1 = magnitude1 >= 0 && sign1 >= 0 || magnitude1 < 0 && sign1 < 0 ? magnitude1 : -magnitude1;
    try {
      assertEquals(expected1, MathUtils.copySign(magnitude1, sign1));
    } catch (MathArithmeticException e) {
      fail();
    }
    
    // Edge case
    int magnitude2 = Integer.MIN_VALUE;
    int sign2 = Integer.MIN_VALUE + 1;
    try {
      assertEquals(expected2, MathUtils.copySign(magnitude2, sign2));
    } catch (MathArithmeticException e) {
      fail();
    }
    
    // Error scenario
    int magnitude3 = Integer.MAX_VALUE;
    int sign3 = -1;
    try {
      MathUtils.copySign(magnitude3, sign3);
    } catch (MathArithmeticException e) {
      assertEquals("Overflow", e.getMessage());
    }
    
  }
  
  @Test
  public void testCopySign_long() {
    // Typical use case
    long magnitude1 = 5000000000L;
    long sign1 = 500000000L;
    long expected1 = magnitude1 >= 0L && sign1 >= 0L || magnitude1 < 0L && sign1 < 0L ? magnitude1 : -magnitude1;
    try {
      assertEquals(expected1, MathUtils.copySign(magnitude1, sign1));
    } catch (MathArithmeticException e) {
      fail();
    }
    
    // Edge case
    long magnitude2 = Long.MIN_VALUE;
    long sign2 = Long.MIN_VALUE + 1;
    try {
      assertEquals(expected2, MathUtils.copySign(magnitude2, sign2));
    } catch (MathArithmeticException e) {
      fail();
    }
    
    // Error scenario
    long magnitude3 = Long.MAX_VALUE;
    long sign3 = -1;
    try {
      MathUtils.copySign(magnitude3, sign3);
    } catch (MathArithmeticException e) {
      assertEquals("Overflow", e.getMessage());
    }
    
  }
  
  @Test
  public void testCheckFinite_double() {
    // Typical use case
    double x1 = 5.5;
    assertDoesNotThrow(() -> {
      MathUtils.checkFinite(x1);
    });
    
    // Edge case
    double x2 = 0.0;
    assertDoesNotThrow(() -> {
      MathUtils.checkFinite(x2);
    });
    
    // Error scenario
    double x3 = Double.POSITIVE_INFINITY;
    assertThrows(NotFiniteNumberException.class, () -> {
      MathUtils.checkFinite(x3);
    });
    
  }
  
  @Test
  public void testCheckFinite_doubleArray() {
    // Typical use case
    double[] val1 = {1.1, 2.2, 3.3};
    assertDoesNotThrow(() -> {
      MathUtils.checkFinite(val1);
    });
    
    // Edge case
    double[] val2 = {};
    assertDoesNotThrow(() -> {
      MathUtils.checkFinite(val2);
    });
    
    // Error scenario
    double[] val3 = {Double.NEGATIVE_INFINITY, 1.0, Double.NaN};
    assertThrows(NotFiniteNumberException.class, () -> {
      MathUtils.checkFinite(val3);
    });
    
  }
  
  @Test
  public void testCheckNotNull_LocalizableObjectArray() {
    // Typical use case
    Object o1 = new Object();
    Localizable pattern1 = new Localizable() {
      public String getLocalizedString(Locale locale) {
        return "Localized message";
      }
    };
    assertDoesNotThrow(() -> {
      MathUtils.checkNotNull(o1, pattern1);
    });
    
    // Edge case
    Object o2 = new Object();
    assertDoesNotThrow(() -> {
      MathUtils.checkNotNull(o2, pattern1);
    });
    
    // Error scenario
    Object o3 = null;
    assertThrows(NullArgumentException.class, () -> {
      MathUtils.checkNotNull(o3, pattern1);
    });
    
  }
  
  @Test
  public void testCheckNotNull_Object() {
    // Typical use case
    Object o1 = new Object();
    assertDoesNotThrow(() -> {
      MathUtils.checkNotNull(o1);
    });
    
    // Edge case
    Object o2 = new Object();
    assertDoesNotThrow(() -> {
      MathUtils.checkNotNull(o2);
    });
    
    // Error scenario
    Object o3 = null;
    assertThrows(NullArgumentException.class, () -> {
      MathUtils.checkNotNull(o3);
    });
    
  }
}