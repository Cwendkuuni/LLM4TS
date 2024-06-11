import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat intFormat;
 private NumberFormat intFormatNegative;

 @Test
 public void testDefaultFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(1, 2);
 StringBuffer result = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);

 format.format(fraction, result, pos);
 assertEquals("-1 / 2", result.toString());

 fraction = new Fraction(-1, 2);
 format.format(fraction, result, pos);
 assertEquals("1 / 2", result.toString());
 }

 @Test
 public void testCustomFormat() {
 intFormat = new DecimalFormat("#");
 intFormatNegative = new DecimalFormat("#");
 intFormatNegative.setNegativePrefix(" -");

 ProperFractionFormat format = new ProperFractionFormat(intFormat, intFormat, intFormatNegative);
 Fraction fraction = new Fraction(1, 2);
 StringBuffer result = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);

 format.format(fraction, result, pos);
 assertEquals("1 / 2", result.toString());

 fraction = new Fraction(-1, 2);
 format.format(fraction, result, pos);
 assertEquals(" -1 / 2", result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, null, null);
 }

 @Test
 public void testFormat() {
 ProperFractionFormat format = new ProperFractionFormat();

 // test with whole number
 Fraction fraction = new Fraction(4, 1);
 StringBuffer result = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);

 format.format(fraction, result, pos);
 assertEquals("4", result.toString());

 // test with fraction
 fraction = new Fraction(1, 2);
 format.format(fraction, result, pos);
 assertEquals("1 / 2", result.toString());

 // test with negative whole number
 fraction = new Fraction(-4, 1);
 format.format(fraction, result, pos);
 assertEquals("-4", result.toString());
 }

 @Test
 public void testParse() {
 ProperFractionFormat format = new ProperFractionFormat();

 // test with positive proper fraction
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse("1 / 2", pos);
 assertEquals(new Fraction(1, 2), fraction);
 assertEquals(5, pos.getIndex());

 // test with negative proper fraction
 fraction = format.parse(" -1 / 2", pos);
 assertEquals(new Fraction(-1, 2), fraction);
 assertEquals(6, pos.getIndex());

 // test with improper fraction
 fraction = format.parse("3", pos);
 assertEquals(new Fraction(3, 1), fraction);
 assertEquals(3, pos.getIndex());

 // test with mixed number
 fraction = format.parse("2 1 / 2", pos);
 assertEquals(new Fraction(5, 2), fraction);
 assertEquals(7, pos.getIndex());

 // test with invalid input
 fraction = format.parse("2 -1 / 2", pos);
 assertNull(fraction);
 assertEquals(1, pos.getIndex());
 }

 @Test
 public void testParseNegativeWholeNumber() {
 ProperFractionFormat format = new ProperFractionFormat();

 // test with negative whole number
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse("-3", pos);
 assertEquals(new Fraction(-3, 1), fraction);
 assertEquals(2, pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseNegativeFraction() {
 ProperFractionFormat format = new ProperFractionFormat();

 // test with negative fraction
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse("-1 / 2", pos);
 assertNull(fraction);
 assertEquals(1, pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFraction() {
 ProperFractionFormat format = new ProperFractionFormat();

 // test with invalid fraction
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse("2 -1 / 2", pos);
 assertNull(fraction);
 assertEquals(1, pos.getErrorIndex());
 }

 @Test
 public void testSetWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 NumberFormat formatToSet = new DecimalFormat("#");

 format.setWholeFormat(formatToSet);
 assertEquals(formatToSet, format.getWholeFormat());
 }
}