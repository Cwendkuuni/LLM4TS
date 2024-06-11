import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.similarity.JaroDistance;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;

 @Test
 public void testDefaultConstructor() {
 format = new ProperFractionFormat();
 testFormat(format, new Fraction(1, 2), "1 1/2");
 }

 @Test
 public void testCustomConstructor() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 wholeFormat.setMinimumFractionDigits(1);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);

 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 testFormat(format, new Fraction(1, 3), "1 1/3");
 }

 @Test
 public void testFormat() {
 // Test formatting positive fractions
 testFormat(format, new Fraction(1, 2), "1 1/2");
 testFormat(format, new Fraction(1, 3), "1 1/3");
 testFormat(format, new Fraction(1, 4), "1 1/4");
 testFormat(format, new Fraction(1, 5), "1 1/5");
 testFormat(format, new Fraction(1, 6), "1 1/6");
 testFormat(format, new Fraction(1, 7), "1 1/7");
 testFormat(format, new Fraction(1, 8), "1 1/8");
 testFormat(format, new Fraction(1, 9), "1 1/9");

 // Test formatting negative whole numbers
 testFormat(format, new Fraction(-1, 1), "-1 1/1");
 testFormat(format, new Fraction(-2, 1), "-2 1/1");
 testFormat(format, new Fraction(-3, 1), "-3 1/1");

 // Test formatting zero
 testFormat(format, new Fraction(0, 1), "0 1/1");
 }

 @Test
 public void testParse() {
 // Test parsing positive fractions
 testParse(format, "1 1/2", new Fraction(1, 2));
 testParse(format, "1 1/3", new Fraction(1, 3));
 testParse(format, "1 1/4", new Fraction(1, 4));
 testParse(format, "1 1/5", new Fraction(1, 5));
 testParse(format, "1 1/6", new Fraction(1, 6));
 testParse(format, "1 1/7", new Fraction(1, 7));
 testParse(format, "1 1/8", new Fraction(1, 8));
 testParse(format, "1 1/9", new Fraction(1, 9));

 // Test parsing negative whole numbers
 testParse(format, "-1 1/1", new Fraction(-1, 1));
 testParse(format, "-2 1/1", new Fraction(-2, 1));
 testParse(format, "-3 1/1", new Fraction(-3, 1));

 // Test parsing zero
 testParse(format, "0 1/1", new Fraction(0, 1));
 }

 @Test
 public void testParseInvalidWholeNumber() {
 String source = "-3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 Assert.assertNull(fraction);
 Assert.assertEquals(5, pos.getErrorIndex());
 }

 @Test
 public void testParseInvalidNumerator() {
 String source = "-3 0/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 Assert.assertNull(fraction);
 Assert.assertEquals(3, pos.getErrorIndex());
 }

 @Test
 public void testParseInvalidDenominator() {
 String source = "-3 1/0";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 Assert.assertNull(fraction);
 Assert.assertEquals(5, pos.getErrorIndex());
 }

 @Test
 public void testParseInvalidFraction() {
 String source = "-3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 Assert.assertNull(fraction);
 Assert.assertEquals(0, pos.getIndex());
 }

 private void testFormat(ProperFractionFormat format, Fraction fraction, String expectedOutput) {
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, new FieldPosition(0, 0));
 Assert.assertEquals(expectedOutput, toAppendTo.toString());
 }

 private void testParse(ProperFractionFormat format, String source, Fraction expectedFraction) {
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 Assert.assertNotNull(fraction);
 JaroDistance jaroDistance = new JaroDistance();
 Assert.assertTrue(jaroDistance.similarity(expectedFraction.toString(), fraction.toString()) > 0.95);
 }
}