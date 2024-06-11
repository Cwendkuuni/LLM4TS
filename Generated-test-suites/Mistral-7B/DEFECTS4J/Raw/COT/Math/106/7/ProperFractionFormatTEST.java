import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.util.MathUtils;
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
 testFormatAndParse("0", new Fraction(0, 1), new ParsePosition(0));
 testFormatAndParse("1", new Fraction(1, 1), new ParsePosition(0));
 testFormatAndParse("1/1", new Fraction(1, 1), new ParsePosition(0));
 testFormatAndParse("2/1", new Fraction(2, 1), new ParsePosition(0));
 testFormatAndParse("1/2", new Fraction(1, 2), new ParsePosition(0));
 }

 @Test
 public void testCustomConstructor() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

 testFormatAndParse("0", new Fraction(0, 1), new ParsePosition(0));
 testFormatAndParse("1", new Fraction(1, 1), new ParsePosition(0));
 testFormatAndParse("1/1", new Fraction(1, 1), new ParsePosition(0));
 testFormatAndParse("2/1", new Fraction(2, 1), new ParsePosition(0));
 testFormatAndParse("1/2", new Fraction(1, 2), new ParsePosition(0));

 testFormatAndParse("3", new Fraction(3, 1), new ParsePosition(0));
 testFormatAndParse("-3", new Fraction(-3, 1), new ParsePosition(0));
 testFormatAndParse("-3 1/2", new Fraction(-7, 2), new ParsePosition(0));
 }

 @Test
 public void testNegativeDenominator() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();

 testFormatAndParse("1/-2", new Fraction(1, -2), new ParsePosition(0));
 testFormatAndParse("-1/-2", new Fraction(-1, 2), new ParsePosition(0));
 }

 @Test(expected = ParseException.class)
 public void testInvalidNegativeWhole() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 testFormatAndParse("-3 -1/2", null, new ParsePosition(0));
 }

 @Test(expected = ParseException.class)
 public void testInvalidNegativeNumerator() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 testFormatAndParse("-3 1/-2", null, new ParsePosition(0));
 }

 @Test(expected = ParseException.class)
 public void testInvalidNegativeBoth() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 testFormatAndParse("-3 -1/-2", null, new ParsePosition(0));
 }

 @Test
 public void testBigDecimal() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

 testFormatAndParse("0.5", new Fraction(new BigDecimal("0.5"), 1), new ParsePosition(0));
 testFormatAndParse("1.5", new Fraction(new BigDecimal("1.5"), 1), new ParsePosition(0));
 testFormatAndParse("1/1.5", new Fraction(1, new BigInteger("3")), new ParsePosition(0));
 testFormatAndParse("2/1.5", new Fraction(2, new BigInteger("3")), new ParsePosition(0));
 testFormatAndParse("1/1.5", new Fraction(1, new BigInteger("3")), new ParsePosition(0));
 }

 @Test
 public void testBigInteger() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

 testFormatAndParse("0.5", new Fraction(new BigDecimal("0.5"), 1), new ParsePosition(0));
 testFormatAndParse("1.5", new Fraction(new BigDecimal("1.5"), 1), new ParsePosition(0));
 testFormatAndParse("1/1.5", new Fraction(1, new BigInteger("3")), new ParsePosition(0));
 testFormatAndParse("2/1.5", new Fraction(2, new BigInteger("3")), new ParsePosition(0));
 testFormatAndParse("1/1.5", new Fraction(1, new BigInteger("3")), new ParsePosition(0));
 testFormatAndParse("BigInteger.TEN.pow(2).toString() 1/2", new Fraction(new BigInteger("100"), new BigInteger("2")), new ParsePosition(0));
 }

 private void testFormatAndParse(String input, Fraction expected, ParsePosition parsePosition) {
 String formatted = properFractionFormat.format(new Fraction(expected.getNumerator(), expected.getDenominator()), new StringBuffer(), null);
 Assert.assertEquals("Format error: " + formatted, input, formatted);
 Fraction actual = properFractionFormat.parse(input, parsePosition);
 Assert.assertEquals("Parse error: " + input, expected, actual);
 }

}