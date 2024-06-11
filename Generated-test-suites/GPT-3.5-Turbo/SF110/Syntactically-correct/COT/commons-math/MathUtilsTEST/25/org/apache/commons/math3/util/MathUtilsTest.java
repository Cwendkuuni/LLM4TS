import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {
  
  // hash tests
  @Test
  public void testHash() {
    assertEquals(10, MathUtils.hash(10.0));
    assertEquals(-5, MathUtils.hash(-5.0));
    assertEquals(0, MathUtils.hash(0.0));
  }
  
  // normalizeAngle tests
  @Test
  public void testNormalizeAngle() {
    assertEquals(0, MathUtils.normalizeAngle(0, 0), 0);
    assertEquals(1.5707963267948966, MathUtils.normalizeAngle(1.5707963267948966, 0), 0);
    assertEquals(0, MathUtils.normalizeAngle(6.283185307179586, 0), 0);
    assertEquals(-1.5707963267948966, MathUtils.normalizeAngle(-1.5707963267948966, 0), 0);
    assertEquals(0, MathUtils.normalizeAngle(-6.283185307179586, 0), 0);
  }
  
  // reduce tests
  @Test
  public void testReduce() {
    assertEquals(0, MathUtils.reduce(0, 1, 0), 0);
    assertEquals(1, MathUtils.reduce(1, 1, 0), 0);
    assertEquals(2, MathUtils.reduce(2, 1, 0), 0);
    assertEquals(3, MathUtils.reduce(3, 1, 0), 0);
    assertEquals(0, MathUtils.reduce(4, 1, 0), 0);
    assertEquals(-1, MathUtils.reduce(-1, 1, 0), 0);
    assertEquals(-2, MathUtils.reduce(-2, 1, 0), 0);
    assertEquals(-3, MathUtils.reduce(-3, 1, 0), 0);
    assertEquals(0, MathUtils.reduce(-4, 1, 0), 0);
  }
  
  // copySign tests
  @Test
  public void testCopySignByte() {
    assertEquals(10, MathUtils.copySign((byte)10, (byte)5));
    assertEquals(-10, MathUtils.copySign((byte)10, (byte)-5));
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignByteException() {
    assertEquals(-128, MathUtils.copySign((byte)-128, (byte)1));
  }
  
  @Test
  public void testCopySignShort() {
    assertEquals(10, MathUtils.copySign((short)10, (short)5));
    assertEquals(-10, MathUtils.copySign((short)10, (short)-5));
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignShortException() {
    assertEquals(-32768, MathUtils.copySign((short)-32768, (short)1));
  }
  
  @Test
  public void testCopySignInt() {
    assertEquals(10, MathUtils.copySign(10, 5));
    assertEquals(-10, MathUtils.copySign(10, -5));
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignIntException() {
    assertEquals(Integer.MIN_VALUE, MathUtils.copySign(Integer.MIN_VALUE, 1));
  }
  
  @Test
  public void testCopySignLong() {
    assertEquals(10L, MathUtils.copySign(10L, 5L));
    assertEquals(-10L, MathUtils.copySign(10L, -5L));
  }
  
  @Test(expected = MathArithmeticException.class)
  public void testCopySignLongException() {
    assertEquals(Long.MIN_VALUE, MathUtils.copySign(Long.MIN_VALUE, 1L));
  }
  
  // checkFinite tests
  @Test
  public void testCheckFinite() {
    // TODO: Write test cases to cover all possible scenarios
  }
  
  // checkNotNull tests
  @Test
  public void testCheckNotNullLocalizable() {
    // TODO: Write test cases to cover all possible scenarios
  }
  
  @Test
  public void testCheckNotNull() {
    // TODO: Write test cases to cover all possible scenarios
  }
}