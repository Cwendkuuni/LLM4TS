package org.apache.commons.math.fraction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

  private ProperFractionFormat properFractionFormat;

  @Before
  public void setUp() {
    properFractionFormat = new ProperFractionFormat();
  }

  @Test
  public void testFormat() {
    Fraction fraction = new Fraction(3, 4);
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals("3/4", result);
  }

  @Test
  public void testFormatWithWholeNumber() {
    Fraction fraction = new Fraction(5, 4);
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals("1 1/4", result);
  }

  @Test
  public void testFormatWithNegativeFraction() {
    Fraction fraction = new Fraction(-3, 5);
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals("-3/5", result);
  }

  @Test
  public void testFormatWithNegativeWholeNumber() {
    Fraction fraction = new Fraction(-7, 4);
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals("-1 3/4", result);
  }

  @Test
  public void testFormatWithZeroFraction() {
    Fraction fraction = new Fraction(0, 5);
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals("0", result);
  }

  @Test
  public void testParse() {
    Fraction result = properFractionFormat.parse("3/4", null);
    Fraction expected = new Fraction(3, 4);
    assertEquals(expected, result);
  }

  @Test
  public void testParseWithWholeNumber() {
    Fraction result = properFractionFormat.parse("1 1/4", null);
    Fraction expected = new Fraction(5, 4);
    assertEquals(expected, result);
  }

  @Test
  public void testParseWithNegativeFraction() {
    Fraction result = properFractionFormat.parse("-3/5", null);
    Fraction expected = new Fraction(-3, 5);
    assertEquals(expected, result);
  }

  @Test
  public void testParseWithNegativeWholeNumber() {
    Fraction result = properFractionFormat.parse("-1 3/4", null);
    Fraction expected = new Fraction(-7, 4);
    assertEquals(expected, result);
  }

  @Test
  public void testParseWithZeroFraction() {
    Fraction result = properFractionFormat.parse("0", null);
    Fraction expected = new Fraction(0, 1);
    assertEquals(expected, result);
  }
}