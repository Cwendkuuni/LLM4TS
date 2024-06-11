import org.junit.Assert;
import org.junit.Test;

public class ProperFractionFormatTest {

   @Test
   public void testFormat() {
      // Test 1
      ProperFractionFormat format = new ProperFractionFormat();
      Fraction fraction = new Fraction(2, 3);
      String expected = "2 / 3";
      String actual = format.format(fraction, new StringBuffer(), null).toString();
      Assert.assertEquals(expected, actual);

      // Test 2
      fraction = new Fraction(-5, 2);
      expected = "-2 1/2";
      actual = format.format(fraction, new StringBuffer(), null).toString();
      Assert.assertEquals(expected, actual);

      // Test 3
      NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
      NumberFormat numeratorFormat = NumberFormat.getIntegerInstance();
      NumberFormat denominatorFormat = NumberFormat.getIntegerInstance();
      format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
      fraction = new Fraction(3, 4);
      expected = "0 3 / 4";
      actual = format.format(fraction, new StringBuffer(), null).toString();
      Assert.assertEquals(expected, actual);
   }

   @Test
   public void testParse() {
      // Test 1
      ProperFractionFormat format = new ProperFractionFormat();
      String source = "2 / 3";
      ParsePosition pos = new ParsePosition(0);
      Fraction expected = new Fraction(2, 3);
      Fraction actual = format.parse(source, pos);
      Assert.assertEquals(expected, actual);

      // Test 2
      source = "-2 1/2";
      pos = new ParsePosition(0);
      expected = new Fraction(-5, 2);
      actual = format.parse(source, pos);
      Assert.assertEquals(expected, actual);

      // Test 3
      NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
      NumberFormat numeratorFormat = NumberFormat.getIntegerInstance();
      NumberFormat denominatorFormat = NumberFormat.getIntegerInstance();
      format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
      source = "0 3 / 4";
      pos = new ParsePosition(0);
      expected = new Fraction(3, 4);
      actual = format.parse(source, pos);
      Assert.assertEquals(expected, actual);
   }

   @Test
   public void testGetWholeFormat() {
      ProperFractionFormat format = new ProperFractionFormat();
      NumberFormat expected = format.getWholeFormat();
      NumberFormat actual = NumberFormat.getInstance();
      Assert.assertEquals(expected, actual);
   }

   @Test
   public void testSetWholeFormat() {
      ProperFractionFormat format = new ProperFractionFormat();
      NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
      format.setWholeFormat(wholeFormat);
      NumberFormat expected = wholeFormat;
      NumberFormat actual = format.getWholeFormat();
      Assert.assertEquals(expected, actual);
   }
}