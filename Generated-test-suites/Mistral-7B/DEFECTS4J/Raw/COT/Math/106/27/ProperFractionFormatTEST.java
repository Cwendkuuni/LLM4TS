import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.similarity.JaroDistance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat defaultNumberFormat;

 @Before
 public void setup() {
 defaultNumberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 properFractionFormat = new ProperFractionFormat(defaultNumberFormat, defaultNumberFormat, defaultNumberFormat);
 }

 @Test
 public void testConstructorWithDefaultNumberFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 // Check if the default constructors sets the whole format correctly
 Assert.assertEquals(defaultNumberFormat, properFractionFormat.getWholeFormat());
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(1, 2);
 StringBuffer expected = new StringBuffer();
 properFractionFormat.format(fraction, expected, null);
 String actual = expected.toString();
 String expectedResult = "1 / 2";
 Assert.assertEquals(expectedResult, actual);

 fraction = new Fraction(-1, 2);
 expected.setLength(0);
 properFractionFormat.format(fraction, expected, null);
 actual = expected.toString();
 expectedResult = "-1 / 2";
 Assert.assertEquals(expectedResult, actual);

 fraction = new Fraction(3, 4);
 expected.setLength(0);
 properFractionFormat.format(fraction, expected, null);
 actual = expected.toString();
 expectedResult = "3 / 4";
 Assert.assertEquals(expectedResult, actual);

 fraction = new Fraction(5, 7);
 expected.setLength(0);
 properFractionFormat.format(fraction, expected, null);
 actual = expected.toString();
 expectedResult = "5 / 7";
 Assert.assertEquals(expectedResult, actual);
 }

 @Test
 public void testFormatWithCustomNumberFormat() {
 NumberFormat wholeFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, defaultNumberFormat, defaultNumberFormat);
 Fraction fraction = new Fraction(1, 2);
 StringBuffer expected = new StringBuffer();
 properFractionFormat.format(fraction, expected, null);
 String actual = expected.toString();
 String expectedResult = "1 % / 2";
 Assert.assertEquals(expectedResult, actual);
 }

 @Test
 public void testParse() {
 // Test parsing a proper fraction
 String input = "1 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = ProperFractionFormat.getParserInstance().parse(input, pos);
 Assert.assertNotNull(fraction);
 Assert.assertEquals(new Fraction(1, 2), fraction);
 Assert.assertEquals(-1, pos.getIndex());

 // Test parsing an improper fraction
 input = "3";
 fraction = ProperFractionFormat.getParserInstance().parse(input, pos);
 Assert.assertNotNull(fraction);
 Assert.assertEquals(new Fraction(3, 1), fraction);
 Assert.assertEquals(input.length(), pos.getIndex());

 // Test parsing an improper fraction with a leading minus sign
 input = "-3";
 fraction = ProperFractionFormat.getParserInstance().parse(input, pos);
 Assert.assertNotNull(fraction);
 Assert.assertEquals(new Fraction(-3, 1), fraction);
 Assert.assertEquals(input.length(), pos.getIndex());

 // Test parsing a proper fraction with a leading minus sign
 input = "-1 / 2";
 fraction = ProperFractionFormat.getParserInstance().parse(input, pos);
 Assert.assertNotNull(fraction);
 Assert.assertEquals(new Fraction(-1, 2), fraction);
 Assert.assertEquals(input.length(), pos.getIndex());

 // Test parsing a proper fraction with a leading minus sign and whitespace
 input = "- 1 / 2";
 fraction = ProperFractionFormat.getParserInstance().parse(input, pos);
 Assert.assertNotNull(fraction);
 Assert.assertEquals(new Fraction(-1, 2), fraction);
 Assert.assertEquals(input.length(), pos.getIndex());

 // Test parsing a proper fraction with multiple whitespaces
 input = " 1 / 2 ";
 fraction = ProperFractionFormat.getParserInstance().parse(input, pos);
 Assert.assertNotNull(fraction);
 Assert.assertEquals(new Fraction(1, 2), fraction);
 Assert.assertEquals(input.length(), pos.getIndex());

 // Test parsing a proper fraction with a fraction part that has multiple digits
 input = "1 2 / 3 4 5";
 fraction = ProperFractionFormat.getParserInstance().parse(input, pos);
 Assert.assertNotNull(fraction);
 Assert.assertEquals(new Fraction(12, 35), fraction);
 Assert.assertEquals(input.length(), pos.getIndex());

 // Test parsing a proper fraction with a fraction part that has leading zeros
 input = "1 0 2 / 3 0 4 0 5";
 fraction = ProperFractionFormat.getParserInstance().parse(input, pos);
 Assert.assertNotNull(fraction);
 Assert.assertEquals(new Fraction(102, 30405), fraction);
 Assert.assertEquals(input.length(), pos.getIndex());

 // Test parsing a proper fraction with a fraction part that has trailing zeros
 input = "1 2 / 3 4 5 0";
 fraction = ProperFractionFormat.getParserInstance().parse(input, pos);
 Assert.assertNotNull(fraction);
 Assert.assertEquals(new Fraction(12, 345), fraction);
 Assert.assertEquals(input.length(), pos.getIndex());
 }

 @Test
 public void testParseWithCustomNumberFormat() {
 NumberFormat wholeFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String input = "1 % / 2 %";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(input, pos);
 Assert.assertNotNull(fraction);
 Assert.assertEquals(new Fraction(1, 2), fraction);
 Assert.assertEquals(-1, pos.getIndex());
 }

 @Test
 public void testSetWholeFormat() {
 NumberFormat wholeFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 properFractionFormat.setWholeFormat(wholeFormat);
 Assert.assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWholeFormatWithNullFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 try {
 properFractionFormat.setWholeFormat(null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 Assert.assertNull(properFractionFormat.getWholeFormat());
 }
}