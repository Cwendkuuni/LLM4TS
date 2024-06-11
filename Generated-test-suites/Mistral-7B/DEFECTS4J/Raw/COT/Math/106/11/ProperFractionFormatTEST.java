package org.apache.commons.math.fraction.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberTheoryUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @org.junit.Before
 public void setUp() {
 format = new ProperFractionFormat();
 wholeFormat = NumberFormat.getNumberInstance();
 numeratorFormat = NumberFormat.getNumberInstance();
 denominatorFormat = NumberFormat.getNumberInstance();
 }

 @org.junit.Test
 public void testDefaultConstructor() {
 format = new ProperFractionFormat();
 }

 @org.junit.Test
 public void testCustomConstructorWithDefaultFormats() {
 format = new ProperFractionFormat();
 }

 @org.junit.Test
 public void testCustomConstructorWithCustomFormats() {
 NumberFormat customWholeFormat = NumberFormat.getNumberInstance();
 NumberFormat customNumeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat customDenominatorFormat = NumberFormat.getNumberInstance();
 format = new ProperFractionFormat(customWholeFormat, customNumeratorFormat, customDenominatorFormat);
 }

 @org.junit.Test
 public void testFormatFraction() {
 Fraction fraction = new Fraction(1, 2);
 StringBuffer expectedOutput = new StringBuffer();
 format.format(fraction, expectedOutput, null);
 String actualOutput = expectedOutput.toString();
 String expected = "1 / 2";
 assertEquals(expected, actualOutput);
}

 @org.junit.Test
 public void testFormatNegativeWholeNumber() {
 Fraction fraction = new Fraction(-1, 2);
 StringBuffer expectedOutput = new StringBuffer();
 format.format(fraction, expectedOutput, null);
 String actualOutput = expectedOutput.toString();
 String expected = "-1 / 2";
 assertEquals(expected, actualOutput);
}

 @org.junit.Test
 public void testFormatLargeFraction() {
 Fraction fraction = new Fraction(BigInteger.valueOf(1234567890), BigInteger.valueOf(987654321));
 StringBuffer expectedOutput = new StringBuffer();
 format.format(fraction, expectedOutput, null);
 String actualOutput = expectedOutput.toString();
 String expected = new BigDecimal(1234567890).toPlainString() + " / " + new BigDecimal(987654321).toPlainString();
 assertEquals(expected, actualOutput);
}

 @org.junit.Test
 public void testParseFractionWithNoWholeNumber() {
 String input = "1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(1, 2), fraction);
}

 @org.junit.Test
 public void testParseFractionWithWholeNumber() {
 String input = "1 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(1, 2), fraction);
}

 @org.junit.Test
 public void testParseFractionWithNegativeWholeNumber() {
 String input = "-1 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(-1, 2), fraction);
}

 @org.junit.Test
 public void testParseFractionWithLargeNumerator() {
 String input = "1234567890 1/987654321";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(BigInteger.valueOf(1234567890), BigInteger.valueOf(987654321)), fraction);
}

 @org.junit.Test
 public void testParseFractionWithNegativeLargeNumerator() {
 String input = "-1234567890 1/987654321";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(BigInteger.valueOf(NumberTheoryUtils.negate(1234567890)), BigInteger.valueOf(987654321)), fraction);
}

 @org.junit.Test
 public void testParseFractionWithNegativeWholeNumberAndLargeNumerator() {
 String input = "-1 1234567890 / 987654321";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(BigInteger.valueOf(NumberTheoryUtils.negate(1234567890)), BigInteger.valueOf(987654321)), fraction);
}

 @org.junit.Test
 public void testParseFractionWithNegativeWholeNumberNegativeNumerator() {
 String input = "-1 -1 / 1";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertNull(fraction);
}

 @org.junit.Test
 public void testParseFractionWithWhitespace() {
 String input = " 1 / 2 ";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(1, 2), fraction);
}

 @org.junit.Test
 public void testParseFractionWithNonNumericalCharacters() {
 String input = "1 / a";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertNull(fraction);
}

 @org.junit.Test
 public void testParseFractionWithInvalidFormat() {
 String input = "1 -1 / 1";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertNull(fraction);
 pos.setIndex(input.length());
 assertEquals(-1, pos.getErrorIndex());
}

 @org.junit.Test
 public void testSetWholeFormat() {
 NumberFormat customWholeFormat = NumberFormat.getNumberInstance();
 format.setWholeFormat(customWholeFormat);
 Number number = customWholeFormat.parse("12345");
 assertEquals(number, format.getWholeFormat().parse("12345"));
}

}