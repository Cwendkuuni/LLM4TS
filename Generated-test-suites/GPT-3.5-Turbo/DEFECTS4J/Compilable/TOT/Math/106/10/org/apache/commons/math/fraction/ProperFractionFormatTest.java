package org.apache.commons.math.fraction;
import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;
import java.text.NumberFormat;
import java.text.ParsePosition;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

  private ProperFractionFormat properFractionFormat = new ProperFractionFormat();

  @Test
  public void testFormat_PositiveWholeNumber() {
    Fraction fraction = new Fraction(3, 4);
    String expected = "0 3 / 4";
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testFormat_NegativeWholeNumber() {
    Fraction fraction = new Fraction(-3, 4);
    String expected = "-1 3 / 4";
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testFormat_PositiveNumerator() {
    Fraction fraction = new Fraction(1, 2);
    String expected = "1 / 2";
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testFormat_NegativeNumerator() {
    Fraction fraction = new Fraction(-1, 2);
    String expected = "-1 / 2";
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testFormat_PositiveDenominator() {
    Fraction fraction = new Fraction(3, 5);
    String expected = "0 3 / 5";
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testFormat_NegativeDenominator() {
    Fraction fraction = new Fraction(3, -5);
    String expected = "0 3 / -5";
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testFormat_WholeNumeratorDenominator() {
    Fraction fraction = new Fraction(5, 2);
    String expected = "2 1 / 2";
    String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testParse_PositiveWholeNumber() {
    String source = "3 4";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(3, 4);
    Fraction result = properFractionFormat.parse(source, pos);
    assertEquals(expected, result);
  }

  @Test
  public void testParse_NegativeWholeNumber() {
    String source = "-3 4";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(-3, 4);
    Fraction result = properFractionFormat.parse(source, pos);
    assertEquals(expected, result);
  }

  @Test
  public void testParse_PositiveNumerator() {
    String source = "1 / 2";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(1, 2);
    Fraction result = properFractionFormat.parse(source, pos);
    assertEquals(expected, result);
  }

  @Test
  public void testParse_NegativeNumerator() {
    String source = "-1 / 2";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(-1, 2);
    Fraction result = properFractionFormat.parse(source, pos);
    assertEquals(expected, result);
  }

  @Test
  public void testParse_PositiveDenominator() {
    String source = "0 3 / 5";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(3, 5);
    Fraction result = properFractionFormat.parse(source, pos);
    assertEquals(expected, result);
  }

  @Test
  public void testParse_NegativeDenominator() {
    String source = "0 3 / -5";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(3, -5);
    Fraction result = properFractionFormat.parse(source, pos);
    assertEquals(expected, result);
  }

  @Test
  public void testParse_WholeNumeratorDenominator() {
    String source = "2 1 / 2";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(5, 2);
    Fraction result = properFractionFormat.parse(source, pos);
    assertEquals(expected, result);
  }
}