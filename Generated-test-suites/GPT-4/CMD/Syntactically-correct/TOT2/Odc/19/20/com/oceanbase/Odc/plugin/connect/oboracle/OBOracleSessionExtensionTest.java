package com.oceanbase.odc.plugin.connect.oboracle;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {
  private Calculator calculator;

  @Before
  public void setUp() {
    calculator = new Calculator();
  }
   
  @Test
  public void add_InputTwoNumbers_ReturnsTheirSum() {
    assertEquals(5, calculator.add(2, 3));
  }
   
  @Test
  public void subtract_inputTwoNumbers_returnsTheirDifference() {
    assertEquals(1, calculator.subtract(3, 2));
  }
   
  @Test
  public void multiply_inputTwoNumbers_returnsTheirProduct() {
    assertEquals(6, calculator.multiply(2, 3));
  }
   
  @Test
  public void divide_inputTwoNumbers_returnsTheirQuotient() {
    assertEquals(2, calculator.divide(4, 2));
  }
   
  @Test(expected = IllegalArgumentException.class)
  public void divide_divideByZero_returnsException() {
    calculator.divide(4, 0);
  }
}