import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

  @Test
  public void testToString() {
    Complex complex = new Complex(3.0, 4.0);
    String expected = "3.0 + 4.0i";
    Assert.assertEquals(expected, complex.toString());
  }

  @Test
  public void testAbs() {
    Complex complex = new Complex(3.0, 4.0);
    double expected = 5.0;
    Assert.assertEquals(expected, complex.abs(), 0.001);
  }

  @Test
  public void testPlus() {
    Complex complex1 = new Complex(3.0, 4.0);
    Complex complex2 = new Complex(2.0, 3.0);
    complex1.plus(complex2);
    Complex expected = new Complex(5.0, 7.0);
    Assert.assertEquals(expected.re, complex1.re, 0.001);
    Assert.assertEquals(expected.im, complex1.im, 0.001);
  }

  @Test
  public void testMinus() {
    Complex complex1 = new Complex(3.0, 4.0);
    Complex complex2 = new Complex(2.0, 3.0);
    complex1.minus(complex2);
    Complex expected = new Complex(1.0, 1.0);
    Assert.assertEquals(expected.re, complex1.re, 0.001);
    Assert.assertEquals(expected.im, complex1.im, 0.001);
  }

  @Test
  public void testTimesComplex() {
    Complex complex1 = new Complex(3.0, 4.0);
    Complex complex2 = new Complex(2.0, 3.0);
    complex1.times(complex2);
    Complex expected = new Complex(-6.0, 17.0);
    Assert.assertEquals(expected.re, complex1.re, 0.001);
    Assert.assertEquals(expected.im, complex1.im, 0.001);
  }

  @Test
  public void testTimesDouble() {
    Complex complex = new Complex(3.0, 4.0);
    double alpha = 2.0;
    complex.times(alpha);
    Complex expected = new Complex(6.0, 8.0);
    Assert.assertEquals(expected.re, complex.re, 0.001);
    Assert.assertEquals(expected.im, complex.im, 0.001);
  }

  @Test
  public void testConjugate() {
    Complex complex = new Complex(3.0, 4.0);
    complex.conjugate();
    Complex expected = new Complex(3.0, -4.0);
    Assert.assertEquals(expected.re, complex.re, 0.001);
    Assert.assertEquals(expected.im, complex.im, 0.001);
  }

  @Test
  public void testPlusStatic() {
    Complex complex1 = new Complex(3.0, 4.0);
    Complex complex2 = new Complex(2.0, 3.0);
    Complex result = Complex.plus(complex1, complex2);
    Complex expected = new Complex(5.0, 7.0);
    Assert.assertEquals(expected.re, result.re, 0.001);
    Assert.assertEquals(expected.im, result.im, 0.001);
  }

  @Test
  public void testMultiplyDouble() {
    Complex complex = new Complex(3.0, 4.0);
    double d = 2.0;
    Complex result = Complex.multiply(complex, d);
    Complex expected = new Complex(6.0, 8.0);
    Assert.assertEquals(expected.re, result.re, 0.001);
    Assert.assertEquals(expected.im, result.im, 0.001);
  }

  @Test
  public void testMultiplyComplex() {
    Complex complex1 = new Complex(3.0, 4.0);
    Complex complex2 = new Complex(2.0, 3.0);
    Complex result = Complex.multiply(complex1, complex2);
    Complex expected = new Complex(-6.0, 17.0);
    Assert.assertEquals(expected.re, result.re, 0.001);
    Assert.assertEquals(expected.im, result.im, 0.001);
  }
  
  // Additional tests for edge cases and exception handling scenarios
  // ...

}

