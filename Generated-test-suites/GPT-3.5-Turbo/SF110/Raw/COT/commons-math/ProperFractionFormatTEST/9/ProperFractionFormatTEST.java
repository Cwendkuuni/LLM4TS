import org.junit.Test;
import org.apache.commons.math3.fraction.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

   @Test
   public void testFormat() {
      Fraction fraction = new Fraction(6, 4);
      ProperFractionFormat format = new ProperFractionFormat();
      String expectedResult = "1 1 / 2";
      assertEquals(expectedResult, format.format(fraction));

      fraction = new Fraction(7, 2);
      expectedResult = "3 1 / 2";
      assertEquals(expectedResult, format.format(fraction));

      fraction = new Fraction(0, 5);
      expectedResult = "0 / 5";
      assertEquals(expectedResult, format.format(fraction));
   }

   @Test
   public void testParse() {
      ProperFractionFormat format = new ProperFractionFormat();
      String fractionString = "1 1 / 2";
      Fraction expectedResult = new Fraction(6, 4);
      assertEquals(expectedResult, format.parse(fractionString));

      fractionString = "3 1 / 2";
      expectedResult = new Fraction(7, 2);
      assertEquals(expectedResult, format.parse(fractionString));

      fractionString = "0 / 5";
      expectedResult = new Fraction(0, 5);
      assertEquals(expectedResult, format.parse(fractionString));
   }

   @Test
   public void testParseError() {
      ProperFractionFormat format = new ProperFractionFormat();
      String fractionString = "1.5";
      Fraction expectedResult = null;
      assertEquals(expectedResult, format.parse(fractionString));

      fractionString = "1 / 2 / 3";
      expectedResult = null;
      assertEquals(expectedResult, format.parse(fractionString));

      fractionString = "1 1";
      expectedResult = null;
      assertEquals(expectedResult, format.parse(fractionString));
   }

   @Test(expected = NullArgumentException.class)
   public void testSetWholeFormatNull() {
      ProperFractionFormat format = new ProperFractionFormat();
      format.setWholeFormat(null);
   }
   
}