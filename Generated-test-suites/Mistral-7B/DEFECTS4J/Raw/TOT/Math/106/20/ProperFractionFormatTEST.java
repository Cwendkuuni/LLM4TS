import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private static final NumberFormat DEFAULT_NUMBER_FORMAT = NumberFormat.getNumberInstance();
 private static final ProperFractionFormat DEFAULT_FORMAT = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT, DEFAULT_NUMBER_FORMAT, DEFAULT_NUMBER_FORMAT);

 @Test
 public void testFormatDefault() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 / 2";
 String actual = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatCustom() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMinimumFractionDigits(1);
 ProperFractionFormat customFormat = new ProperFractionFormat(wholeFormat, DEFAULT_NUMBER_FORMAT, DEFAULT_NUMBER_FORMAT);
 Fraction fraction = new Fraction(1, 3);
 String expected = "1 1/3";
 String actual = customFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseDefault() {
 String input = "1/2";
 Fraction expected = new Fraction(1, 2);
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = DEFAULT_FORMAT.parse(input, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseCustomWholeNegative() {
 String input = "-1/2";
 Fraction expected = new Fraction(-1, 2);
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = DEFAULT_FORMAT.parse(input, pos);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidFormat() {
 String input = "3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 try {
 Fraction actual = format.parse(input, pos);
 fail();
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNullFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 try {
 format.setWholeFormat(null);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}