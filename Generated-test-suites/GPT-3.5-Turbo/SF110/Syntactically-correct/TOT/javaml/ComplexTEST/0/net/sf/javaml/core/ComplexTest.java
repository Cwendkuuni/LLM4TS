import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {
  
  // Test case for the Complex constructor with parameters
  @Test
  public void testConstructorWithParameters() {
    Complex complex = new Complex(3.0, 4.0);
    assertEquals(3.0, complex.re, 0.0);
    assertEquals(4.0, complex.im, 0.0);
  }
  
  // Test case for the Complex constructor without parameters
  @Test
  public void testConstructorWithoutParameters() {
    Complex complex = new Complex();
    assertEquals(0.0, complex.re, 0.0);
    assertEquals(0.0, complex.im, 0.0);
  }
  
  // Test case for the toString method
  @Test
  public void testToString() {
    Complex complex = new Complex(3.0, 4.0);
    assertEquals("3.0 + 4.0i", complex.toString());
  }
  
  // Test case for the abs method
  @Test
  public void testAbs() {
    Complex complex = new Complex(3.0, 4.0);
    assertEquals(5.0, complex.abs(), 0.0);
  }
  
  // Test case for the plus method
  @Test
  public void testPlus() {
    Complex complex1 = new Complex(3.0, 4.0);
    Complex complex2 = new Complex(2.0, 1.0);
    complex1.plus(complex2);
    assertEquals(5.0, complex1.re, 0.0);
    assertEquals(5.0, complex1.im, 0.0);
  }
  
  // Test case for the minus method
  @Test
  public void testMinus() {
    Complex complex1 = new Complex(3.0, 4.0);
    Complex complex2 = new Complex(2.0, 1.0);
    complex1.minus(complex2);
    assertEquals(1.0, complex1.re, 0.0);
    assertEquals(3.0, complex1.im, 0.0);
  }
  
  // Test case for the times method with Complex parameter
  @Test
  public void testTimesWithComplex() {
    Complex complex1 = new Complex(3.0, 4.0);
    Complex complex2 = new Complex(2.0, 1.0);
    complex1.times(complex2);
    assertEquals(2.0, complex1.re, 0.0);
    assertEquals(11.0, complex1.im, 0.0);
  }
  
  // Test case for the times method with double parameter
  @Test
  public void testTimesWithDouble() {
    Complex complex = new Complex(3.0, 4.0);
    complex.times(2.0);
    assertEquals(6.0, complex.re, 0.0);
    assertEquals(8.0, complex.im, 0.0);
  }
  
  // Test case for the conjugate method
  @Test
  public void testConjugate() {
    Complex complex = new Complex(3.0, 4.0);
    complex.conjugate();
    assertEquals(3.0, complex.re, 0.0);
    assertEquals(-4.0, complex.im, 0.0);
  }
  
  // Test case for the static plus method
  @Test
  public void testStaticPlus() {
    Complex complex1 = new Complex(3.0, 4.0);
    Complex complex2 = new Complex(2.0, 1.0);
    Complex result = Complex.plus(complex1, complex2);
    assertEquals(5.0, result.re, 0.0);
    assertEquals(5.0, result.im, 0.0);
  }
  
  // Test case for the static multiply method with Complex parameter
  @Test
  public void testStaticMultiplyWithComplex() {
    Complex complex1 = new Complex(3.0, 4.0);
    Complex complex2 = new Complex(2.0, 1.0);
    Complex result = Complex.multiply(complex1, complex2);
    assertEquals(2.0, result.re, 0.0);
    assertEquals(11.0, result.im, 0.0);
  }
  
  // Test case for the static multiply method with double parameter
  @Test
  public void testStaticMultiplyWithDouble() {
    Complex complex = new Complex(3.0, 4.0);
    Complex result = Complex.multiply(complex, 2.0);
    assertEquals(6.0, result.re, 0.0);
    assertEquals(8.0, result.im, 0.0);
  }
  
}