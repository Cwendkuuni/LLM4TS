import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

 private static final ProperFractionFormat DEFAULT_FORMAT = new ProperFractionFormat();
 private static final ProperFractionFormat CUSTOM_FORMAT = new ProperFractionFormat(getNumberFormat("0.##"));

 @Test
 public void testConstructorDefault() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction result = format.parse("1 1/2", new ParsePosition(0));
 Assert.assertEquals(new Fraction(1, 2), result);
 }

 @Test
 public void testConstructorCustom() {
 ProperFractionFormat format = new ProperFractionFormat(getCusttomNumberFormat());
 Fraction result = format.parse("1.5", new ParsePosition(0));
 Assert.assertEquals(new Fraction(3, 2), result);
 }

 @Test
 public void testConstructorCustomWholeNumeratorDenominator() {
 ProperFractionFormat format = new ProperFractionFormat(getNumberFormat("0"), getNumberFormat("0.##"), getNumberFormat("0.##"));
 Fraction result = format.parse("1 -1/3", new ParsePosition(0));
 Assert.assertEquals(new Fraction(-1, 3), result);
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(3, 2);
 String expected = "1 1/2";
 ProperFractionFormat format = new ProperFractionFormat();
 String result = format.format(fraction, new StringBuffer(), null).toString();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testParse() {
 Fraction expected = new Fraction(-1, 3);
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse("1 -1/3", pos);
 Assert.assertEquals(expected, result);
 Assert.assertEquals(3, pos.getErrorIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWholeFormatNull() {
 ProperFractionFormat format = new ProperFractionFormat();
 try {
 format.setWholeFormat(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseImproperFraction() {
 Fraction expected = new Fraction(3, 2);
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse("1.5", pos);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = ParsePosition.ParseException.class)
 public void testParseInvalidWholeNumber() {
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 format.parse("abc 1/2", pos);
 Assert.fail("Expected ParsePosition.ParseException but got: " + pos.getErrorMessage());
 }

 @Test(expected = ParsePosition.ParseException.class)
 public void testParseInvalidNumerator() {
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 format.parse("1 a/2", pos);
 Assert.fail("Expected ParsePosition.ParseException but got: " + pos.getErrorMessage());
 }

 @Test(expected = ParsePosition.ParseException.class)
 public void testParseInvalidDenominator() {
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 format.parse("1 1 a", pos);
 Assert.fail("Expected ParsePosition.ParseException but got: " + pos.getErrorMessage());
 }

 @Test(expected = ParsePosition.ParseException.class)
 public void testParseInvalidWholeAndNumerator() {
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 format.parse("abc 1 a", pos);
 Assert.fail("Expected ParsePosition.ParseException but got: " + pos.getErrorMessage());
 }

 @Test(expected = ParsePosition.ParseException.class)
 public void testParseInvalidWholeAndDenominator() {
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 format.parse("abc -1", pos);
 Assert.fail("Expected ParsePosition.ParseException but got: " + pos.getErrorMessage());
 }

 @Test(expected = ParsePosition.ParseException.class)
 public void testParseInvalidNumeratorAndDenominator() {
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 format.parse("1 a b", pos);
 Assert.fail("Expected ParsePosition.ParseException but got: " + pos.getErrorMessage());
 }

 @Test(expected = ParsePosition.ParseException.class)
 public void testParseInvalidFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 format.parse("1 1 a", pos);
 Assert.fail("Expected ParsePosition.ParseException but got: " + pos.getErrorMessage());
 }

 @Test
 public void testParseNegativeWholeNumber() {
 Fraction expected = new Fraction(-1, 2);
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse("-1 1/2", pos);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testParseNegativeNumerator() {
 Fraction expected = new Fraction(1, -2);
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse("1 -1/2", pos);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testParseNegativeDenominator() {
 Fraction expected = new Fraction(1, 2);
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse("1 1 -2", pos);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testParseNegativeWholeNumeratorAndDenominator() {
 Fraction expected = new Fraction(-1, -2);
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse("-1 -1/2", pos);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testParseLargeFraction() {
 Fraction expected = new Fraction(BigInteger.valueOf(1000000000000000001L), BigInteger.valueOf(100000000000000011L));
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse(expected.toString(), pos);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testParseLargeNegativeFraction() {
 Fraction expected = new Fraction(BigInteger.valueOf(-1000000000000000001L), BigInteger.valueOf(100000000000000011L));
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse(expected.toString(), pos);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testParseLargeFractionWithCustomFormat() {
 BigDecimal wholeFormat = new BigDecimal("0");
 BigDecimal numeratorFormat = new BigDecimal("0.##");
 BigDecimal denominatorFormat = new BigDecimal("0.##");
 ProperFractionFormat format = new ProperFractionFormat(getNumberFormat(wholeFormat.toPlainString()),
 getNumberFormat(numeratorFormat.toPlainString()),
 getNumberFormat(denominatorFormat.toPlainString()));
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse(expected.toString(), pos);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testFormatWithCustomFormat() {
 Fraction fraction = new Fraction(BigInteger.valueOf(1000000000000000001L), BigInteger.valueOf(100000000000000011L));
 ProperFractionFormat format = new ProperFractionFormat(getNumberFormat("0"), getNumberFormat("0.##"), getNumberFormat("0.##"));
 String expected = "1000000000000000001 100000000000000011";
 String result = format.format(fraction, new StringBuffer(), null).toString();
 Assert.assertEquals(expected, result);
 }

 private NumberFormat getNumberFormat(String pattern) {
 NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);
 format.setPattern(pattern);
 return format;
 }

 private NumberFormat getCusttomNumberFormat() {
 NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);
 format.setMinimumFractionDigits(3);
 return format;
 }
}