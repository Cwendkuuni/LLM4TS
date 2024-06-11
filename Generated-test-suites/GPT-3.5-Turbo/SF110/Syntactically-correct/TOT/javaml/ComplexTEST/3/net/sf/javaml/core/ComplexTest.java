
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.Complex;

public class ComplexTest {
  
  private Complex complex;
  private Complex otherComplex;
  
  @Before
  public void setUp() {
    complex = new Complex(2.5, 3.5);
    otherComplex = new Complex(1.5, 2.5);
  }
  
  @Test
  public void testComplexConstructorWithReAndIm() {
    assertEquals(2.5, complex.re, 0.0);
    assertEquals(3.5, complex.im, 0.0);
  }
  
  @Test
  public void testComplexConstructorWithoutArguments() {
    assertEquals(0.0, complex.re, 0.0);
    assertEquals(0.0, complex.im, 0.0);
  }
  
  @Test
  public void testToString() {
    assertEquals("2.5 + 3.5i", complex.toString());
  }
  
  @Test
  public void testAbs() {
    assertEquals(4.609772, complex.abs(), 0.0001);
  }
  
  @Test
  public void testPlus() {
    complex.plus(otherComplex);
    assertEquals(4.0, complex.re, 0.0);
    assertEquals(6.0, complex.im, 0.0);
  }
  
  @Test
  public void testMinus() {
    complex.minus(otherComplex);
    assertEquals(1.0, complex.re, 0.0);
    assertEquals(1.0, complex.im, 0.0);
  }
  
  @Test
  public void testTimesWithComplex() {
    complex.times(otherComplex);
    assertEquals(-4.5, complex.re, 0.0);
    assertEquals(11.75, complex.im, 0.0);
  }
  
  @Test
  public void testTimesWithAlpha() {
    complex.times(2.0);
    assertEquals(5.0, complex.re, 0.0);
    assertEquals(7.0, complex.im, 0.0);
  }
  
  @Test
  public void testConjugate() {
    complex.conjugate();
    assertEquals(2.5, complex.re, 0.0);
    assertEquals(-3.5, complex.im, 0.0);
  }
  
  @Test
  public void testPlusWithStaticMethod() {
    Complex result = Complex.plus(complex, otherComplex);
    assertEquals(4.0, result.re, 0.0);
    assertEquals(6.0, result.im, 0.0);
  }
  
  @Test
  public void testMultiplyWithDouble() {
    Complex result = Complex.multiply(complex, 2.0);
    assertEquals(5.0, result.re, 0.0);
    assertEquals(7.0, result.im, 0.0);
  }
  
  @Test
  public void testMultiplyWithComplex() {
    Complex result = Complex.multiply(complex, otherComplex);
    assertEquals(-4.5, result.re, 0.0);
    assertEquals(11.75, result.im, 0.0);
  }
  
  @Test
  public void testComplexI() {
    assertEquals(0.0, Complex.I.re, 0.0);
    assertEquals(1.0, Complex.I.im, 0.0);
  }
  
}
