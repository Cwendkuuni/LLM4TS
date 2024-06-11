import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 // Create a ProperFractionFormat instance with default number formats
 properFractionFormat = new ProperFractionFormat();

 // Create custom number formats for testing
 wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 wholeFormat.setMinimumFractionDigits(0);
 wholeFormat.setMaximumFractionDigits(3);

 numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 numeratorFormat.setMinimumFractionDigits(0);
 numeratorFormat.setMaximumFractionDigits(3);

 denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 denominatorFormat.setMinimumFractionDigits(0);
 denominatorFormat.setMaximumFractionDigits(3);

 // Create a ProperFractionFormat instance with custom number formats
 properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 }

 // Test method: testFormat
 @Test
 public void testFormat() {
 // Test formatting a Fraction object
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 1/2";
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);

 String formatted = properFractionFormat.format(fraction, toAppendTo, pos);
 Assert.assertEquals(expected, toAppendTo.toString());

 // Test formatting a BigDecimal as a Fraction object
 BigDecimal bigDecimal = new BigDecimal("3.14159265358979323846");
 Fraction formattedFraction = new Fraction(bigDecimal);
 expected = "3 14159/100000";
 formatted = properFractionFormat.format(formattedFraction, toAppendTo, pos);
 Assert.assertEquals(expected, toAppendTo.toString());
 }

 // Test method: testParse
 @Test
 public void testParse() {
 // Test parsing a simple proper fraction
 String source = "1 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(1, 2);
 Fraction parsed = properFractionFormat.parse(source, pos);
 Assert.assertEquals(expected, parsed);
 Assert.assertEquals(source.length(), pos.getIndex());

 // Test parsing a fraction with a negative whole number
 source = "-1 2/3";
 expected = new Fraction(-1, 2);
 parsed = properFractionFormat.parse(source, pos);
 Assert.assertEquals(expected, parsed);
 Assert.assertEquals(source.length(), pos.getIndex());

 // Test parsing a fraction with a numerator containing decimal points
 source = "1.5 2/3";
 expected = new Fraction(3, 2); // The fraction 1.5 is converted to 3/2 before parsing
 parsed = properFractionFormat.parse(source, pos);
 Assert.assertEquals(expected, parsed);
 Assert.assertEquals(source.length(), pos.getIndex());

 // Test parsing an improper fraction
 // The improper fraction "5 1/2" is internally converted to "5 2" and then to "6"
 source = "5 1/2";
 expected = new Fraction(6);
 parsed = properFractionFormat.parse(source, pos);
 Assert.assertEquals(expected, parsed);
 Assert.assertEquals(source.length(), pos.getIndex());

 // Test parsing an invalid fraction with a negative denominator
 source = "1 -1/2";
 Fraction parsedWithSign = properFractionFormat.parse(source, pos);
 Assert.assertNull(parsedWithSign);
 Assert.assertEquals(0, pos.getIndex());

 // Test parsing an invalid fraction with a negative numerator
 source = "-1 1/2";
 parsedWithSign = properFractionFormat.parse(source, pos);
 Assert.assertNull(parsedWithSign);
 Assert.assertEquals(1, pos.getIndex());

 // Test parsing an invalid fraction with a negative whole number and a negative numerator
 source = "-1 -1/2";
 parsedWithSign = properFractionFormat.parse(source, pos);
 Assert.assertNull(parsedWithSign);
 Assert.assertEquals(2, pos.getIndex());
 }

 // Test method: testSetWholeFormat
 @Test
 public void testSetWholeFormat() {
 // Set a custom whole format and test formatting and parsing
 wholeFormat.setPattern("0");
 properFractionFormat.setWholeFormat(wholeFormat);
 String expectedFormat = "0";
 String source = "0 1/2";
 String expected = "0 1/2";
 Fraction expectedFraction = new Fraction(0, 1);
 String toAppendTo = new StringBuffer();
 Fraction parsedFraction = null;

 // Test formatting
 String formatted = properFractionFormat.format(expectedFraction, toAppendTo, new FieldPosition(0, 0));
 Assert.assertEquals(expectedFormat, toAppendTo.toString());

 // Test parsing
 parsedFraction = properFractionFormat.parse(source, new ParsePosition(0));
 Assert.assertEquals(expectedFraction, parsedFraction);
 }

 // Test method: testSetNumeratorFormat
 @Test
 public void testSetNumeratorFormat() {
 // Set a custom numerator format and test formatting and parsing
 numeratorFormat.setPattern("#,##0.###");
 properFractionFormat.setNumeratorFormat(numeratorFormat);
 String expectedFormat = "3,142";
 String source = "1 1/2";
 String expected = "3,142 / 2";
 Fraction expectedFraction = new Fraction(3, 2);
 String toAppendTo = new StringBuffer();
 Fraction parsedFraction = null;

 // Test formatting
 String formatted = properFractionFormat.format(expectedFraction, toAppendTo, new FieldPosition(0, 0));
 Assert.assertEquals(expectedFormat, toAppendTo.toString());

 // Test parsing
 parsedFraction = properFractionFormat.parse(source, new ParsePosition(0));
 Assert.assertEquals(expectedFraction, parsedFraction);
 }

 // Test method: testSetDenominatorFormat
 @Test
 public void testSetDenominatorFormat() {
 // Set a custom denominator format and test formatting and parsing
 denominatorFormat.setPattern("#,##0.###");
 properFractionFormat.setDenominatorFormat(denominatorFormat);
 String expectedFormat = "3,142";
 String source = "1 1/2";
 String expected = "1 / 3,142";
 Fraction expectedFraction = new Fraction(1, 3);
 String toAppendTo = new StringBuffer();
 Fraction parsedFraction = null;

 // Test formatting
 String formatted = properFractionFormat.format(expectedFraction, toAppendTo, new FieldPosition(0, 0));
 Assert.assertEquals(expectedFormat, toAppendTo.toString());

 // Test parsing
 parsedFraction = properFractionFormat.parse(source, new ParsePosition(0));
 Assert.assertEquals(expectedFraction, parsedFraction);
 }
}