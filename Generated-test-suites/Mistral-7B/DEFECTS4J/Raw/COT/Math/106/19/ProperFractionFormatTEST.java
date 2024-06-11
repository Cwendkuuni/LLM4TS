package org.apache.commons.math.fraction.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat defaultNumberFormat;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 defaultNumberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 properFractionFormat = new ProperFractionFormat(defaultNumberFormat, defaultNumberFormat, defaultNumberFormat);
 }

 @Test
 public void testDefaultConstructor() {
 ProperFractionFormat format = new ProperFractionFormat();
 assertEquals(defaultNumberFormat, format.getWholeFormat());
 assertEquals(defaultNumberFormat, format.getNumeratorFormat());
 assertEquals(defaultNumberFormat, format.getDenominatorFormat());
 }

 @Test
 public void testCustomConstructor() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 wholeFormat.setMinimumFractionDigits(2);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, defaultNumberFormat, defaultNumberFormat);
 assertEquals(wholeFormat, format.getWholeFormat());
 assertEquals(defaultNumberFormat, format.getNumeratorFormat());
 assertEquals(defaultNumberFormat, format.getDenominatorFormat());
 }

 @Test
 public void testFormatFraction() {
 Fraction fraction = new Fraction(1, 2);
 StringBuffer expected = new StringBuffer();
 getNumberFormat("1.0").format(1, expected, null);
 expected.append(" ");
 getNumberFormat("0.0").format(2, expected, null);
 getNumberFormat("0.0").format(1, expected, null);
 getNumberFormat("0.1").format(2, expected, null);
 getNumberFormat("0.1").format(1, expected, null);
 getNumberFormat("0.0").format(BigDecimal.valueOf(2).divide(BigDecimal.valueOf(3)).toBigInteger().intValue(), expected, null);
 assertEquals(expected.toString(), properFractionFormat.format(fraction, new StringBuffer(), null).toString());
 }

 @Test
 public void testFormatNegativeWhole() {
 Fraction fraction = new Fraction(-1, 2);
 StringBuffer expected = new StringBuffer();
 getNumberFormat("-1.0").format(-1, expected, null);
 expected.append(" ");
 getNumberFormat("0.0").format(2, expected, null);
 getNumberFormat("0.0").format(-1, expected, null);
 getNumberFormat("0.1").format(2, expected, null);
 getNumberFormat("0.1").format(-1, expected, null);
 getNumberFormat("0.0").format(BigDecimal.valueOf(-1).multiply(BigDecimal.valueOf(2)).add(BigDecimal.valueOf(1)).toBigInteger().intValue(), expected, null);
 assertEquals(expected.toString(), properFractionFormat.format(fraction, new StringBuffer(), null).toString());
 }

 @Test
 public void testFormatImproperFraction() {
 Fraction fraction = new Fraction(3, 2);
 String expected = "1.5";
 assertEquals(expected, properFractionFormat.format(fraction, new StringBuffer(), null).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNullWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, defaultNumberFormat, defaultNumberFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNullNumeratorFormat() {
 ProperFractionFormat format = new ProperFractionFormat(defaultNumberFormat, null, defaultNumberFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNullDenominatorFormat() {
 ProperFractionFormat format = new ProperFractionFormat(defaultNumberFormat, defaultNumberFormat, null);
 }

 @Test
 public void testParseProperFraction() {
 String properFraction = "1 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = properFractionFormat.parse(properFraction, pos);
 assertEquals(new Fraction(1, 3), result);
 assertEquals(properFraction.length(), pos.getIndex());
 }

 @Test
 public void testParseImproperFraction() {
 String improperFraction = "1.5";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = properFractionFormat.parse(improperFraction, pos);
 assertEquals(new Fraction(3, 2), result);
 assertEquals(improperFraction.length(), pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidWhole() {
 String invalidWhole = "x";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(invalidWhole, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidNumerator() {
 String invalidNumerator = "3 foo/2";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(invalidNumerator, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDenominator() {
 String invalidDenominator = "1 2 foo";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(invalidDenominator, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidWholeAndNumerator() {
 String invalidWholeAndNumerator = "x 2";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(invalidWholeAndNumerator, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidWholeAndDenominator() {
 String invalidWholeAndDenominator = "1 x";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(invalidWholeAndDenominator, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidNumeratorAndDenominator() {
 String invalidNumeratorAndDenominator = "3 foo 2";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(invalidNumeratorAndDenominator, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidNegativeWhole() {
 String invalidNegativeWhole = "-1 2/3";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(invalidNegativeWhole, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidNegativeWholeAndNumerator() {
 String invalidNegativeWholeAndNumerator = "-1 2 foo";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(invalidNegativeWholeAndNumerator, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidNegativeWholeAndDenominator() {
 String invalidNegativeWholeAndDenominator = "-1 2 x";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(invalidNegativeWholeAndDenominator, pos);
 }

 private NumberFormat getNumberFormat(String pattern) {
 NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);
 format.applyPattern(pattern);
 return format;
 }

}