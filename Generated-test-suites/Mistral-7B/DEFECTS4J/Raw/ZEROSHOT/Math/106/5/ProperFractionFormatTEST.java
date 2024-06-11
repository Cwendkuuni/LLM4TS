import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedConstructor;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ProperFractionFormatTest {

 private ProperFractionFormat format;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @BeforeEach
 void setUp() {
 // Create a ProperFractionFormat instance with default settings
 format = new ProperFractionFormat();

 // Create custom NumberFormat instances for testing
 wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 wholeFormat.setMinimumFractionDigits(2);
 wholeFormat.setMaximumFractionDigits(2);
 wholeFormat.setGroupingUsed(false);

 numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 numeratorFormat.setMinimumFractionDigits(2);
 numeratorFormat.setMaximumFractionDigits(2);
 numeratorFormat.setGroupingUsed(false);

 denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 denominatorFormat.setMinimumFractionDigits(2);
 denominatorFormat.setMaximumFractionDigits(2);
 denominatorFormat.setGroupingUsed(false);
 }

 @Test
 void testDefaultFormat() {
 // Test formatting a Fraction with default settings
 Fraction fraction = new Fraction(1, 3);
 String expected = "1 1/3";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 void testCustomFormat() {
 // Test formatting a Fraction with custom settings
 Fraction fraction = new Fraction(1, 3);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String expected = "-1.00 1.00/3.00";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatNegativeFraction() {
 // Test formatting a negative Fraction
 Fraction fraction = new Fraction(-1, 3);
 String expected = "-1.00 1.00/3.00";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 void testFormatLargeFraction() {
 // Test formatting a large Fraction
 Fraction fraction = new Fraction(BigInteger.valueOf(1234567890), BigInteger.valueOf(987654321));
 String expected = "-1234567890.00 1234567890.00/987654321.00";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @ParameterizedConstructor(names = {"testParseFractionWithWhole", "testParseFractionWithoutWhole", "testParseInvalidFraction"})
 @ValueSource(strings = {"-3 2/3", "2/3", "-3 -1/2"})
 void testParse(String source) {
 Fraction expected = new Fraction(1, 2); // Default value for comparison
 if (source.startsWith("-")) {
 expected = new Fraction(-1, 2);
 }
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual, "Parse error");
 assertEquals(source.length(), pos.getIndex(), "Parse index");
 }

 @Test
 void testParseWholePart() {
 // Test parsing a fraction with a whole part
 String source = "-3 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = format.parse(source, pos);
 Fraction expected = new Fraction(-3, 2);
 assertEquals(expected, actual, "Parse error");
 assertEquals(source.length(), pos.getIndex(), "Parse index");
 }

 @Test
 void testParseNumeratorPart() {
 // Test parsing a fraction without a whole part
 String source = "2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = format.parse(source, pos);
 Fraction expected = new Fraction(1, 2);
 assertEquals(expected, actual, "Parse error");
 assertEquals(source.length(), pos.getIndex(), "Parse index");
 }

 @Test
 void testParseInvalidFraction() {
 String source = "-3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = format.parse(source, pos);
 assertNull(actual, "Parse should return null for invalid fraction");
 assertEquals(-1, pos.getErrorIndex(), "Parse index should be set to error index");
 }

 @Test
 void testParseLargeFraction() {
 // Test parsing a large fraction
 String source = "-1234567890.00 1234567890.00/987654321.00";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = format.parse(source, pos);
 Fraction expected = new Fraction(BigInteger.valueOf(1234567890), BigInteger.valueOf(987654321));
 assertEquals(expected, actual, "Parse error");
 assertEquals(source.length(), pos.getIndex(), "Parse index");
 }

 @Test
 void testFormatRounding() {
 // Test formatting a Fraction with rounding
 format.setRoundingMode(RoundingMode.HALF_UP);
 Fraction fraction = new Fraction(BigDecimal.valueOf(1.1), BigDecimal.valueOf(3.0));
 String expected = "-3.5 1.1/3.0";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual, "Format error");
 }

 @Test
 void testFormatWithGrouping() {
 // Test formatting a Fraction with grouping
 wholeFormat.setGroupingUsed(true);
 numeratorFormat.setGroupingUsed(true);
 denominatorFormat.setGroupingUsed(true);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(1000, 3000);
 String expected = "1,000.00 1,000.00/3,000.00";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual, "Format error");
 }
}