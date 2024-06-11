import static org.junit.Assert.*;
import org.junit.Test;

public class FractionTest {
  
  @Test
  public void testConstructorDouble() {
    Fraction fraction = new Fraction(0.5);
    assertEquals(1, fraction.getNumerator());
    assertEquals(2, fraction.getDenominator());
  }
  
  @Test
  public void testConstructorMaxDenominator() {
    Fraction fraction = new Fraction(0.75, 100);
    assertEquals(3, fraction.getNumerator());
    assertEquals(4, fraction.getDenominator());
  }
  
  @Test
  public void testConstructorNegativeValue() {
    try {
      Fraction fraction = new Fraction(-0.5);
      fail("Expected FractionConversionException");
    } catch (FractionConversionException e) {
      // expected
    }
  }
  
  @Test
  public void testConstructorZeroDenominator() {
    try {
      Fraction fraction = new Fraction(1, 0);
      fail("Expected MathArithmeticException");
    } catch (MathArithmeticException e) {
      // expected
    }
  }
  
  @Test
  public void testAbs() {
    Fraction fraction = new Fraction(-1, 2);
    Fraction absFraction = fraction.abs();
    assertEquals(1, absFraction.getNumerator());
    assertEquals(2, absFraction.getDenominator());
  }
  
  @Test
  public void testAdd() {
    Fraction fraction1 = new Fraction(1, 2);
    Fraction fraction2 = new Fraction(1, 3);
    Fraction sum = fraction1.add(fraction2);
    assertEquals(5, sum.getNumerator());
    assertEquals(6, sum.getDenominator());
  }
  
  @Test
  public void testAddInt() {
    Fraction fraction = new Fraction(3, 4);
    Fraction sum = fraction.add(2);
    assertEquals(11, sum.getNumerator());
    assertEquals(4, sum.getDenominator());
  }
  
  @Test
  public void testSubtract() {
    Fraction fraction1 = new Fraction(5, 6);
    Fraction fraction2 = new Fraction(2, 3);
    Fraction difference = fraction1.subtract(fraction2);
    assertEquals(1, difference.getNumerator());
    assertEquals(6, difference.getDenominator());
  }
  
  @Test
  public void testSubtractInt() {
    Fraction fraction = new Fraction(7, 8);
    Fraction difference = fraction.subtract(3);
    assertEquals(1, difference.getNumerator());
    assertEquals(8, difference.getDenominator());
  }
  
  @Test
  public void testMultiply() {
    Fraction fraction1 = new Fraction(2, 3);
    Fraction fraction2 = new Fraction(3, 4);
    Fraction product = fraction1.multiply(fraction2);
    assertEquals(1, product.getNumerator());
    assertEquals(2, product.getDenominator());
  }
  
  @Test
  public void testMultiplyInt() {
    Fraction fraction = new Fraction(4, 5);
    Fraction product = fraction.multiply(2);
    assertEquals(8, product.getNumerator());
    assertEquals(5, product.getDenominator());
  }
  
  @Test
  public void testDivide() {
    Fraction fraction1 = new Fraction(1, 2);
    Fraction fraction2 = new Fraction(3, 4);
    Fraction quotient = fraction1.divide(fraction2);
    assertEquals(2, quotient.getNumerator());
    assertEquals(3, quotient.getDenominator());
  }
  
  @Test
  public void testDivideInt() {
    Fraction fraction = new Fraction(5, 6);
    Fraction quotient = fraction.divide(2);
    assertEquals(5, quotient.getNumerator());
    assertEquals(12, quotient.getDenominator());
  }
}