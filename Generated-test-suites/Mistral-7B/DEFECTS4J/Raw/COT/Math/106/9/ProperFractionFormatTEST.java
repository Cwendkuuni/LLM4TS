import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 wholeFormat = NumberFormatUtils.createNumberFormat(Locale.US);
 numeratorFormat = NumberFormatUtils.createNumberFormat(Locale.US);
 denominatorFormat = NumberFormatUtils.createNumberFormat(Locale.US);
 properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 }

 @Test
 public void testDefaultConstructor() {
 ProperFractionFormat defaultProperFractionFormat = new ProperFractionFormat();
 Assert.assertEquals(defaultProperFractionFormat.getClass(), ProperFractionFormat.class);
 }

 @Test
 public void testCustomConstructor() {
 NumberFormat customWholeFormat = NumberFormatUtils.createNumberFormat(".0######%", Locale.US);
 NumberFormat customNumeratorFormat = NumberFormatUtils.createNumberFormat("0.000");
 NumberFormat customDenominatorFormat = NumberFormatUtils.createNumberFormat("0.000");
 ProperFractionFormat customProperFractionFormat = new ProperFractionFormat(customWholeFormat, customNumeratorFormat, customDenominatorFormat);
 Assert.assertEquals(customProperFractionFormat.getClass(), ProperFractionFormat.class);
 Assert.assertEquals(customProperFractionFormat.getWholeFormat(), customWholeFormat);
 Assert.assertEquals(customProperFractionFormat.getNumeratorFormat(), customNumeratorFormat);
 Assert.assertEquals(customProperFractionFormat.getDenominatorFormat(), customDenominatorFormat);
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(1, 2);
 StringBuffer stringBuffer = new StringBuffer(256);
 properFractionFormat.format(fraction, stringBuffer, new FieldPosition(0, 0));
 String result = stringBuffer.toString();
 Assert.assertEquals(result, "1 / 2");
 }

 @Test
 public void testFormatWithCustomWholeFormat() {
 Fraction fraction = new Fraction(3, 2);
 StringBuffer stringBuffer = new StringBuffer(256);
 ProperFractionFormat customProperFractionFormat = new ProperFractionFormat(NumberFormatUtils.createNumberFormat("#,##.##"), null, null);
 customProperFractionFormat.format(fraction, stringBuffer, new FieldPosition(0, 0));
 String result = stringBuffer.toString();
 Assert.assertEquals(result, "3.5 / 2");
 }

 @Test
 public void testFormatWithCustomNumeratorFormat() {
 Fraction fraction = new Fraction(1, 3);
 StringBuffer stringBuffer = new StringBuffer(256);
 ProperFractionFormat customProperFractionFormat = new ProperFractionFormat(null, NumberFormatUtils.createNumberFormat("0.#"), null);
 customProperFractionFormat.format(fraction, stringBuffer, new FieldPosition(0, 0));
 String result = stringBuffer.toString();
 Assert.assertEquals(result, "1 / 3");
 }

 @Test
 public void testFormatWithCustomDenominatorFormat() {
 Fraction fraction = new Fraction(1, 4);
 StringBuffer stringBuffer = new StringBuffer(256);
 ProperFractionFormat customProperFractionFormat = new ProperFractionFormat(null, null, NumberFormatUtils.createNumberFormat("0%"));
 customProperFractionFormat.format(fraction, stringBuffer, new FieldPosition(0, 0));
 String result = stringBuffer.toString();
 Assert.assertEquals(result, "1 / 4%");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWholeFormatNull() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 try {
 properFractionFormat.setWholeFormat(null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals(e.getMessage(), "whole format can not be null.");
 }
 }

 @Test
 public void testParse() {
 String input = "3 1/2";
 ParsePosition parsePosition = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(input, parsePosition);
 Assert.assertEquals(fraction.getNumerator(), 3);
 Assert.assertEquals(fraction.getDenominator(), 2);
 Assert.assertEquals(parsePosition.getIndex(), input.length());
 }

 @Test
 public void testParseWithCustomWholeFormat() {
 String input = "-3 1/2";
 ParsePosition parsePosition = new ParsePosition(0);
 ProperFractionFormat customProperFractionFormat = new ProperFractionFormat(NumberFormatUtils.createNumberFormat("#,##"), null, null);
 Fraction fraction = customProperFractionFormat.parse(input, parsePosition);
 Assert.assertEquals(fraction.getNumerator(), -3);
 Assert.assertEquals(fraction.getDenominator(), 2);
 Assert.assertEquals(parsePosition.getIndex(), input.length());
 }

 @Test
 public void testParseWithCustomNumeratorFormat() {
 String input = "1.25 1/2";
 ParsePosition parsePosition = new ParsePosition(0);
 ProperFractionFormat customProperFractionFormat = new ProperFractionFormat(null, NumberFormatUtils.createNumberFormat("0.#"), null);
 Fraction fraction = customProperFractionFormat.parse(input, parsePosition);
 Assert.assertEquals(fraction.getNumerator(), 1);
 Assert.assertEquals(fraction.getDenominator(), 2);
 Assert.assertEquals(parsePosition.getIndex(), input.length());
 }

 @Test
 public void testParseWithCustomDenominatorFormat() {
 String input = "1 1/2%";
 ParsePosition parsePosition = new ParsePosition(0);
 ProperFractionFormat customProperFractionFormat = new ProperFractionFormat(null, null, NumberFormatUtils.createNumberFormat("%"));
 Fraction fraction = customProperFractionFormat.parse(input, parsePosition);
 Assert.assertEquals(fraction.getNumerator(), 1);
 Assert.assertEquals(fraction.getDenominator(), 2);
 Assert.assertEquals(parsePosition.getIndex(), input.length());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormat() {
 String input = "3 -1/2";
 ParsePosition parsePosition = new ParsePosition(0);
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 try {
 Fraction fraction = properFractionFormat.parse(input, parsePosition);
 } catch (ParseException e) {
 Assert.assertEquals(e.getMessage(), "Invalid proper fraction format: 3 -1/2");
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidNumberFormat() {
 String input = "3 a/2";
 ParsePosition parsePosition = new ParsePosition(0);
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 try {
 Fraction fraction = properFractionFormat.parse(input, parsePosition);
 } catch (NumberFormatException e) {
 Assert.assertEquals(e.getMessage(), "For input string: \"3 a/2\"");
 }
 }

 @Test
 public void testParseImproperFraction() {
 String input = "3 1/2";
 ParsePosition parsePosition = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(input, parsePosition);
 Assert.assertEquals(fraction.getNumerator(), 3);
 Assert.assertEquals(fraction.getDenominator(), 2);
 Assert.assertEquals(parsePosition.getIndex(), input.length());
 }

 @Test
 public void testParseImproperFractionWithCustomNumberFormat() {
 String input = "3 1/2";
 ParsePosition parsePosition = new ParsePosition(0);
 ProperFractionFormat customProperFractionFormat = new ProperFractionFormat(null, null, NumberFormatUtils.createNumberFormat("#,##.##"));
 Fraction fraction = customProperFractionFormat.parse(input, parsePosition);
 Assert.assertEquals(fraction.getNumerator(), 3);
 Assert.assertEquals(fraction.getDenominator(), 2);
 Assert.assertEquals(parsePosition.getIndex(), input.length());
 }

 @Test
 public void testParseMixedFraction() {
 String input = "3 1/2";
 ParsePosition parsePosition = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(input, parsePosition);
 BigDecimal expected = new BigDecimal("7.5");
 BigDecimal actual = new BigDecimal(fraction.toString());
 Assert.assertEquals(Precision.round(actual.doubleValue(), 4), Precision.round(expected.doubleValue(), 4));
 Assert.assertEquals(parsePosition.getIndex(), input.length());
 }

 @Test
 public void testParseMixedFractionWithCustomNumberFormat() {
 String input = "3 1/2";
 ParsePosition parsePosition = new ParsePosition(0);
 ProperFractionFormat customProperFractionFormat = new ProperFractionFormat(null, null, NumberFormatUtils.createNumberFormat("#,##.##"));
 Fraction fraction = customProperFractionFormat.parse(input, parsePosition);
 BigDecimal expected = new BigDecimal("7.5");
 BigDecimal actual = new BigDecimal(fraction.toString());
 Assert.assertEquals(Precision.round(actual.doubleValue(), 4), Precision.round(expected.doubleValue(), 4));
 Assert.assertEquals(parsePosition.getIndex(), input.length());
 }
}