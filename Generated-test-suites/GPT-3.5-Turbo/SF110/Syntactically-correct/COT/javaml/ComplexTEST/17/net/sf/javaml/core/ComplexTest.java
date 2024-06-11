import org.junit.*;
import static org.junit.Assert.*;

public class ComplexTest {

  // Tests for constructor Complex(double, double)
  @Test
  public void testConstructor1() {
    Complex complex = new Complex(2.0, 3.0);
    assertEquals(2.0, complex.re, 0.0);
    assertEquals(3.0, complex.im, 0.0);
  }

  // Tests for constructor Complex()
  @Test
  public void testConstructor2() {
    Complex complex = new Complex();
    assertEquals(0.0, complex.re, 0.0);
    assertEquals(0.0, complex.im, 0.0);
  }

  // Test for method toString()
  @Test
  public void testToString() {
    Complex complex = new Complex(2.0, 3.0);
    assertEquals("2.0 + 3.0i", complex.toString());
  }

  // Test for method abs()
  @Test
  public void testAbs() {
    Complex complex = new Complex(3.0, 4.0);
    assertEquals(5.0, complex.abs(), 0.0);
  }

  // Test for method plus(Complex)
  @Test
  public void testPlus() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(5.0, 7.0);
    complex1.plus(complex2);
    assertEquals(7.0, complex1.re, 0.0);
    assertEquals(10.0, complex1.im, 0.0);
  }

  // Test for method minus(Complex)
  @Test
  public void testMinus() {
    Complex complex1 = new Complex(5.0, 7.0);
    Complex complex2 = new Complex(2.0, 3.0);
    complex1.minus(complex2);
    assertEquals(3.0, complex1.re, 0.0);
    assertEquals(4.0, complex1.im, 0.0);
  }

  // Test for method times(Complex)
  @Test
  public void testTimes() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(4.0, 5.0);
    complex1.times(complex2);
    assertEquals(-7.0, complex1.re, 0.0);
    assertEquals(22.0, complex1.im, 0.0);
  }

  // Test for method times(double)
  @Test
  public void testTimesDouble() {
    Complex complex = new Complex(2.0, 3.0);
    complex.times(1.5);
    assertEquals(3.0, complex.re, 0.0);
    assertEquals(4.5, complex.im, 0.0);
  }

  // Test for method conjugate()
  @Test
  public void testConjugate() {
    Complex complex = new Complex(2.0, 3.0);
    complex.conjugate();
    assertEquals(2.0, complex.re, 0.0);
    assertEquals(-3.0, complex.im, 0.0);
  }

  // Test for static method plus(Complex, Complex)
  @Test
  public void testStaticPlus() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(4.0, 5.0);
    Complex sum = Complex.plus(complex1, complex2);
    assertEquals(6.0, sum.re, 0.0);
    assertEquals(8.0, sum.im, 0.0);
  }

  // Test for static method multiply(Complex, double)
  @Test
  public void testStaticMultiplyDouble() {
    Complex complex = new Complex(2.0, 3.0);
    Complex product = Complex.multiply(complex, 1.5);
    assertEquals(3.0, product.re, 0.0);
    assertEquals(4.5, product.im, 0.0);
  }

  // Test for static method multiply(Complex, Complex)
  @Test
  public void testStaticMultiplyComplex() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(4.0, 5.0);
    Complex product = Complex.multiply(complex1, complex2);
    assertEquals(-7.0, product.re, 0.0);
    assertEquals(22.0, product.im, 0.0);
  }

}