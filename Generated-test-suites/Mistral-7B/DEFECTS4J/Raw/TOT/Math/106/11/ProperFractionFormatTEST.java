import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private FractionFormat properFractionFormat;

 @Test
 public void testDefaultFormat() {
 FractionFormat format = new ProperFractionFormat();
 String input = "1 2/3";
 Fraction expected = new Fraction(1, 2);
 Fraction output = format.parse(input, new ParsePosition(0));
 assertEquals(expected, output);

 StringBuffer buffer = new StringBuffer(10);
 format.format(expected, buffer, new FieldPosition(0, 0));
 String outputString = buffer.toString();
 assertEquals("1 2/3", outputString);
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMinimumIntegerDigits(2);
 wholeFormat.setGroupingUsed(false);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getPercentInstance();

 FractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String input = " 2 3/4";
 Fraction expected = new Fraction(2, 4);
 Fraction output = format.parse(input, new ParsePosition(0));
 assertEquals(expected, output);

 StringBuffer buffer = new StringBuffer(10);
 format.format(expected, buffer, new FieldPosition(0, 0));
 String outputString = buffer.toString();
 assertEquals(" 2 3/4", outputString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFormat() {
 try {
 NumberFormat wholeFormat = null;
 FractionFormat format = new ProperFractionFormat(wholeFormat, null, null);
 fail("IllegalArgumentException expected but not thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeWhole() {
 FractionFormat format = new ProperFractionFormat();
 String input = "-1 2/3";
 try {
 Fraction output = format.parse(input, new ParsePosition(0));
 fail("IllegalArgumentException expected but not thrown.");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testInvalidFormat() {
 FractionFormat format = new ProperFractionFormat();
 String input = "-1 -1/2";
 try {
 Fraction output = format.parse(input, new ParsePosition(0));
 fail("ParseException expected but not thrown.");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidNumberFormat() {
 FractionFormat format = new ProperFractionFormat();
 String input = "invalid";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction output = format.parse(input, pos);
 fail("NumberFormatException expected but not thrown.");
 } catch (ParseException e) {
 // expected
 }
 assertNotNull(e.getErrorOffset());
 assertEquals(0, e.getErrorOffset().getIndex());
 }

}