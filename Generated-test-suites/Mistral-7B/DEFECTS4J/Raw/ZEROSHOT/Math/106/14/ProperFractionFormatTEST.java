import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.text.simpledate.DateFormatContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private ProperFractionFormat properFractionFormatCustom;

 @Test
 public void testDefaultConstructor() {
 properFractionFormat = new ProperFractionFormat();
 Fraction fraction = new Fraction(5, 2);
 String expected = "5 / 2";
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testCustomConstructor() {
 NumberFormat wholeFormat = NumberFormat.getInstance();
 NumberFormat numeratorFormat = NumberFormat.getInstance();
 NumberFormat denominatorFormat = NumberFormat.getInstance();
 properFractionFormatCustom = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(5, 2);
 String expected = "5 / 2";
 String actual = properFractionFormatCustom.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormat() {
 // Test formatting a positive fraction
 Fraction fraction = new Fraction(5, 2);
 properFractionFormat = new ProperFractionFormat();
 String expected = "5 / 2";
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 // Test formatting a negative fraction
 fraction = new Fraction(-5, 2);
 expected = "-5 / 2";
 actual = properFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 // Test formatting a fraction with a whole number
 fraction = new Fraction(4, 2);
 expected = "4 / 2";
 actual = properFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 // Test formatting a fraction with a leading minus sign in the whole number part
 fraction = new Fraction(-4, 2);
 expected = "-4 / 2";
 actual = properFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWholeFormatNull() {
 properFractionFormatCustom = new ProperFractionFormat(
 new NumberFormat[0], new NumberFormat[0], new NumberFormat[0]);
 try {
 properFractionFormatCustom.setWholeFormat(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParse() {
 // Test parsing a positive improper fraction
 String source = "5";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFractionFormat.parse(source, pos);
 Fraction expected = new Fraction(5, 1);
 assertEquals(expected, actual);
 // Test parsing a positive proper fraction
 source = "5 1/2";
 actual = properFractionFormat.parse(source, pos);
 expected = new Fraction(5, 2);
 assertEquals(expected, actual);
 // Test parsing a negative proper fraction
 source = "-5 1/2";
 actual = properFractionFormat.parse(source, pos);
 expected = new Fraction(-5, 2);
 assertEquals(expected, actual);
 // Test parsing a negative improper fraction
 source = "-5";
 actual = properFractionFormat.parse(source, pos);
 expected = new Fraction(-5, 1);
 assertEquals(expected, actual);
 // Test parsing an invalid fraction with a negative whole number
 source = "-5 -1/2";
 pos.setIndex(0);
 try {
 properFractionFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 // Test parsing an invalid fraction with a leading minus sign in the denominator
 source = "5 -1/2";
 pos.setIndex(0);
 try {
 properFractionFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseWithCustomFormats() {
 // Test parsing a positive improper fraction with custom whole format
 NumberFormat wholeFormat = new DecimalFormat("#0");
 properFractionFormatCustom = new ProperFractionFormat(
 wholeFormat, new DecimalFormat("#0"), new DecimalFormat("#0"));
 String source = "5";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFractionFormatCustom.parse(source, pos);
 Fraction expected = new Fraction(5, 1);
 assertEquals(expected, actual);
 // Test parsing a positive proper fraction with custom numerator and denominator formats
 DateFormatContext context = DateFormatContext.forPattern("yyyy/MM/dd HH:mm:ss");
 properFractionFormatCustom = new ProperFractionFormat(
 new DecimalFormat("#,###"), new DecimalFormat("#,###"), new DecimalFormat("#,###"));
 source = "5 1/2";
 actual = properFractionFormatCustom.parse(source, pos);
 expected = new Fraction(5, 2);
 assertEquals(expected, actual);
 // Test parsing a negative proper fraction with custom whole format
 source = "-5 1/2";
 actual = properFractionFormatCustom.parse(source, pos);
 expected = new Fraction(-5, 2);
 assertEquals(expected, actual);
 // Test parsing a negative improper fraction with custom denominator format
 source = "-5";
 pos.setIndex(0);
 actual = properFractionFormatCustom.parse(source, pos);
 expected = new Fraction(-5, 1);
 assertEquals(expected, actual);
 }

}