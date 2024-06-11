import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.translate.Translator;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ParsePosition;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;

 @Test
 public void testDefaultFormat() {
 format = new ProperFractionFormat();
 testFormat(format, new Fraction(1, 2), "1 1/2");
 testFormat(format, new Fraction(-1, 2), "-1 1/2");
 testFormat(format, new Fraction(0, 1), "0 1");
 testFormat(format, new Fraction(0, -1), "0 -1");
 testFormat(format, new Fraction(3, 2), "3 1/2");
 testFormat(format, new Fraction(-3, 2), "-3 1/2");
 testFormat(format, new Fraction(3, -2), "3 -1/2");
 testFormat(format, new Fraction(-3, -2), "-3 -1/2");
 testFormat(format, new Fraction(Integer.MIN_VALUE, 1), "" + Integer.MIN_VALUE + " 1");
 testFormat(format, new Fraction(1, Integer.MIN_VALUE), "1 " + Integer.MIN_VALUE);
 testFormat(format, new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE), "" + Integer.MIN_VALUE + " " + Integer.MIN_VALUE);
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 testFormat(format, new Fraction(1, 2), "1 % 1/2");
 testFormat(format, new Fraction(-1, 2), "-1 % 1/2");
 testFormat(format, new Fraction(0, 1), "0 % 1");
 testFormat(format, new Fraction(0, -1), "0 % -1");
 testFormat(format, new Fraction(3, 2), "3 % 1/2");
 testFormat(format, new Fraction(-3, 2), "-3 % 1/2");
 testFormat(format, new Fraction(3, -2), "3 % -1/2");
 testFormat(format, new Fraction(-3, -2), "-3 % -1/2");
 testFormat(format, new Fraction(Integer.MIN_VALUE, 1), "" + Integer.MIN_VALUE + " % 1");
 testFormat(format, new Fraction(1, Integer.MIN_VALUE), "1 % " + Integer.MIN_VALUE);
 testFormat(format, new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE), "" + Integer.MIN_VALUE + " % " + Integer.MIN_VALUE);
 }

 @Test
 public void testParse() {
 format = new ProperFractionFormat();
 testParse(format, "1 1/2", new Fraction(1, 2));
 testParse(format, "-1 1/2", new Fraction(-1, 2));
 testParse(format, "0 1", new Fraction(0, 1));
 testParse(format, "0 -1", new Fraction(0, -1));
 testParse(format, "3 1/2", new Fraction(3, 2));
 testParse(format, "-3 1/2", new Fraction(-3, 2));
 testParse(format, "3 -1/2", new Fraction(3, -2));
 testParse(format, "-3 -1/2", new Fraction(-3, -2));
 testParse(format, "" + Integer.MIN_VALUE + " 1", new Fraction(Integer.MIN_VALUE, 1));
 testParse(format, "1 " + Integer.MIN_VALUE, new Fraction(1, Integer.MIN_VALUE));
 testParse(format, "" + Integer.MIN_VALUE + " " + Integer.MIN_VALUE, new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE));
 }

 @Test
 public void testParseInvalid() {
 format = new ProperFractionFormat();
 testParseInvalid(format, "-1 -1/2");
 testParseInvalid(format, "1 1/2 3");
 testParseInvalid(format, "1/0 1/2");
 testParseInvalid(format, "1/1/2");
 testParseInvalid(format, "-1/-1/2");
 testParseInvalid(format, "1 1/-2");
 testParseInvalid(format, "-1 1/-2");
 testParseInvalid(format, "0 0");
 testParseInvalid(format, "0 0 1");
 testParseInvalid(format, "1 1/0");
 testParseInvalid(format, "1/0 1");
 testParseInvalid(format, "1/1/0");
 testParseInvalid(format, "1/0/0");
 testParseInvalid(format, "1 -1/2");
 testParseInvalid(format, "1/-1/2");
 testParseInvalid(format, "1/1/2 1");
 testParseInvalid(format, "1/1/2 0");
 testParseInvalid(format, "1/1/2 1 1");
 testParseInvalid(format, "1/1/2 1 0");
 testParseInvalid(format, "1/1/2 1 1 1");
 }

 @Test
 public void testFormatAndParseRoundtrip() {
 format = new ProperFractionFormat();
 Fraction fraction = new Fraction(new BigInteger("12345678901234567890"), new BigInteger("98765432109876543210"));
 String formatted = format.format(fraction, new StringBuilder(), null);
 Fraction parsed = format.parse(formatted, new ParsePosition(0));
 Assert.assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseRoundtripCustom() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(new BigDecimal("12345678901234567890"), new BigDecimal("98765432109876543210"));
 String formatted = format.format(fraction, new StringBuilder(), null);
 Fraction parsed = format.parse(formatted, new ParsePosition(0));
 Assert.assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseRoundtripBigDecimal() {
 format = new ProperFractionFormat();
 BigDecimal value = new BigDecimal("12345678901234567890.12345678901234567890");
 Fraction fraction = new Fraction(value);
 String formatted = format.format(fraction, new StringBuilder(), null);
 Fraction parsed = format.parse(formatted, new ParsePosition(0));
 Assert.assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseRoundtripBigInteger() {
 format = new ProperFractionFormat();
 BigInteger numerator = new BigInteger("12345678901234567890");
 BigInteger denominator = new BigInteger("98765432109876543210");
 Fraction fraction = new Fraction(numerator, denominator);
 String formatted = format.format(fraction, new StringBuilder(), null);
 Fraction parsed = format.parse(formatted, new ParsePosition(0));
 Assert.assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseRoundtripBigDecimalCustom() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 BigDecimal value = new BigDecimal("12345678901234567890.12345678901234567890");
 Fraction fraction = new Fraction(value);
 String formatted = format.format(fraction, new StringBuilder(), null);
 Fraction parsed = format.parse(formatted, new ParsePosition(0));
 Assert.assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseRoundtripBigIntegerCustom() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 BigInteger numerator = new BigInteger("12345678901234567890");
 BigInteger denominator = new BigInteger("98765432109876543210");
 Fraction fraction = new Fraction(numerator, denominator);
 String formatted = format.format(fraction, new StringBuilder(), null);
 Fraction parsed = format.parse(formatted, new ParsePosition(0));
 Assert.assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatStringComparison() {
 format = new ProperFractionFormat();
 Fraction f1 = new Fraction(1, 2);
 Fraction f2 = new Fraction(3, 4);
 String s1 = format.format(f1, new StringBuilder(), null).toString();
 String s2 = format.format(f2, new StringBuilder(), null).toString();
 Assert.assertFalse(new JaroWinklerDistance().apply(s1, s2) > 0.85d);
 }

 @Test
 public void testFormatTranslatedStringComparison() {
 Translator translator = Translator.getDefault();
 format = new ProperFractionFormat();
 Locale es_ES = new Locale("es", "ES");
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(es_ES);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(es_ES);
 NumberFormat denominatorFormat = NumberFormat.getPercentInstance(es_ES);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction f1 = new Fraction(1, 2);
 Fraction f2 = new Fraction(3, 4);
 String s1 = format.format(f1, new StringBuilder(), null).toString();
 String s2 = format.format(f2, new StringBuilder(), null).toString();
 String translatedS1 = translator.translate(s1).get(es_ES).toString();
 String translatedS2 = translator.translate(s2).get(es_ES).toString();
 Assert.assertFalse(new JaroWinklerDistance().apply(translatedS1, translatedS2) > 0.85d);
 }

 private void testFormat(ProperFractionFormat format, Fraction fraction, String expected) {
 StringBuilder toAppendTo = new StringBuilder();
 FormatUtils.appendFormat(format, fraction, toAppendTo, new FieldPosition(0, 0));
 Assert.assertEquals(expected, toAppendTo.toString());
 }

 private void testParse(ProperFractionFormat format, String input, Fraction expected) {
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = format.parse(input, pos);
 Assert.assertEquals(expected, parsed);
 Assert.assertEquals(input.length(), pos.getIndex());
 }

 private void testParseInvalid(ProperFractionFormat format, String input) {
 Assert.assertNull(format.parse(input, new ParsePosition(0)));
 }
}