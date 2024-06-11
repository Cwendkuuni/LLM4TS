import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {
  
  @Test
  public void testFormat() {
    ProperFractionFormat format = new ProperFractionFormat();
    Fraction fraction = new Fraction(3, 4);
    String expected = "3 / 4";
    String result = format.format(fraction, null, null).toString();
    assertEquals(expected, result);
  }
  
  @Test
  public void testParse() {
    ProperFractionFormat format = new ProperFractionFormat();
    String source = "3 / 4";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(3, 4);
    Fraction result = format.parse(source, pos);
    assertEquals(expected, result);
  }
  
  @Test
  public void testParseNegativeWholeNumber() {
    ProperFractionFormat format = new ProperFractionFormat();
    String source = "-3 / 4";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(-3, 4);
    Fraction result = format.parse(source, pos);
    assertEquals(expected, result);
  }
  
  @Test
  public void testParseNegativeNumerator() {
    ProperFractionFormat format = new ProperFractionFormat();
    String source = "3 / -4";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(-3, 4);
    Fraction result = format.parse(source, pos);
    assertEquals(expected, result);
  }
  
  @Test
  public void testParseNegativeDenominator() {
    ProperFractionFormat format = new ProperFractionFormat();
    String source = "3 / 4";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(3, -4);
    Fraction result = format.parse(source, pos);
    assertEquals(expected, result);
  }
  
  @Test(expected = ParseException.class)
  public void testParseInvalidFormat() {
    ProperFractionFormat format = new ProperFractionFormat();
    String source = "3 - 1 / 2";
    ParsePosition pos = new ParsePosition(0);
    format.parse(source, pos);
  }
  
  @Test(expected = ParseException.class)
  public void testParseInvalidWholeNumber() {
    ProperFractionFormat format = new ProperFractionFormat();
    String source = "3.5 / 4";
    ParsePosition pos = new ParsePosition(0);
    format.parse(source, pos);
  }
  
  @Test(expected = ParseException.class)
  public void testParseInvalidNumerator() {
    ProperFractionFormat format = new ProperFractionFormat();
    String source = "3 / 4.5";
    ParsePosition pos = new ParsePosition(0);
    format.parse(source, pos);
  }
  
  @Test(expected = ParseException.class)
  public void testParseInvalidDenominator() {
    ProperFractionFormat format = new ProperFractionFormat();
    String source = "3 / 4.5";
    ParsePosition pos = new ParsePosition(0);
    format.parse(source, pos);
  }
  
}