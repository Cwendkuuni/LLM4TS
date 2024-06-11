import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private static final ProperFractionFormat DEFAULT_FORMAT = new ProperFractionFormat();
 private static final ProperFractionFormat CUSTOM_FORMAT = new ProperFractionFormat(NumberFormat.getNumberInstance());

 @Test
 public void testFormatDefault() {
 Fraction fraction = new Fraction(4, 3);
 String expectedOutput = "4 3/3";
 String actualOutput = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testFormatCustom() {
 Fraction fraction = new Fraction(4, 3);
 NumberFormat customWholeFormat = NumberFormat.getNumberInstance();
 NumberFormat customNumeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat customDenominatorFormat = NumberFormat.getNumberInstance();
 ProperFractionFormat customFormat = new ProperFractionFormat(customWholeFormat, customNumeratorFormat, customDenominatorFormat);
 String expectedOutput = "4 3/3";
 String actualOutput = customFormat.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testFormatWithWhole() {
 Fraction fraction = new Fraction(-5, 2);
 String expectedOutput = "-5 2/1";
 String actualOutput = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWholeFormatNull() {
 ProperFractionFormat format = new ProperFractionFormat();
 try {
 format.setWholeFormat(null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseDefault() {
 String input = "1 2/3";
 Fraction expectedOutput = new Fraction(1, 2);
 Fraction actualOutput = DEFAULT_FORMAT.parse(input, new ParsePosition(0));
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testParseCustom() {
 String input = "1 2/3";
 Fraction expectedOutput = new Fraction(1, 2);
 Fraction actualOutput = CUSTOM_FORMAT.parse(input, new ParsePosition(0));
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormat() {
 String input = "-3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat.parse(input, pos);
 fail("ParseException expected");
 }

 @Test
 public void testParseNegativeSignInWhole() {
 String input = "-3 1/2";
 Fraction expectedOutput = new Fraction(-7, 2);
 Fraction actualOutput = DEFAULT_FORMAT.parse(input, new ParsePosition(0));
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testParseNegativeSignInNumerator() {
 String input = "3 -1/2";
 Fraction expectedOutput = new Fraction(3, -2);
 Fraction actualOutput = DEFAULT_FORMAT.parse(input, new ParsePosition(0));
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testParseMisaligned() {
 String input = "3\n1/2";
 Fraction expectedOutput = new Fraction(3, 2);
 Fraction actualOutput = DEFAULT_FORMAT.parse(input, new ParsePosition(0));
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testParseWhitespace() {
 String input = "3 \n1 / 2";
 Fraction expectedOutput = new Fraction(3, 2);
 Fraction actualOutput = DEFAULT_FORMAT.parse(input, new ParsePosition(0));
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testParseMultipleWhitespaces() {
 String input = "3   \n1   / 2";
 Fraction expectedOutput = new Fraction(3, 2);
 Fraction actualOutput = DEFAULT_FORMAT.parse(input, new ParsePosition(0));
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testParseMultipleNonWhitespaceCharacters() {
 String input = "3a1/b2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = ProperFractionFormat.parse(input, pos);
 assertTrue(pos.getIndex() >= input.length());
 assertNull(fraction);
 }

 @Test
 public void testParseLeadingZero() {
 String input = "0 1/2";
 Fraction expectedOutput = new Fraction(0, 1);
 Fraction actualOutput = DEFAULT_FORMAT.parse(input, new ParsePosition(0));
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testParseLeadingZeroInWhole() {
 String input = "0 -3 1/2";
 Fraction expectedOutput = new Fraction(-3, 1);
 Fraction actualOutput = DEFAULT_FORMAT.parse(input, new ParsePosition(0));
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testFormatAndParseRoundtrip() {
 Fraction fraction = new Fraction(10, 3);
 String expectedOutput = "3 3/1";
 String actualOutput = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null).toString();
 Fraction roundtripFraction = DEFAULT_FORMAT.parse(actualOutput, new ParsePosition(0));
 assertEquals(fraction, roundtripFraction);
 }

 @Test
 public void testFormatAndParseRoundtripWithCustomFormat() {
 Fraction fraction = new Fraction(10, 3);
 NumberFormat customWholeFormat = NumberFormat.getNumberInstance();
 NumberFormat customNumeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat customDenominatorFormat = NumberFormat.getNumberInstance();
 ProperFractionFormat customFormat = new ProperFractionFormat(customWholeFormat, customNumeratorFormat, customDenominatorFormat);
 String expectedOutput = "3 3/1";
 String actualOutput = customFormat.format(fraction, new StringBuffer(), null).toString();
 Fraction roundtripFraction = customFormat.parse(actualOutput, new ParsePosition(0));
 assertEquals(fraction, roundtripFraction);
 }

 @Test
 public void testLevenshteinDistance() {
 String input = "3 1/2";
 String output = "3 1 1/2";
 int expectedDistance = 2;
 int actualDistance = new LevenshteinDistance().apply(input, output);
 assertEquals(expectedDistance, actualDistance);
 }

 @Test
 public void testFormatWithLeadingMinusSign() {
 Fraction fraction = new Fraction(-3, 1);
 String expectedOutput = "-3 1/1";
 String actualOutput = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expectedOutput, actualOutput);
 }
}