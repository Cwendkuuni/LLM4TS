package org.apache.commons.math.fraction.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class ProperFractionFormatTest {

 private FractionFormat properFormat;
 private FractionFormat improperFormat;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
 properFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 improperFormat = new FractionFormat(numeratorFormat, denominatorFormat);
 }

 @Test
 public void testDefaultProperFractionFormat() {
 // Test default constructor
 Fraction fraction = new Fraction(1, 2);
 StringBuffer sb = new StringBuffer();
 properFormat.format(fraction, sb, null);
 String formatted = sb.toString();
 assertEquals("1 / 2", formatted);

 Fraction parsed = properFormat.parse(formatted, new ParsePosition(0));
 assertEquals(fraction, parsed);
 }

 @Test
 public void testCustomProperFractionFormat() {
 // Test custom constructor
 NumberFormat wholeFormatCustom = NumberFormat.getNumberInstance();
 NumberFormat numeratorFormatCustom = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormatCustom = NumberFormat.getNumberInstance();
 wholeFormatCustom.setMinimumFractionDigits(2);
 ProperFractionFormat customFormat = new ProperFractionFormat(wholeFormatCustom,
 numeratorFormatCustom, denominatorFormatCustom);

 Fraction fraction = new Fraction(BigInteger.valueOf(12345), BigInteger.valueOf(6789));
 StringBuffer sb = new StringBuffer();
 customFormat.format(fraction, sb, null);
 String formatted = sb.toString();
 assertEquals("-12345.00 / 6789", formatted);

 Fraction parsed = customFormat.parse(formatted, new ParsePosition(0));
 assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatProperFraction() {
 // Test format method
 Fraction fraction = new Fraction(BigInteger.valueOf(123), BigInteger.valueOf(45));
 StringBuffer sb = new StringBuffer();
 properFormat.format(fraction, sb, null);
 String formatted = sb.toString();
 assertEquals("123 / 45", formatted);

 Fraction parsed = properFormat.parse(formatted, new ParsePosition(0));
 assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatImproperFraction() {
 // Test format method with improper fraction
 Fraction fraction = new Fraction(BigInteger.valueOf(1234), BigInteger.valueOf(5));
 StringBuffer sb = new StringBuffer();
 improperFormat.format(fraction, sb, null);
 String formatted = sb.toString();
 assertEquals("246.88", formatted);

 Fraction parsed = improperFormat.parse(formatted, new ParsePosition(0));
 assertEquals(fraction, parsed);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWholeFormat() {
 try {
 ProperFractionFormat format = new ProperFractionFormat(null, numeratorFormat,
 denominatorFormat);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNumeratorFormat() {
 try {
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, null,
 denominatorFormat);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDenominatorFormat() {
 try {
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat,
 numeratorFormat, null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidWholeFormat() {
 try {
 NumberFormat wholeFormatInvalid = new DecimalFormat("#,##.###%");
 ProperFractionFormat format = new ProperFractionFormat(
 wholeFormatInvalid, numeratorFormat, denominatorFormat);
 fail("NumberFormatException expected");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidNumeratorFormat() {
 try {
 NumberFormat numeratorFormatInvalid = new DecimalFormat("##.###%");
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat,
 numeratorFormatInvalid, denominatorFormat);
 fail("NumberFormatException expected");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidDenominatorFormat() {
 try {
 NumberFormat denominatorFormatInvalid = new DecimalFormat("#,##.###%");
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat,
 numeratorFormat, denominatorFormatInvalid);
 fail("NumberFormatException expected");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test(expected = ParseException.class)
 public void testInvalidProperFractionParse() {
 String source = "3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction parsed = properFormat.parse(source, pos);
 fail("ParseException expected");
 } catch (ParseException e) {
 // Expected
 }
 assertFalse(parsed instanceof Fraction);
 assertEquals(-1, pos.getIndex());
 assertEquals(3, pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testInvalidImproperFractionParse() {
 String source = "3 2/2";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction parsed = properFormat.parse(source, pos);
 fail("ParseException expected");
 } catch (ParseException e) {
 // Expected
 }
 assertFalse(parsed instanceof Fraction);
 assertEquals(-1, pos.getIndex());
 assertEquals(3, pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testInvalidWholePartFormatParse() {
 String source = "3e";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction parsed = properFormat.parse(source, pos);
 fail("ParseException expected");
 } catch (ParseException e) {
 // Expected
 }
 assertFalse(parsed instanceof Fraction);
 assertEquals(-1, pos.getIndex());
 assertEquals(3, pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testInvalidNumeratorPartParse() {
 String source = "3 1e";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction parsed = properFormat.parse(source, pos);
 fail("ParseException expected");
 } catch (ParseException e) {
 // Expected
 }
 assertFalse(parsed instanceof Fraction);
 assertEquals(-1, pos.getIndex());
 assertEquals(3, pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testInvalidDenominatorPartParse() {
 String source = "3 1 0";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction parsed = properFormat.parse(source, pos);
 fail("ParseException expected");
 } catch (ParseException e) {
 // Expected
 }
 assertFalse(parsed instanceof Fraction);
 assertEquals(-1, pos.getIndex());
 assertEquals(3, pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testInvalidMixedFractionParse() {
 String source = "3 1 1/2";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction parsed = properFormat.parse(source, pos);
 fail("ParseException expected");
 } catch (ParseException e) {
 // Expected
 }
 assertFalse(parsed instanceof Fraction);
 assertEquals(-1, pos.getIndex());
 assertEquals(3, pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testInvalidNegativeWholePartParse() {
 String source = "-3 1/2";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction parsed = properFormat.parse(source, pos);
 fail("ParseException expected");
 } catch (ParseException e) {
 // Expected
 }
 assertFalse(parsed instanceof Fraction);
 assertEquals(-1, pos.getIndex());
 assertEquals(3, pos.getErrorIndex());
 }

 @Test
 public void testParseFractionWithLeadingZero() {
 String source = "0 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFormat.parse(source, pos);
 assertTrue(parsed instanceof Fraction);
 assertEquals(new Fraction(0, 1), parsed);
 assertEquals(5, pos.getIndex());
 }

}