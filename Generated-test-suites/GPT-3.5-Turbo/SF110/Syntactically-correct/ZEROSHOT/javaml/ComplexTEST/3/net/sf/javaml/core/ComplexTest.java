import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

  @Test
  public void testConstructor() {
    Complex complex = new Complex(2.5, 3.7);
    Assert.assertEquals(2.5, complex.re, 0.01);
    Assert.assertEquals(3.7, complex.im, 0.01);
  }

  @Test
  public void testDefaultConstructor() {
    Complex complex = new Complex();
    Assert.assertEquals(0.0, complex.re, 0.01);
    Assert.assertEquals(0.0, complex.im, 0.01);
  }

  @Test
  public void testToString() {
    Complex complex = new Complex(2.5, 3.7);
    String expected = "2.5 + 3.7i";
    Assert.assertEquals(expected, complex.toString());
  }

  @Test
  public void testAbs() {
    Complex complex = new Complex(2.0, 3.0);
    double expected = Math.sqrt(13.0);
    Assert.assertEquals(expected, complex.abs(), 0.01);
  }

  @Test
  public void testPlus() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(4.0, 5.0);
    complex1.plus(complex2);
    Assert.assertEquals(6.0, complex1.re, 0.01);
    Assert.assertEquals(8.0, complex1.im, 0.01);
  }

  @Test
  public void testMinus() {
    Complex complex1 = new Complex(4.0, 5.0);
    Complex complex2 = new Complex(2.0, 3.0);
    complex1.minus(complex2);
    Assert.assertEquals(2.0, complex1.re, 0.01);
    Assert.assertEquals(2.0, complex1.im, 0.01);
  }

  @Test
  public void testTimesWithComplex() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(4.0, 5.0);
    complex1.times(complex2);
    Assert.assertEquals(-7.0, complex1.re, 0.01);
    Assert.assertEquals(22.0, complex1.im, 0.01);
  }

  @Test
  public void testTimesWithDouble() {
    Complex complex = new Complex(2.0, 3.0);
    complex.times(2.5);
    Assert.assertEquals(5.0, complex.re, 0.01);
    Assert.assertEquals(7.5, complex.im, 0.01);
  }

  @Test
  public void testConjugate() {
    Complex complex = new Complex(2.5, 3.7);
    complex.conjugate();
    Assert.assertEquals(2.5, complex.re, 0.01);
    Assert.assertEquals(-3.7, complex.im, 0.01);
  }

  @Test
  public void testStaticPlus() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(4.0, 5.0);
    Complex result = Complex.plus(complex1, complex2);
    Assert.assertEquals(6.0, result.re, 0.01);
    Assert.assertEquals(8.0, result.im, 0.01);
  }

  @Test
  public void testStaticMultiplyWithDouble() {
    Complex complex = new Complex(2.0, 3.0);
    Complex result = Complex.multiply(complex, 2.5);
    Assert.assertEquals(5.0, result.re, 0.01);
    Assert.assertEquals(7.5, result.im, 0.01);
  }

  @Test
  public void testStaticMultiplyWithComplex() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(4.0, 5.0);
    Complex result = Complex.multiply(complex1, complex2);
    Assert.assertEquals(-7.0, result.re, 0.01);
    Assert.assertEquals(22.0, result.im, 0.01);
  }
  
  @Test
  public void testImaginaryConstant() {
    Assert.assertEquals(0.0, Complex.I.re, 0.01);
    Assert.assertEquals(1.0, Complex.I.im, 0.01);
  }
}