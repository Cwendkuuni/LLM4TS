import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;

 @Test
 public void testDefaultFormat() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 1/2";
 String formatted = format(fraction).toString();
 assertEquals(expected, formatted);
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(3, 4);
 String expected = "3 1/4";
 String formatted = format(fraction).toString();
 assertEquals(expected, formatted);
 }

 @Test
 public void testFormatWithNegativeWhole() {
 Fraction fraction = new Fraction(-3, 2);
 String expected = "-3 1/2";
 String formatted = format(fraction).toString();
 assertEquals(expected, formatted);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFormat() {
 NumberFormat wholeFormat = null;
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, null, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = null;
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(1, 2);
 String input = "1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction parsed = format.parse(input, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 assertEquals(-1, pos.getIndex());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidFormat2() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = null;
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String input = "1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction parsed = format.parse(input, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 assertEquals(-1, pos.getIndex());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidFormat3() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 NumberFormat numeratorFormat = null;
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String input = "1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction parsed = format.parse(input, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 assertEquals(-1, pos.getIndex());
 }

 private StringBuffer format(Fraction fraction) {
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 return toAppendTo;
 }
}