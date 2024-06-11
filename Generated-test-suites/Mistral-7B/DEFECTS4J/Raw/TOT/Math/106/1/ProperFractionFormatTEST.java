import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFormat;
 private ProperFractionFormat customFormat;

 @Test
 public void testDefaultFormat() {
 properFormat = new ProperFractionFormat();

 // Test formatting
 Fraction testFraction = new Fraction(1, 2);
 String expected = "1 / 2";
 String actual = properFormat.format(testFraction, new StringBuffer(), null);
 assertEquals(expected, actual);

 // Test parsing
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = properFormat.parse(expected, pos);
 assertEquals(testFraction, parsedFraction);
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMinimumIntegerDigits(2);
 wholeFormat.setGroupingUsed(false);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();

 customFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

 // Test formatting
 testFraction = new Fraction(-3, 2);
 expected = "-03 / 02";
 actual = customFormat.format(testFraction, new StringBuffer(), null);
 assertEquals(expected, actual);

 // Test parsing
 String testString = expected;
 pos = new ParsePosition(0);
 parsedFraction = customFormat.parse(testString, pos);
 assertEquals(testFraction, parsedFraction);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidWholeFormat() {
 NumberFormat invalidFormat = null;
 ProperFractionFormat invalidFormatObject = new ProperFractionFormat(invalidFormat, new NumberFormat(""), new NumberFormat(""));
 }

 @Test(expected = ParseException.class)
 public void testInvalidString() {
 String invalidString = "3 -1/2";
 properFormat.parse(invalidString, new ParsePosition(0));
 }

 @Test(expected = ParseException.class)
 public void testInvalidDenominator() {
 String invalidDenominator = "1/0";
 customFormat.parse(invalidDenominator, new ParsePosition(0));
 }

 @Test
 public void testFormatWholeNegative() {
 properFormat = new ProperFractionFormat();
 testFraction = new Fraction(-3, 1);
 expected = "-3";
 actual = properFormat.format(testFraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWholePositive() {
 properFormat = new ProperFractionFormat();
 testFraction = new Fraction(3, 1);
 expected = "3";
 actual = properFormat.format(testFraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNumerator() {
 properFormat = new ProperFractionFormat();
 testFraction = new Fraction(1, 3);
 expected = "1 / 3";
 actual = properFormat.format(testFraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDenominator() {
 properFormat = new ProperFractionFormat();
 testFraction = new Fraction(2, 5);
 expected = "2 / 5";
 actual = properFormat.format(testFraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatMixed() {
 properFormat = new ProperFractionFormat();
 testFraction = new Fraction(-5, 7);
 expected = "-5 / 7";
 actual = properFormat.format(testFraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWholeNegative() {
 String testString = "-3";
 pos = new ParsePosition(0);
 parsedFraction = properFormat.parse(testString, pos);
 assertEquals(new Fraction(-3, 1), parsedFraction);
 }

 @Test
 public void testParseWholePositive() {
 String testString = "3";
 pos = new ParsePosition(0);
 parsedFraction = properFormat.parse(testString, pos);
 assertEquals(new Fraction(3, 1), parsedFraction);
 }

 @Test
 public void testParseNumerator() {
 String testString = "1 / 3";
 pos = new ParsePosition(0);
 parsedFraction = properFormat.parse(testString, pos);
 assertEquals(new Fraction(1, 3), parsedFraction);
 }

 @Test
 public void testParseDenominator() {
 String testString = "2 / 5";
 pos = new ParsePosition(0);
 parsedFraction = properFormat.parse(testString, pos);
 assertEquals(new Fraction(2, 5), parsedFraction);
 }

 @Test
 public void testParseMixed() {
 String testString = "-5 / 7";
 pos = new ParsePosition(0);
 parsedFraction = properFormat.parse(testString, pos);
 assertEquals(new Fraction(-5, 7), parsedFraction);
 }
}