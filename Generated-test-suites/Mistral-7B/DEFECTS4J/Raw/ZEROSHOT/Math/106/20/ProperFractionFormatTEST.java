import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private static final ProperFractionFormat FORMAT = new ProperFractionFormat();

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(4, 2);
 String expected = "2 2 / 2";
 StringBuffer result = new StringBuffer();
 FORMAT.format(fraction, result, new FieldPosition(0, 0, 0));
 assertEquals(expected, result.toString());
 }

 @Test
 public void testFormatWithCustomWholeFormat() {
 ProperFractionFormat customFormat = new ProperFractionFormat(NumberFormat.getNumberInstance());
 customFormat.setWholeFormat(NumberFormat.getIntegerInstance());
 Fraction fraction = new Fraction(4, 2);
 String expected = "4 / 2";
 StringBuffer result = new StringBuffer();
 customFormat.format(fraction, result, new FieldPosition(0, 0, 0));
 assertEquals(expected, result.toString());
 }

 @Test
 public void testFormatWithCustomNumeratorFormat() {
 ProperFractionFormat customFormat = new ProperFractionFormat(
 NumberFormat.getNumberInstance(),
 NumberFormat.getNumberInstance(),
 NumberFormat.getIntegerInstance()
 );
 customFormat.setNumeratorFormat(NumberFormat.getNumberInstance());
 Fraction fraction = new Fraction(4, 2);
 String expected = "2 / 4";
 StringBuffer result = new StringBuffer();
 customFormat.format(fraction, result, new FieldPosition(0, 0, 0));
 assertEquals(expected, result.toString());
 }

 @Test
 public void testFormatWithCustomDenominatorFormat() {
 ProperFractionFormat customFormat = new ProperFractionFormat(
 NumberFormat.getNumberInstance(),
 NumberFormat.getIntegerInstance(),
 NumberFormat.getNumberInstance()
 );
 customFormat.setDenominatorFormat(NumberFormat.getNumberInstance());
 Fraction fraction = new Fraction(4, 2);
 String expected = "2 / 3";
 StringBuffer result = new StringBuffer();
 customFormat.format(fraction, result, new FieldPosition(0, 0, 0));
 assertEquals(expected, result.toString());
 }

 @Test
 public void testParse() {
 ProperFractionFormat customFormat = new ProperFractionFormat(
 NumberFormat.getNumberInstance(),
 NumberFormat.getNumberInstance(),
 NumberFormat.getNumberInstance()
 );
 Fraction result = customFormat.parse("2 / 3", new ParsePosition(0));
 assertEquals(new Fraction(2, 3), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullFormat() {
 ProperFractionFormat customFormat = new ProperFractionFormat(null, null, null);
 assertNull(customFormat.getWholeFormat());
 try {
 customFormat.parse("2 / 3", new ParsePosition(0));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}