import org.junit.Assert;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {

  @Test
  public void testFormatWithPositiveFraction() {
    ProperFractionFormat format = new ProperFractionFormat();

    Fraction fraction = new Fraction(3, 4);
    String expected = "3 / 4";
    String actual = format.format(fraction);
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithNegativeFraction() {
    ProperFractionFormat format = new ProperFractionFormat();

    Fraction fraction = new Fraction(-5, 2);
    String expected = "-5 / 2";
    String actual = format.format(fraction);
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithZeroFraction() {
    ProperFractionFormat format = new ProperFractionFormat();

    Fraction fraction = new Fraction(0, 5);
    String expected = "0";
    String actual = format.format(fraction);
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithWholeNumber() {
    ProperFractionFormat format = new ProperFractionFormat();

    Fraction fraction = new Fraction(7);
    String expected = "7";
    String actual = format.format(fraction);
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithMixedNumber() {
    ProperFractionFormat format = new ProperFractionFormat();

    Fraction fraction = new Fraction(3, 2);
    String expected = "1 1/2";
    String actual = format.format(fraction);
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithCustomFormat() {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);

    ProperFractionFormat format = new ProperFractionFormat(nf);

    Fraction fraction = new Fraction(2, 3);
    String expected = "2.67 / 3";
    String actual = format.format(fraction);
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testParseWithValidWholeNumber() {
    ProperFractionFormat format = new ProperFractionFormat();

    String source = "5";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(5);
    Fraction actual = format.parse(source, pos);
    
    Assert.assertEquals(expected, actual);
    Assert.assertEquals(source.length(), pos.getIndex());
  }

  @Test
  public void testParseWithValidFraction() {
    ProperFractionFormat format = new ProperFractionFormat();

    String source = "9 / 4";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(9, 4);
    Fraction actual = format.parse(source, pos);
    
    Assert.assertEquals(expected, actual);
    Assert.assertEquals(source.length(), pos.getIndex());
  }

  @Test
  public void testParseWithInvalidNumberFormat() {
    ProperFractionFormat format = new ProperFractionFormat();

    String source = "abc";
    ParsePosition pos = new ParsePosition(0);
    Fraction actual = format.parse(source, pos);
    
    Assert.assertNull(actual);
    Assert.assertEquals(0, pos.getIndex());
  }

  @Test
  public void testParseWithInvalidFractionFormat() {
    ProperFractionFormat format = new ProperFractionFormat();

    String source = "4 /";
    ParsePosition pos = new ParsePosition(0);
    Fraction actual = format.parse(source, pos);
    
    Assert.assertNull(actual);
    Assert.assertEquals(2, pos.getIndex());
  }

  @Test
  public void testParseWithInvalidMinusSignPosition() {
    ProperFractionFormat format = new ProperFractionFormat();

    String source = "-1 -3/4";
    ParsePosition pos = new ParsePosition(0);
    Fraction actual = format.parse(source, pos);
    
    Assert.assertNull(actual);
    Assert.assertEquals(0, pos.getIndex());
  }
}