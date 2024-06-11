import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat defaultNumberFormat = NumberFormat.getDefault();
 private ProperFractionFormat properFractionFormat;

 @Test
 public void testDefaultProperFractionFormat() {
 properFractionFormat = new ProperFractionFormat();
 testFormatAndParse("0", new Fraction(0));
 testFormatAndParse("-1", new Fraction(-1, 1));
 testFormatAndParse("1", new Fraction(1, 1));
 testFormatAndParse("2", new Fraction(2, 1));
 testFormatAndParse("-2", new Fraction(-2, 1));
 testFormatAndParse("3", new Fraction(3, 1));
 testFormatAndParse("-3", new Fraction(-3, 1));
 testFormatAndParse("4", new Fraction(4, 1));
 testFormatAndParse("-4", new Fraction(-4, 1));
 testFormatAndParse("5", new Fraction(5, 1));
 testFormatAndParse("-5", new Fraction(-5, 1));
 }

 @Test
 public void testCustomWholeFormatProperFractionFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMinimumFractionDigits(1);
 properFractionFormat = new ProperFractionFormat(wholeFormat);
 testFormatAndParse("0", new Fraction(0));
 testFormatAndParse("-1", new Fraction(-1, 1));
 testFormatAndParse("1", new Fraction(1, 1));
 testFormatAndParse("2", new Fraction(2, 1));
 testFormatAndParse("-2", new Fraction(-2, 1));
 testFormatAndParse("3", new Fraction(3, 1));
 testFormatAndParse("-3", new Fraction(-3, 1));
 testFormatAndParse("4", new Fraction(4, 1));
 testFormatAndParse("-4", new Fraction(-4, 1));
 testFormatAndParse("5", new Fraction(5, 1));
 testFormatAndParse("-5", new Fraction(-5, 1));
 testFormatAndParse("0.5", new Fraction(0, 1, 1));
 testFormatAndParse("-0.5", new Fraction(0, 1, 1));
 }

 @Test
 public void testCustomNumeratorFormatProperFractionFormat() {
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 numeratorFormat.setMinimumFractionDigits(1);
 properFractionFormat = new ProperFractionFormat(defaultNumberFormat, numeratorFormat);
 testFormatAndParse("0", new Fraction(0));
 testFormatAndParse("-1", new Fraction(-1, 1));
 testFormatAndParse("1", new Fraction(1, 1));
 testFormatAndParse("2", new Fraction(2, 1));
 testFormatAndParse("-2", new Fraction(-2, 1));
 testFormatAndParse("3", new Fraction(3, 1));
 testFormatAndParse("-3", new Fraction(-3, 1));
 testFormatAndParse("4", new Fraction(4, 1));
 testFormatAndParse("-4", new Fraction(-4, 1));
 testFormatAndParse("5", new Fraction(5, 1));
 testFormatAndParse("-5", new Fraction(-5, 1));
 testFormatAndParse("0.5", new Fraction(0, 1, 1));
 testFormatAndParse("-0.5", new Fraction(0, 1, 1));
 testFormatAndParse("1.5", new Fraction(1, 1, 1));
 testFormatAndParse("-1.5", new Fraction(-1, 1, 1));
 testFormatAndParse("2.5", new Fraction(2, 1, 1));
 testFormatAndParse("-2.5", new Fraction(-2, 1, 1));
 }

 @Test
 public void testCustomDenominatorFormatProperFractionFormat() {
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
 denominatorFormat.setMinimumFractionDigits(1);
 properFractionFormat = new ProperFractionFormat(defaultNumberFormat, defaultNumberFormat, denominatorFormat);
 testFormatAndParse("0", new Fraction(0));
 testFormatAndParse("-1", new Fraction(-1, 1));
 testFormatAndParse("1", new Fraction(1, 1));
 testFormatAndParse("2", new Fraction(2, 1));
 testFormatAndParse("-2", new Fraction(-2, 1));
 testFormatAndParse("3", new Fraction(3, 1));
 testFormatAndParse("-3", new Fraction(-3, 1));
 testFormatAndParse("4", new Fraction(4, 1));
 testFormatAndParse("-4", new Fraction(-4, 1));
 testFormatAndParse("5", new Fraction(5, 1));
 testFormatAndParse("-5", new Fraction(-5, 1));
 testFormatAndParse("0.5", new Fraction(0, 1, 1));
 testFormatAndParse("-0.5", new Fraction(0, 1, 1));
 testFormatAndParse("1.5", new Fraction(1, 1, 1));
 testFormatAndParse("-1.5", new Fraction(-1, 1, 1));
 testFormatAndParse("2.5", new Fraction(2, 1, 1));
 testFormatAndParse("-2.5", new Fraction(-2, 1, 1));
 testFormatAndParse("3/2", new Fraction(1, 2));
 testFormatAndParse("-3/2", new Fraction(-1, 2));
 testFormatAndParse("4/3", new Fraction(2, 3));
 testFormatAndParse("-4/3", new Fraction(-2, 3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFormat() {
 try {
 properFractionFormat = new ProperFractionFormat(null);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFormatPart() {
 try {
 properFractionFormat = new ProperFractionFormat(defaultNumberFormat, null);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 try {
 properFractionFormat = new ProperFractionFormat(null, defaultNumberFormat);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 try {
 properFractionFormat = new ProperFractionFormat(defaultNumberFormat, null, defaultNumberFormat);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 private void testFormatAndParse(String source, Fraction expected) {
 StringBuffer toAppendTo = new StringBuffer(32);
 ProperFractionFormat.FieldPosition pos = new ProperFractionFormat.FieldPosition();
 String formatted = properFractionFormat.format(new Fraction(expected.getNumerator(), expected.getDenominator()), toAppendTo, pos);
 assertEquals(expected.toString(), formatted);
 ParsePosition parsePosition = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(source, parsePosition);
 assertEquals(expected, parsed);
 assertEquals(parsePosition.getIndex(), source.length());
 }

}