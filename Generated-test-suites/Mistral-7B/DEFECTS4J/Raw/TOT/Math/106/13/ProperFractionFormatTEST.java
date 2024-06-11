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
 Fraction fraction = new Fraction(1, 2);
 ProperFractionFormat defaultFormat = new ProperFractionFormat();
 String formatted = defaultFormat.format(fraction, new StringBuffer(), null);
 assertEquals("1/2", formatted.toString());
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMinimumFractalsDigits(1);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
 ProperFractionFormat customFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(3, 4);
 String formatted = customFormat.format(fraction, new StringBuffer(), null);
 assertEquals("-3/4", formatted.toString());
 }

 @Test
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-1, 2);
 ProperFractionFormat defaultFormat = new ProperFractionFormat();
 String formatted = defaultFormat.format(fraction, new StringBuffer(), null);
 assertEquals("-1/2", formatted.toString());
 }

 @Test
 public void testImproperFormat() {
 Fraction fraction = new Fraction(5, 2);
 ProperFractionFormat defaultFormat = new ProperFractionFormat();
 try {
 String formatted = defaultFormat.format(fraction, new StringBuffer(), null);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseWholeNumber() {
 ProperFractionFormat defaultFormat = new ProperFractionFormat();
 String source = "-3";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = defaultFormat.parse(source, pos);
 assertEquals(-3, fraction.getNumerator());
 assertEquals(1, fraction.getDenominator());
 }

 @Test
 public void testParseNumerator() {
 ProperFractionFormat defaultFormat = new ProperFractionFormat();
 String source = "1 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = defaultFormat.parse(source, pos);
 assertEquals(1, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testParseDenominator() {
 ProperFractionFormat defaultFormat = new ProperFractionFormat();
 String source = "1 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = defaultFormat.parse(source, pos);
 assertEquals(1, fraction.getNumerator());
 assertEquals(3, fraction.getDenominator());
 }

 @Test
 public void testParseNegativeWholeNumber() {
 ProperFractionFormat defaultFormat = new ProperFractionFormat();
 String source = "-3 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = defaultFormat.parse(source, pos);
 assertEquals(-7, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testParseInvalidFormat() {
 ProperFractionFormat defaultFormat = new ProperFractionFormat();
 String source = "-3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction fraction = defaultFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseImproperFormat() {
 ProperFractionFormat defaultFormat = new ProperFractionFormat();
 String source = "5/2";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction fraction = defaultFormat.parse(source, pos);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
}

}