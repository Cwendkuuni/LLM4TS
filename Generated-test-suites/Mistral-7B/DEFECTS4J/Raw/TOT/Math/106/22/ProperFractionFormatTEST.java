package org.apache.commons.math.fraction.test;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ProperFractionFormatTest {

 private static final NumberFormat DEFAULT_FORMAT = NumberFormatUtils.createNumberFormat(Locale.ENGLISH);
 private ProperFractionFormat pff;

 @Test
 public void testDefaultFormat() {
 pff = new ProperFractionFormat();
 testFormatAndParse("0", new Fraction(0, 1), null);
 testFormatAndParse("1", new Fraction(1, 1), null);
 testFormatAndParse("1 1/2", new Fraction(2, 1), null);
 testFormatAndParse("-1 1/2", new Fraction(-2, 1), null);
 testFormatAndParse("1 3/4", new Fraction(4, 3), null);
 testFormatAndParse("-1 3/4", new Fraction(-4, 3), null);
 testFormatAndParse("1 1/3", new Fraction(1, 3), null);
 testFormatAndParse("1 1/-3", new Fraction(1, -3), null);
 testFormatAndParse("1/1", new Fraction(1, 1), null);
 testFormatAndParse("1/-1", new Fraction(1, -1), null);
 testFormatAndParse("1/2", new Fraction(1, 2), null);
 testFormatAndParse("1/-2", new Fraction(1, -2), null);
 testFormatAndParse("2/3", new Fraction(2, 3), null);
 testFormatAndParse("2/-3", new Fraction(2, -3), null);
 testFormatAndParse("3/2", new Fraction(3, 2), null);
 testFormatAndParse("3/-2", new Fraction(3, -2), null);
 testFormatAndParse("1/4", new Fraction(1, 4), null);
 testFormatAndParse("1/-4", new Fraction(1, -4), null);
 testFormatAndParse("4/3", new Fraction(4, 3), null);
 testFormatAndParse("4/-3", new Fraction(4, -3), null);
 testFormatAndParse("3/4", new Fraction(3, 4), null);
 testFormatAndParse("3/-4", new Fraction(3, -4), null);
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat("#,##0", Locale.US);
 ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, DEFAULT_FORMAT, DEFAULT_FORMAT);
 testFormatAndParse("-12345678901234567890 1/2", new Fraction(-24673529012345678901234567891, 1), null);
 }

 @Test
 public void testParseException() {
 pff = new ProperFractionFormat();
 testParseException("1 -1/2");
 testParseException("1/-1 -1/2");
 testParseException("-1/-1");
 testParseException("1/-0");
 testParseException("1 /-0");
 testParseException("1 0/0");
 testParseException("1 /0");
 testParseException("1 /-0");
 testParseException("1 1/-0");
 testParseException("1 0/-1");
 testParseException("1 /-1");
 testParseException("1/1/1");
 testParseException("-1/1/1");
 testParseException("1/1/-1");
 testParseException("-1/1/-1");
 testParseException("1/1/0");
 testParseException("1/0/1");
 testParseException("1/0/-1");
 testParseException("1/-0/1");
 testParseException("1/-0/-1");
 testParseException("0/1");
 testParseException("0/-1");
 testParseException("1/1");
 testParseException("-1/1");
 testParseException("1/-1");
 testParseException("-1/-1");
 }

 @Test
 public void testParseNegativeFraction() {
 pff = new ProperFractionFormat();
 testFormatAndParse("-1 1/2", new Fraction(-2, 1), null);
 testFormatAndParse("-1 3/4", new Fraction(-4, 3), null);
 testFormatAndParse("-1 1/3", new Fraction(-1, 3), null);
 testFormatAndParse("-1 1/-3", new Fraction(-1, -3), null);
 testFormatAndParse("-1/1", new Fraction(-1, 1), null);
 testFormatAndParse("-1/-1", new Fraction(-1, -1), null);
 testFormatAndParse("-1/2", new Fraction(-1, 2), null);
 testFormatAndParse("-1/-2", new Fraction(-1, -2), null);
 testFormatAndParse("-2/3", new Fraction(-2, 3), null);
 testFormatAndParse("-2/-3", new Fraction(-2, -3), null);
 testFormatAndParse("-3/2", new Fraction(-3, 2), null);
 testFormatAndParse("-3/-2", new Fraction(-3, -2), null);
 testFormatAndParse("-1/4", new Fraction(-1, 4), null);
 testFormatAndParse("-1/-4", new Fraction(-1, -4), null);
 testFormatAndParse("-4/3", new Fraction(-4, 3), null);
 testFormatAndParse("-4/-3", new Fraction(-4, -3), null);
 testFormatAndParse("-3/4", new Fraction(-3, 4), null);
 testFormatAndParse("-3/-4", new Fraction(-3, -4), null);
 }

 @Test
 public void testParseBigDecimal() {
 pff = new ProperFractionFormat();
 testFormatAndParse("1.5", new Fraction(1, 2), null);
 testFormatAndParse("-1.5", new Fraction(-1, 2), null);
 testFormatAndParse("2.666666666666666666666666666666667", new Fraction(2, 7), null);
 testFormatAndParse("-2.666666666666666666666666666666667", new Fraction(-2, 7), null);
 }

 private void testFormatAndParse(String input, Fraction expected, ParsePosition parsePosition) {
 String result = format(input, pff);
 Fraction resultFraction = pff.parse(input, parsePosition);
 assertEquals("format", expected, resultFraction);
 assertEquals("toString", result, pff.format(expected, new StringBuffer(), null).toString());
 }

 private void testParseException(String input) {
 ParsePosition parsePosition = new ParsePosition(0);
 assertNull("parse", pff.parse(input, parsePosition));
 assertEquals("index", parsePosition.getIndex(), 0);
 assertEquals("errorIndex", parsePosition.getErrorIndex(), 0);
 parsePosition.setIndex(input.length());
 }

 private String format(String input, ProperFractionFormat format) {
 StringBuffer buffer = new StringBuffer();
 format.format(new Fraction(1, 1), buffer, null);
 return buffer.toString();
 }
}