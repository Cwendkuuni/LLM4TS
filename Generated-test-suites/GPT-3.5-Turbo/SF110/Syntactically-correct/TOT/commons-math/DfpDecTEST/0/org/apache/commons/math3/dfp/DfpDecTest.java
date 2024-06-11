import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DfpDecTest {
  
  private DfpDec dfpDec;
  
  @Before
  public void setUp() {
    dfpDec = new DfpDec(null);
  }
  
  @Test
  public void testDfpDecConstructor() {
    assertNotNull(dfpDec);
  }
  
  @Test
  public void testDfpDecByteConstructor() {
    byte x = 1;
    DfpDec result = dfpDec.newInstance(x);
    assertNotNull(result);
    assertEquals(x, result.toByteArray()[0]);
  }
  
  @Test
  public void testDfpDecIntConstructor() {
    int x = 10;
    DfpDec result = dfpDec.newInstance(x);
    assertNotNull(result);
    assertEquals(Double.valueOf(x), result.toDouble());
  }
  
  @Test
  public void testDfpDecLongConstructor() {
    long x = 100;
    DfpDec result = dfpDec.newInstance(x);
    assertNotNull(result);
    assertEquals(Double.valueOf(x), result.toDouble());
  }
  
  @Test
  public void testDfpDecDoubleConstructor() {
    double x = 10.5;
    DfpDec result = dfpDec.newInstance(x);
    assertNotNull(result);
    assertEquals(x, result.toDouble(), 0.01);
  }
  
  @Test
  public void testDfpDecDfpConstructor() {
    Dfp dec = new DfpDec(10);
    DfpDec result = dfpDec.newInstance(dec);
    assertNotNull(result);
    assertEquals(dec, result);
  }
  
  @Test
  public void testDfpDecStringConstructor() {
    String s = "10.5";
    DfpDec result = dfpDec.newInstance(s);
    assertNotNull(result);
    assertEquals(Double.valueOf(s), result.toDouble());
  }
  
  @Test
  public void testDfpDecByteSignNansConstructor() {
    byte sign = 1;
    byte nans = 2;
    DfpDec result = dfpDec.newInstance(sign, nans);
    assertNotNull(result);
    assertEquals(sign, result.getSign());
    assertEquals(nans, result.getNans());
  }
  
  @Test
  public void testGetDecimalDigits() {
    int expected = dfpDec.getRadixDigits() * 4 - 3;
    int result = dfpDec.getDecimalDigits();
    assertEquals(expected, result);
  }
  
  @Test
  public void testRound() {
    int in = 5;
    int result = dfpDec.round(in);
    assertEquals(in, result);
  }
  
  @Test
  public void testNextAfter() {
    Dfp x = new DfpDec(10);
    DfpDec result = dfpDec.nextAfter(x);
    assertNotNull(result);
    assertTrue(result.lessThan(x));
  }

}