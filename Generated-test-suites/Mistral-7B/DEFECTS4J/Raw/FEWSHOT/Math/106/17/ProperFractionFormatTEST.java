package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.commons.math.fraction.Fraction;
import org.apache.commons.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

 private NumberFormat defaultNumberFormat;
 private ProperFractionFormat properFractionFormat;
 private ProperFractionFormat properFractionFormatCustom;

 @org.junit.Before
 public void setUp() {
 defaultNumberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 properFractionFormat = new ProperFractionFormat();
 properFractionFormatCustom = new ProperFractionFormat(defaultNumberFormat, defaultNumberFormat, defaultNumberFormat);
 }

 @org.junit.Test
 public void testDefaultFormat() {
 final String input = "-1 1/2";
 Fraction expected = new Fraction(-1, 2);
 String output = properFractionFormat.format(expected, new StringBuffer(), null);
 assertEquals(input, output);

 Fraction parsed = properFractionFormat.parse(input, new ParsePosition(0));
 assertEquals(expected, parsed);
 }

 @org.junit.Test
 public void testCustomFormat() {
 final String input = "-1 1/2";
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 ProperFractionFormat properFractionFormatCustom = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction expected = new Fraction(-1, 2);
 String output = properFractionFormatCustom.format(expected, new StringBuffer(), null);
 assertEquals(input, output);

 Fraction parsed = properFractionFormatCustom.parse(input, new ParsePosition(0));
 assertEquals(expected, parsed);
 }

 @org.junit.Test
 public void testParseWithInvalidInput() {
 final String input = "-1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(input, pos);
 assertNull(parsed);
 assertEquals(input.length(), pos.getIndex());
 }

 @org.junit.Test
 public void testParseWithLeadingMinusSignInWholeNumber() {
 final String input = "-1 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(input, pos);
 assertEquals(new Fraction(-1, 2), parsed);
 assertEquals(input.length(), pos.getIndex());
 }

 @org.junit.Test
 public void testParseWithLeadingMinusSignInNumerator() {
 final String input = "1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(input, pos);
 assertEquals(new Fraction(-1, 2), parsed);
 assertEquals(input.length(), pos.getIndex());
 }

 @org.junit.Test
 public void testFormatWithLeadingZeroInWholeNumber() {
 final String input = "0 1/2";
 Fraction expected = new Fraction(0, 2);
 String output = properFractionFormat.format(expected, new StringBuffer(), null);
 assertEquals(input, output);
 }

 @org.junit.Test
 public void testFormatWithLeadingZeroInNumerator() {
 final String input = "1 0/2";
 Fraction expected = new Fraction(0, 2);
 String output = properFractionFormat.format(expected, new StringBuffer(), null);
 assertEquals(input, output);
 }

 @org.junit.Test
 public void testFormatAndParseWithBigDecimal() {
 final String input = "-1.5 3/4";
 NumberFormat decimalFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 decimalFormat.setMaximumFractionDigits(2);
 ProperFractionFormat properFractionFormatDecimal = new ProperFractionFormat(decimalFormat, decimalFormat, decimalFormat);
 Fraction expected = new Fraction(new BigDecimal("-1.5"), new BigDecimal("3/4"));
 String output = properFractionFormatDecimal.format(expected, new StringBuffer(), null);
 assertEquals(input, output);

 Fraction parsed = properFractionFormatDecimal.parse(input, new ParsePosition(0));
 assertEquals(expected, parsed);
 }

 @org.junit.Test
 public void testFormatAndParseWithLeadingZeroInNumeratorAndDenominator() {
 final String input = "0 0/0";
 Fraction expected = new Fraction(0, 0);
 String output = properFractionFormat.format(expected, new StringBuffer(), null);
 assertEquals(input, output);

 Fraction parsed = properFractionFormat.parse(input, new ParsePosition(0));
 assertEquals(expected, parsed);
 }

 @org.junit.Test
 public void testFormatAndParseWithLargeFraction() {
 final String input = "12345678901234567890 98765432109876543210";
 Fraction expected = new Fraction(12345678901234567890L, 98765432109876543210L);
 String output = properFractionFormat.format(expected, new StringBuffer(), null);
 assertEquals(input, output);

 Fraction parsed = properFractionFormat.parse(input, new ParsePosition(0));
 assertEquals(expected, parsed);
 }

 @org.junit.Test
 public void testFormatAndParseWithMixedSigns() {
 final String input = "-1 1/2 +1 1/2";
 ProperFractionFormat properFractionFormatMixed = new ProperFractionFormat();
 Fraction expected = new Fraction(-1, 2).add(new Fraction(1, 2));
 String output = properFractionFormatMixed.format(expected, new StringBuffer(), null);
 assertEquals(input, output);

 Fraction parsed = properFractionFormatMixed.parse(input, new ParsePosition(0));
 assertEquals(expected, parsed);
 }

 @org.junit.Test
 public void testFormatAndParseWithWhitespace() {
 final String input = " 1 2 / 3 4 ";
 ProperFractionFormat properFractionFormatWhitespace = new ProperFractionFormat();
 Fraction expected = new Fraction(1, 2);
 String output = properFractionFormatWhitespace.format(expected, new StringBuffer(), null);
 assertEquals(input, output);

 Fraction parsed = properFractionFormatWhitespace.parse(input, new ParsePosition(0));
 assertEquals(expected, parsed);
 }

 @org.junit.Test
 public void testFormatAndParseWithMultipleFractions() {
 final String input = "-1 1/2 + 1 1/2 - 1 1/2 + 1 1/2";
 ProperFractionFormat properFractionFormatMultiple = new ProperFractionFormat();
 Fraction expected = new Fraction(-1, 2).add(new Fraction(1, 2)).negate().add(new Fraction(1, 2)).add(new Fraction(1, 2));
 String output = properFractionFormatMultiple.format(expected, new StringBuffer(), null);
 assertEquals(input, output);

 Fraction parsed = properFractionFormatMultiple.parse(input, new ParsePosition(0));
 assertEquals(expected, parsed);
 }

 @org.junit.Test
 public void testCompareFractionsWithDifferentDenominators() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 int comparisonResult = JaroWinklerDistance.distance(properFractionFormat.format(fraction1, new StringBuffer(), null), properFractionFormat.format(fraction2, new StringBuffer(), null)).getSimilarity();
 assertEquals(0.5, comparisonResult, 0.01);
 }

}