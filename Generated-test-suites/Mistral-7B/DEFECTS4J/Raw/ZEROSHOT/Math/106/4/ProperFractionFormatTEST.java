import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFormat;
 private ProperFractionFormat customFormat;

 @Test
 public void testDefaultFormat() {
 properFormat = new ProperFractionFormat();
 Fraction fraction = new Fraction(1, 2);
 StringBuffer expected = new StringBuffer(16);
 properFormat.format(fraction, expected, new FieldPosition(0, 0));
 String actual = expected.toString();
 assertEquals("-1 / 2", actual);
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
 properFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(1, 2);
 StringBuffer expected = new StringBuffer(16);
 properFormat.format(fraction, expected, new FieldPosition(0, 0));
 String actual = expected.toString();
 assertEquals("-1 / 2", actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFormat() {
 NumberFormat wholeFormat = null;
 ProperFractionFormat properFormat = new ProperFractionFormat(wholeFormat, null, null);
 }

 @Test
 public void testParseProperFraction() {
 String source = "-1 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFormat.parse(source, pos);
 assertEquals(-3, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test(expected = ParseException.class)
 public void testInvalidProperFraction() {
 String source = "-1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFormat.parse(source, pos);
 fail("Expected ParseException but got " + fraction);
 }

 @Test
 public void testParseImproperFraction() {
 String source = "3 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFormat.parse(source, pos);
 assertEquals(7, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test(expected = ParseException.class)
 public void testInvalidImproperFraction() {
 String source = "3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFormat.parse(source, pos);
 fail("Expected ParseException but got " + fraction);
 }

 @Test
 public void testCustomParseProperFraction() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
 denominatorFormat.setMinimumIntegerDigits(2);
 ProperFractionFormat customFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String source = "-01 02/03";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = customFormat.parse(source, pos);
 assertEquals(-1, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 assertEquals(3, fraction.getDenominator().intValue());
 }

 @Test
 public void testFormatCustomProperFraction() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
 denominatorFormat.setMinimumIntegerDigits(2);
 ProperFractionFormat customFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(1, 2);
 StringBuffer expected = new StringBuffer(16);
 customFormat.format(fraction, expected, new FieldPosition(0, 0));
 String actual = expected.toString();
 assertEquals("-01 / 02", actual);
 }
}