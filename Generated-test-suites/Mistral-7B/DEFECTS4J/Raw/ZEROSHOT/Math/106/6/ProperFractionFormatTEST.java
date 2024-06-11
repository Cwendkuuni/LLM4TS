import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private static final ProperFractionFormat defaultFormat = new ProperFractionFormat();
 private static final ProperFractionFormat customFormat = new ProperFractionFormat(new DecimalFormat("0.######"));

 @Test
 public void testFormatDefault() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 / 2";
 String actual = defaultFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testFormatCustom() {
 Fraction fraction = new Fraction(1, 3);
 String expected = "0.0013333333333333333333333333333333333333333333333";
 String actual = customFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testFormatWholeNegative() {
 Fraction fraction = new Fraction(-1, 1);
 String expected = "-1 / 1";
 String actual = defaultFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testFormatWholeAndNumeratorNegative() {
 Fraction fraction = new Fraction(-2, -3);
 String expected = "2 / -3";
 String actual = defaultFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNullFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidInput() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Format parseFormat = format.getClass().getFormat(format.getClass().getName());
 Fraction result = (Fraction) parseFormat.parseObject(input, Fraction.class, pos);
 fail("Expected NumberFormatException but got " + result);
 }

 @Test
 public void testParseDefault() {
 Fraction expected = new Fraction(3, 2);
 String input = "3 1/2";
 ParsePosition pos = new ParsePosition(0);
 Format parseFormat = ProperFractionFormat.class.getFormat(ProperFractionFormat.class.getName());
 Fraction result = (Fraction) parseFormat.parseObject(input, Fraction.class, pos);
 assertEquals(expected, result);
 }

 @Test
 public void testParseCustom() {
 Fraction expected = new Fraction(1, 7);
 String input = "1.00001428571428571428571428571428571428571428571428571428";
 ParsePosition pos = new ParsePosition(0);
 Format parseFormat = customFormat.getClass().getFormat(customFormat.getClass().getName());
 Fraction result = (Fraction) parseFormat.parseObject(input, Fraction.class, pos);
 assertEquals(expected, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseWholeNegative() {
 Fraction expected = new Fraction(-3, 1);
 String input = "-3 1/2";
 ParsePosition pos = new ParsePosition(0);
 Format parseFormat = defaultFormat.getClass().getFormat(defaultFormat.getClass().getName());
 Fraction result = (Fraction) parseFormat.parseObject(input, Fraction.class, pos);
 fail("Expected NumberFormatException but got " + result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseMixedNegative() {
 Fraction expected = new Fraction(-3, 2);
 String input = "2 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Format parseFormat = defaultFormat.getClass().getFormat(defaultFormat.getClass().getName());
 Fraction result = (Fraction) parseFormat.parseObject(input, Fraction.class, pos);
 fail("Expected NumberFormatException but got " + result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidFraction() {
 Fraction expected = null;
 String input = "1 -1";
 ParsePosition pos = new ParsePosition(0);
 Format parseFormat = defaultFormat.getClass().getFormat(defaultFormat.getClass().getName());
 Fraction result = (Fraction) parseFormat.parseObject(input, Fraction.class, pos);
 assertNull(result);
 assertTrue(pos.getErrorIndex() > 0);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidFraction2() {
 Fraction expected = null;
 String input = "1 -1/0";
 ParsePosition pos = new ParsePosition(0);
 Format parseFormat = defaultFormat.getClass().getFormat(defaultFormat.getClass().getName());
 Fraction result = (Fraction) parseFormat.parseObject(input, Fraction.class, pos);
 assertNull(result);
 assertTrue(pos.getErrorIndex() > 0);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidFraction3() {
 Fraction expected = null;
 String input = "1 -1/-0";
 ParsePosition pos = new ParsePosition(0);
 Format parseFormat = defaultFormat.getClass().getFormat(defaultFormat.getClass().getName());
 Fraction result = (Fraction) parseFormat.parseObject(input, Fraction.class, pos);
 assertNull(result);
 assertTrue(pos.getErrorIndex() > 0);
 }
}