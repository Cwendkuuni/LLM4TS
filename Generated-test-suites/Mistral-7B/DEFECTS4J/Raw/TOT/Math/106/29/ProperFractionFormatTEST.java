import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ProperFractionFormatTest {

 private static final NumberFormat WHOLE_FORMAT = new DecimalFormat("0");
 private static final NumberFormat NUMERATOR_FORMAT = new DecimalFormat("#");
 private static final NumberFormat DENOMINATOR_FORMAT = new DecimalFormat("#");

 @Test
 public void testProperFractionFormat() {
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 Fraction fraction = new Fraction(3, 2);
 String expected = "3 1/2";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testEmptyWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(3, 2);
 String actual = format.format(fraction, new StringBuffer(), null).toString();
 assertEquals("1/2", actual);
 }

 @Test
 public void testNegativeWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat(new DecimalFormat("-0"));
 Fraction fraction = new Fraction(3, 2);
 String expected = "-3 1/2";
 String actual = format.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testNegativeNumeratorFormat() {
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("-#"), DENOMINATOR_FORMAT);
 Fraction fraction = new Fraction(-3, 2);
 String expected = "-3 1/2";
 String actual = format.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNumeratorFormat() {
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, null, DENOMINATOR_FORMAT);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDenominatorFormat() {
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, null);
 }

 @Test
 public void testParseProperFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "3 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(3, 2);
 Fraction actual = format.parse(input, pos);
 assertEquals(expected, actual);
 pos.setIndex(input.length());
 }

 @Test
 public void testParseWholeNumber() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "-3";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(-3, 1);
 Fraction actual = format.parse(input, pos);
 assertEquals(expected, actual);
 pos.setIndex(input.length());
 }

 @Test
 public void testParseInvalidWholeNumber() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "-3-";
 ParsePosition pos = new ParsePosition(0);
 assertNull(format.parse(input, pos));
 assertEquals(-1, pos.getErrorIndex());
 }

 @Test
 public void testParseInvalidNumerator() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "3 /-1";
 ParsePosition pos = new ParsePosition(0);
 assertNull(format.parse(input, pos));
 assertEquals(-1, pos.getErrorIndex());
 }

 @Test
 public void testParseInvalidDenominator() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "3 1 /-";
 ParsePosition pos = new ParsePosition(0);
 assertNull(format.parse(input, pos));
 assertEquals(-1, pos.getErrorIndex());
 }

 @Test
 public void testParseEmptyInput() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "";
 ParsePosition pos = new ParsePosition(0);
 assertNull(format.parse(input, pos));
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testParseTooManyComponents() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "3 1/2 3/4";
 ParsePosition pos = new ParsePosition(0);
 assertNull(format.parse(input, pos));
 assertEquals(-1, pos.getErrorIndex());
 }
}