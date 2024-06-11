import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.fraction.*;

public class ProperFractionFormatTest {

   @Test
   public void testFormat_WithPositiveFraction_ShouldReturnProperFormat() {
      ProperFractionFormat format = new ProperFractionFormat();
      Fraction fraction = new Fraction(3, 4);
      String expected = "3 / 4";
    
      String result = format.format(fraction);
    
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testFormat_WithNegativeFractionInWholeNumber_ShouldReturnProperFormat() {
      ProperFractionFormat format = new ProperFractionFormat();
      Fraction fraction = new Fraction(-5, 3);
      String expected = "-1 2 / 3";
    
      String result = format.format(fraction);
    
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testFormat_WithNegativeFractionInNumerator_ShouldReturnProperFormat() {
      ProperFractionFormat format = new ProperFractionFormat();
      Fraction fraction = new Fraction(2, -5);
      String expected = "-2 / 5";
    
      String result = format.format(fraction);
    
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testFormat_WithNegativeFractionInDenominator_ShouldReturnProperFormat() {
      ProperFractionFormat format = new ProperFractionFormat();
      Fraction fraction = new Fraction(3, -4);
      String expected = "-3 / 4";
    
      String result = format.format(fraction);
    
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testFormat_WithNegativeFractionInWholeAndNumerator_ShouldReturnProperFormat() {
      ProperFractionFormat format = new ProperFractionFormat();
      Fraction fraction = new Fraction(-9, -7);
      String expected = "1 2 / 7";
    
      String result = format.format(fraction);
    
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testFormat_WithZeroFraction_ShouldReturnProperFormat() {
      ProperFractionFormat format = new ProperFractionFormat();
      Fraction fraction = new Fraction(0, 3);
      String expected = "0";
    
      String result = format.format(fraction);
    
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testParse_WithValidProperFraction_ShouldReturnFraction() {
      ProperFractionFormat format = new ProperFractionFormat();
      String input = "2 / 5";
      Fraction expected = new Fraction(2, 5);
    
      Fraction result = format.parse(input);
    
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testParse_WithValidWholeNumberFraction_ShouldReturnFraction() {
      ProperFractionFormat format = new ProperFractionFormat();
      String input = "3";
      Fraction expected = new Fraction(3);
    
      Fraction result = format.parse(input);
    
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testParse_WithNegativeValidProperFraction_ShouldReturnFraction() {
      ProperFractionFormat format = new ProperFractionFormat();
      String input = "-2 / 5";
      Fraction expected = new Fraction(-2, 5);
    
      Fraction result = format.parse(input);
    
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testParse_WithNegativeValidWholeNumberFraction_ShouldReturnFraction() {
      ProperFractionFormat format = new ProperFractionFormat();
      String input = "-3";
      Fraction expected = new Fraction(-3);
    
      Fraction result = format.parse(input);
    
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testParse_WithInvalidFormat_ShouldReturnNull() {
      ProperFractionFormat format = new ProperFractionFormat();
      String input = "2 3 / 5";
    
      Fraction result = format.parse(input);
    
      Assert.assertNull(result);
   }

   @Test
   public void testParse_WithInvalidLeadingMinusSign_ShouldReturnNull() {
      ProperFractionFormat format = new ProperFractionFormat();
      String input = "- 2 / 5";
    
      Fraction result = format.parse(input);
    
      Assert.assertNull(result);
   }

   @Test
   public void testParse_WithInvalidTrailingMinusSign_ShouldReturnNull() {
      ProperFractionFormat format = new ProperFractionFormat();
      String input = "2 / 5 -";
    
      Fraction result = format.parse(input);
    
      Assert.assertNull(result);
   }

   @Test
   public void testParse_WithInvalidNegativeWholeNumber_ShouldReturnNull() {
      ProperFractionFormat format = new ProperFractionFormat();
      String input = "-2";
    
      Fraction result = format.parse(input);
    
      Assert.assertNull(result);
   }
}