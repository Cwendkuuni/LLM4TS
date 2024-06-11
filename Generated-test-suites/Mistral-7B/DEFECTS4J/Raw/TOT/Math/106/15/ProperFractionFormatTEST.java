import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private static final NumberFormat DEFAULT_WHOLE_FORMAT = NumberFormat.getNumberInstance();
 private static final NumberFormat DEFAULT_NUMERATOR_FORMAT = NumberFormat.getNumberInstance();
 private static final NumberFormat DEFAULT_DENOMINATOR_FORMAT = NumberFormat.getNumberInstance();
 private static final ProperFractionFormat DEFAULT_FORMAT = new ProperFractionFormat(DEFAULT_NUMERATOR_FORMAT, DEFAULT_DENOMINATOR_FORMAT);

 @Test
 public void testProperFractionFormat_Default() {
 Fraction fraction = new Fraction(1, 2);
 StringBuffer expected = new StringBuffer();
 ProperFractionFormat format = new ProperFractionFormat();
 format.format(fraction, expected, new FieldPosition(0, 0));
 assertEquals("1 / 2", expected.toString());
 }

 @Test
 public void testProperFractionFormat_CustomFormats() {
 NumberFormat customWholeFormat = NumberFormat.getNumberInstance();
 NumberFormat customNumeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat customDenominatorFormat = NumberFormat.getNumberInstance();
 ProperFractionFormat format = new ProperFractionFormat(customWholeFormat, customNumeratorFormat, customDenominatorFormat);
 Fraction fraction = new Fraction(1, 3);
 StringBuffer expected = new StringBuffer();
 format.format(fraction, expected, new FieldPosition(0, 0));
 // Custom formatting logic goes here
 // For testing, just append the Fraction to the StringBuffer
 expected.append(fraction.toString());
 assertEquals(expected.toString(), format.format(fraction, expected, new FieldPosition(0, 0)).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProperFractionFormat_NullWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, DEFAULT_NUMERATOR_FORMAT, DEFAULT_DENOMINATOR_FORMAT);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProperFractionFormat_NegativeWholeNumber() {
 Fraction fraction = new Fraction(-2, 1);
 StringBuffer expected = new StringBuffer();
 ProperFractionFormat format = new ProperFractionFormat();
 format.format(fraction, expected, new FieldPosition(0, 0));
 assertEquals("-2 / 1", expected.toString());
 }

 @Test(expected = ParseException.class)
 public void testParse_InvalidWholeNumber() {
 String source = "InvalidWholeNumber 1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 format.parse(source, pos);
 // The exception should have been thrown before this point
 fail("ParseException should have been thrown.");
 }

 @Test(expected = ParseException.class)
 public void testParse_InvalidFraction() {
 String source = "2 -1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 format.parse(source, pos);
 // The exception should have been thrown before this point
 fail("ParseException should have been thrown.");
 }

 @Test
 public void testParse_ProperFraction() {
 String source = "3 1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction parsed = format.parse(source, pos);
 assertEquals(new Fraction(3, 2), parsed);
 }

 @Test
 public void testParse_ImproperFraction() {
 String source = "5 1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction parsed = format.parse(source, pos);
 assertEquals(new Fraction(7, 2), parsed);
 }
}