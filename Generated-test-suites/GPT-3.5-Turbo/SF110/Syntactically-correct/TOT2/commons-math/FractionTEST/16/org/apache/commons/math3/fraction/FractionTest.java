package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {
  
  @Test
  public void testFractionDoubleConstructor() {
    // Typical case
    Fraction frac1 = new Fraction(0.5);
    assertEquals("0.5", frac1.toString());
    // Edge case
    Fraction frac2 = new Fraction(2147483647.0);
    assertEquals("2147483647", frac2.toString());
  }
  
  @Test
  public void testFractionDoubleConstructorWithEpsilon() {
    // Typical case
    Fraction frac1 = new Fraction(0.5, 1e-5, 100);
    assertEquals("1 / 2", frac1.toString());
    // Edge case
    Fraction frac2 = new Fraction(Double.MAX_VALUE, 1e-5, 100);
    assertEquals("45035996273704967 / 2", frac2.toString());
  }
  
  @Test
  public void testFractionDoubleConstructorWithMaxDenominator() {
    // Typical case
    Fraction frac1 = new Fraction(0.5, 100);
    assertEquals("1 / 2", frac1.toString());
    // Edge case
    Fraction frac2 = new Fraction(0.5, Integer.MAX_VALUE);
    assertEquals("1 / 2", frac2.toString());
  }

  // Add more test cases for other methods
  
}