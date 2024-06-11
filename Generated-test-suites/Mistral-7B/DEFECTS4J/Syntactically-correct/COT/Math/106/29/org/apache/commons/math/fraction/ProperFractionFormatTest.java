import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat defaultNumberFormat;
 private ProperFractionFormat properFractionFormat;
 private Fraction expectedFraction;

 @Before
 public void setUp() {
 defaultNumberFormat = NumberFormat.getNumberInstance();
 properFractionFormat = new ProperFractionFormat();
 }

 @Test
 public void testDefaultConstructor() {
 String formatted = properFractionFormat.format(new Fraction(1, 2), new StringBuffer(), null);
 assertEquals("1 / 2", formatted.toString());
 }

 @Test
 public void testCustomConstructor() {
 NumberFormat customNumberFormat = NumberFormat.getNumberInstance();
 ProperFractionFormat customFormat = new ProperFractionFormat(customNumberFormat, customNumberFormat, customNumberFormat);
 String formatted = customFormat.format(new Fraction(1, 2), new StringBuffer(), null);
 // Custom format string will determine the output
 }

 @Test
 public void testFormatMethod() {
 Fraction fraction = new Fraction(3, 4);
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);

 ProperFractionFormat format = new ProperFractionFormat();
 String formatted = format.format(fraction, toAppendTo, pos);
 assertEquals("3 / 4", formatted.toString());
 }

 @Test
 public void testGetWholeFormatMethod() {
 NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
 String formatted = wholeFormat.format(new BigInteger("-3"));
 String expected = "-3";
 assertEquals(expected, formatted);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidWholeNumber() {
 String source = "2 3/4";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 fail("Expected ParseException");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidNumerator() {
 String source = "3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 fail("Expected ParseException");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDenominator() {
 String source = "3 1/0";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 fail("Expected ParseException");
 }

 @Test
 public void testParseProperFraction() {
 String source = "3 1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 expectedFraction = new Fraction(3, 2);
 assertEquals(expectedFraction, fraction);
 }

 @Test
 public void testParseImproperFraction() {
 String source = "5";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 expectedFraction = new Fraction(5, 1);
 assertEquals(expectedFraction, fraction);
 }

 @Test
 public void testParseMixedFraction() {
 String source = "7 1/3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 expectedFraction = new Fraction(7, 3);
 assertEquals(expectedFraction, fraction);
 }

 @Test
 public void testSetWholeFormatMethod() {
 NumberFormat customNumberFormat = NumberFormat.getNumberInstance();
 ProperFractionFormat format = new ProperFractionFormat();
 format.setWholeFormat(customNumberFormat);
 String formatted = format.getWholeFormat().format(new BigInteger("-3"));
 String expected = "Custom Format: -3";
 assertTrue(formatted.contains(expected));
 }
}