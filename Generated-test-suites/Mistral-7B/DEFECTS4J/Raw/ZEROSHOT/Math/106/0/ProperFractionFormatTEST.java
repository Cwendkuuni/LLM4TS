import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.simplerules.NumberFormatRule;
import org.apache.commons.text.simplerules.ParsePosition;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.NumberFormat;
import java.text.FieldPosition;
import java.util.Locale;

public class ProperFractionFormatTest {

 private ProperFractionFormat pff;
 private NumberFormat numberFormat;

 @Test
 public void testDefaultFormat() {
 pff = new ProperFractionFormat();
 Fraction fraction = pff.parse("3 1/2", new ParsePosition(0));
 assertEquals(new Fraction(3, 2), fraction);
 }

 @Test
 public void testCustomFormat() {
 numberFormat = new NumberFormatRule("0.##", Locale.US);
 pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 Fraction fraction = pff.parse("3 1.5/2", new ParsePosition(0));
 assertEquals(new Fraction(3, 2, 1, 2), fraction);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFormat() {
 try {
 pff = new ProperFractionFormat(null, numberFormat, numberFormat);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got " + e.getMessage());
 }
 }

 @Test
 public void testFormat() {
 pff = new ProperFractionFormat();
 Fraction fraction = new Fraction(1, 3, 2, 3);
 String expected = "2 1/3";
 String formatResult = pff.format(fraction, new StringBuffer(), null);
 assertEquals(expected, formatResult.toString());
 }

 @Test
 public void testFormatWithCustomWholeFormat() {
 numberFormat = new NumberFormatRule("0.-#", Locale.US);
 pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 Fraction fraction = new Fraction(1, 3, 2, 3);
 String expected = "-2.0- 1/3";
 String formatResult = pff.format(fraction, new StringBuffer(), null);
 assertEquals(expected, formatResult.toString());
 }

 @Test
 public void testParse() {
 Fraction fraction = pff.parse("3 1/2", new ParsePosition(0));
 assertEquals(new Fraction(3, 2), fraction);
 }

 @Test
 public void testParseWithWhiteSpace() {
 Fraction fraction = pff.parse("3  1/2", new ParsePosition(0));
 assertEquals(new Fraction(3, 2), fraction);
 }

 @Test(expected = ParseException.class)
 public void testParseNullValue() {
 ParsePosition pos = new ParsePosition(0);
 pff.parse(null, pos);
 fail("Expected ParseException, but got null");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFraction() {
 ParsePosition pos = new ParsePosition(0);
 pff.parse("3 -1/2", pos);
 fail("Expected ParseException, but got " + pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFractionWithTrailingMinusSign() {
 ParsePosition pos = new ParsePosition(0);
 pff.parse("3 -1/2-", pos);
 fail("Expected ParseException, but got " + pos.getErrorIndex());
 }

 @Test
 public void testParseImproperFraction() {
 Fraction fraction = pff.parse("5 1/2", new ParsePosition(0));
 assertEquals(new Fraction(7, 2), fraction);
 }

 @Test
 public void testParseImproperFractionWithLeadingZero() {
 Fraction fraction = pff.parse("0 5 1/2", new ParsePosition(0));
 assertEquals(new Fraction(7, 2), fraction);
 }

 @Test
 public void testParseImproperFractionWithLeadingWhitespace() {
 ParsePosition pos = new ParsePosition(0);
 String input = "  5 1/2";
 Fraction fraction = pff.parse(input, pos);
 assertEquals(new Fraction(7, 2), fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 @Test
 public void testParseImproperFractionWithTrailingWhitespace() {
 ParsePosition pos = new ParsePosition(0);
 String input = "5 1/2  ";
 Fraction fraction = pff.parse(input, pos);
 assertEquals(new Fraction(7, 2), fraction);
 assertEquals(input.length() - 1, pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidLeadingZero() {
 ParsePosition pos = new ParsePosition(0);
 pff.parse("00 1/2", pos);
 fail("Expected ParseException, but got " + pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseNegativeWholeNumber() {
 ParsePosition pos = new ParsePosition(0);
 pff.parse("3/2", pos);
 fail("Expected ParseException, but got " + pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidNegativeWholeNumber() {
 ParsePosition pos = new ParsePosition(0);
 pff.parse("-3/2", pos);
 fail("Expected ParseException, but got " + pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidTrailingMinusSign() {
 ParsePosition pos = new ParsePosition(0);
 pff.parse("3 1/2-", pos);
 fail("Expected ParseException, but got " + pos.getErrorIndex());
 }

 @Test
 public void testParseCustomFormat() {
 numberFormat = new NumberFormatRule("0.-#", Locale.US);
 pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 Fraction fraction = pff.parse("-3.0- 1/2", new ParsePosition(0));
 assertEquals(new Fraction(-7, 2), fraction);
 }

 @Test
 public void testParseCustomFormatWithLeadingZero() {
 numberFormat = new NumberFormatRule("0.-#", Locale.US);
 pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 Fraction fraction = pff.parse("0.-3.0- 1/2", new ParsePosition(0));
 assertEquals(new Fraction(-7, 2), fraction);
 }

 @Test
 public void testParseCustomFormatWithTrailingZero() {
 numberFormat = new NumberFormatRule("0.-#", Locale.US);
 pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 Fraction fraction = pff.parse("-3.0- 1/2 0", new ParsePosition(0));
 assertEquals(new Fraction(-7, 2), fraction);
 }

 @Test
 public void testParseCustomFormatWithLeadingWhitespace() {
 numberFormat = new NumberFormatRule("0.-#", Locale.US);
 pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 ParsePosition pos = new ParsePosition(0);
 String input = "   -3.0- 1/2";
 Fraction fraction = pff.parse(input, pos);
 assertEquals(new Fraction(-7, 2), fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 @Test
 public void testParseCustomFormatWithTrailingWhitespace() {
 numberFormat = new NumberFormatRule("0.-#", Locale.US);
 pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 ParsePosition pos = new ParsePosition(0);
 String input = "-3.0- 1/2   ";
 Fraction fraction = pff.parse(input, pos);
 assertEquals(new Fraction(-7, 2), fraction);
 assertEquals(input.length() - 1, pos.getIndex());
 }

 @Test
 public void testParseCustomFormatWithLeadingMinusSign() {
 numberFormat = new NumberFormatRule("0.-#", Locale.US);
 pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 ParsePosition pos = new ParsePosition(0);
 String input = "--3.0- 1/2";
 Fraction fraction = pff.parse(input, pos);
 assertEquals(new Fraction(-7, 2), fraction);
 assertEquals(input.length() - 1, pos.getIndex());
 }

 @Test
 public void testParseCustomFormatWithTrailingMinusSign() {
 numberFormat = new NumberFormatRule("0.-#", Locale.US);
 pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 ParsePosition pos = new ParsePosition(0);
 String input = "-3.0- 1/2--";
 Fraction fraction = pff.parse(input, pos);
 assertEquals(new Fraction(-7, 2), fraction);
 assertEquals(input.length() - 1, pos.getIndex());
 }

 @Test
 public void testParseCustomFormatWithLeadingWhitespaceAndMinusSign() {
 numberFormat = new NumberFormatRule("0.-#", Locale.US);
 pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 ParsePosition pos = new ParsePosition(0);
 String input = "  --3.0- 1/2";
 Fraction fraction = pff.parse(input, pos);
 assertEquals(new Fraction(-7, 2), fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 @Test
 public void testParseCustomFormatWithTrailingWhitespaceAndMinusSign() {
 numberFormat = new NumberFormatRule("0.-#", Locale.US);
 pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 ParsePosition pos = new ParsePosition(0);
 String input = "-3.0- 1/2   --";
 Fraction fraction = pff.parse(input, pos);
 assertEquals(new Fraction(-7, 2), fraction);
 assertEquals(input.length() - 1, pos.getIndex());
 }
}