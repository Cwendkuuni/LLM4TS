import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ComplexTest {

  @Test
  public void testToString() {
    Complex complex = new Complex(2.0, 3.0);
    String expected = "2.0 + 3.0i";
    assertEquals(expected, complex.toString());
  }

  @Test
  public void testAbs() {
    Complex complex = new Complex(3.0, 4.0);
    double expected = 5.0;
    assertEquals(expected, complex.abs(), 0.001);
  }

  @Test
  public void testPlus() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(4.0, 5.0);
    complex1.plus(complex2);
    double expectedRe = 6.0;
    double expectedIm = 8.0;
    assertEquals(expectedRe, complex1.re, 0.001);
    assertEquals(expectedIm, complex1.im, 0.001);
  }

  @Test
  public void testMinus() {
    Complex complex1 = new Complex(8.0, 10.0);
    Complex complex2 = new Complex(4.0, 5.0);
    complex1.minus(complex2);
    double expectedRe = 4.0;
    double expectedIm = 5.0;
    assertEquals(expectedRe, complex1.re, 0.001);
    assertEquals(expectedIm, complex1.im, 0.001);
  }

  @Test
  public void testTimes() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(4.0, 5.0);
    complex1.times(complex2);
    double expectedRe = -7.0;
    double expectedIm = 22.0;
    assertEquals(expectedRe, complex1.re, 0.001);
    assertEquals(expectedIm, complex1.im, 0.001);
  }

  @Test
  public void testTimesWithAlpha() {
    Complex complex = new Complex(2.0, 3.0);
    complex.times(2.0);
    double expectedRe = 4.0;
    double expectedIm = 6.0;
    assertEquals(expectedRe, complex.re, 0.001);
    assertEquals(expectedIm, complex.im, 0.001);
  }

  @Test
  public void testConjugate() {
    Complex complex = new Complex(2.0, 3.0);
    complex.conjugate();
    double expectedIm = -3.0;
    assertEquals(expectedIm, complex.im, 0.001);
  }

  @Test
  public void testPlusStatic() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(4.0, 5.0);
    Complex result = Complex.plus(complex1, complex2);
    double expectedRe = 6.0;
    double expectedIm = 8.0;
    assertEquals(expectedRe, result.re, 0.001);
    assertEquals(expectedIm, result.im, 0.001);
  }

  @Test
  public void testMultiplyDouble() {
    Complex complex = new Complex(2.0, 3.0);
    Complex result = Complex.multiply(complex, 2.0);
    double expectedRe = 4.0;
    double expectedIm = 6.0;
    assertEquals(expectedRe, result.re, 0.001);
    assertEquals(expectedIm, result.im, 0.001);
  }

  @Test
  public void testMultiplyComplex() {
    Complex complex1 = new Complex(2.0, 3.0);
    Complex complex2 = new Complex(4.0, 5.0);
    Complex result = Complex.multiply(complex1, complex2);
    double expectedRe = -7.0;
    double expectedIm = 22.0;
    assertEquals(expectedRe, result.re, 0.001);
    assertEquals(expectedIm, result.im, 0.001);
  }

}