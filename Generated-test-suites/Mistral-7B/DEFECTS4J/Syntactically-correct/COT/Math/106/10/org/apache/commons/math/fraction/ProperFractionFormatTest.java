import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;

 @Test
 public void testDefaultConstructor() {
 properFractionFormat = new ProperFractionFormat();
 testFormatAndParse("0", new Fraction(0, 1), null);
 testFormatAndParse("1", new Fraction(1, 1), null);
 testFormatAndParse("1/1", new Fraction(1, 1), null);
 testFormatAndParse("2/1", new Fraction(2, 1), null);
 testFormatAndParse("-1/1", new Fraction(-1, 1), null);
 testFormatAndParse("1/2", new Fraction(1, 2), null);
 testFormatAndParse("1/-2", new Fraction(1, -2), null);
 testFormatAndParse("1/3", new Fraction(1, 3), null);
 testFormatAndParse("1/-3", new Fraction(1, -3), null);
 }

 @Test
 public void testCustomConstructor() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 wholeFormat.setMinimumFractionDigits(2);
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat);
 testFormatAndParse("0.00", new Fraction(0, 1), null);
 testFormatAndParse("1.00", new Fraction(1, 1), null);
 testFormatAndParse("1/1.00", new Fraction(1, 1), null);
 testFormatAndParse("2/1.00", new Fraction(2, 1), null);
 testFormatAndParse("-1/1.00", new Fraction(-1, 1), null);
 testFormatAndParse("1/2.00", new Fraction(1, 2), null);
 testFormatAndParse("1/-2.00", new Fraction(1, -2), null);
 testFormatAndParse("1/3.00", new Fraction(1, 3), null);
 testFormatAndParse("1/-3.00", new Fraction(1, -3), null);
 }

 @Test
 public void testCustomConstructorsWithNullFormat() {
 Assert.assertThrows("whole format can not be null.", IllegalArgumentException.class, () -> new ProperFractionFormat(null));
 Assert.assertThrows("numerator format can not be null.", IllegalArgumentException.class, () -> new ProperFractionFormat(null, null, null));
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(BigInteger.valueOf(1234567890), BigInteger.valueOf(234567890));
 properFractionFormat = new ProperFractionFormat();
 String formatted = properFractionFormat.format(fraction, new StringBuffer(), null);
 testFormatAndParse(formatted, fraction, new ParsePosition(0));
 }

 @Test
 public void testFormatWithCustomWholeFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 wholeFormat.setMinimumFractionDigits(2);
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat);
 Fraction fraction = new Fraction(BigInteger.valueOf(1234567890), BigInteger.valueOf(234567890));
 String formatted = properFractionFormat.format(fraction, new StringBuffer(), null);
 testFormatAndParse(formatted, fraction, new ParsePosition(0));
 }

 @Test
 public void testParse() {
 testParse("0", new Fraction(0, 1), null);
 testParse("1", new Fraction(1, 1), null);
 testParse("1/1", new Fraction(1, 1), null);
 testParse("2/1", new Fraction(2, 1), null);
 testParse("-1/1", new Fraction(-1, 1), null);
 testParse("1/2", new Fraction(1, 2), null);
 testParse("1/-2", new Fraction(1, -2), null);
 testParse("1/3", new Fraction(1, 3), null);
 testParse("1/-3", new Fraction(1, -3), null);
 testParse("3/2", new Fraction(3, 2), null);
 testParse("-3/2", new Fraction(-3, 2), null);
 testParse("1.5", new Fraction(1, 2), null);
 testParse("-1.5", new Fraction(-1, 2), null);
 testParse("1/1.5", new Fraction(1, 2), null);
 testParse("-1/1.5", new Fraction(-1, 2), null);
 testParse("1/3.5", new Fraction(1, 3), null);
 testParse("-1/3.5", new Fraction(-1, 3), null);
 testParse("2/1.5", new Fraction(2, 3), null);
 testParse("-2/1.5", new Fraction(-2, 3), null);
 }

 @Test
 public void testParseWithInvalidInput() {
 testParse("", null, new ParsePosition(0));
 testParse("1/0", null, new ParsePosition(0));
 testParse("1/-0", null, new ParsePosition(0));
 testParse("1/-", null, new ParsePosition(0));
 testParse("-1/-", null, new ParsePosition(0));
 testParse("1/-/2", null, new ParsePosition(0));
 testParse("1/-1/2", null, new ParsePosition(0));
 testParse("1/2/", null, new ParsePosition(0));
 testParse("1/2/1/", null, new ParsePosition(0));
 testParse("1/2/1/1/", null, new ParsePosition(0));
 }

 private void testFormatAndParse(String input, Fraction expected, ParsePosition pos) {
 String formatted = properFractionFormat.format(expected, new StringBuffer(), null).toString();
 Fraction actual = properFractionFormat.parse(formatted, pos);
 Assert.assertEquals("Input: " + input, expected, actual);
 }

 private void testParse(String input, Fraction expected, ParsePosition pos) {
 String testInput = input;
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 Fraction actual = properFractionFormat.parse(testInput, pos);
 if (expected == null) {
 Assert.assertNull("Input: " + testInput, actual);
 } else {
 Assert.assertEquals("Input: " + testInput, expected, actual);
 }
 }

 private void parseAndIgnoreWhitespace(String source, ParsePosition pos) {
 int index = pos.getIndex();
 while (index < source.length() && Character.isWhitespace(source.charAt(index))) {
 index++;
 pos.setIndex(index);
 }
 }

 private char parseNextCharacter(String source, ParsePosition pos) {
 char result = source.charAt(pos.getIndex());
 int newIndex = pos.getIndex() + 1;
 pos.setIndex(newIndex);
 return result;
 }
}