import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComplexTest {

  @Test
  public void testToString() {
    Complex c1 = new Complex(2.0, 3.0);
    assertEquals("2.0 + 3.0i", c1.toString());

    Complex c2 = new Complex(0.0, 0.0);
    assertEquals("0.0 + 0.0i", c2.toString());
  }

  @Test
  public void testAbs() {
    Complex c1 = new Complex(3.0, 4.0);
    double result1 = c1.abs();
    assertEquals(5.0, result1, 0.0001);

    Complex c2 = new Complex(0.0, 0.0);
    double result2 = c2.abs();
    assertEquals(0.0, result2, 0.0001);
  }

  @Test
  public void testPlus() {
    Complex c1 = new Complex(2.0, 3.0);
    Complex c2 = new Complex(1.0, 2.0);

    c1.plus(c2);
    assertEquals("3.0 + 5.0i", c1.toString());
  }

  @Test
  public void testMinus() {
    Complex c1 = new Complex(2.0, 3.0);
    Complex c2 = new Complex(1.0, 2.0);

    c1.minus(c2);
    assertEquals("1.0 + 1.0i", c1.toString());
  }

  @Test
  public void testTimes() {
    Complex c1 = new Complex(2.0, 3.0);
    Complex c2 = new Complex(1.0, 2.0);

    c1.times(c2);
    assertEquals("-4.0 + 7.0i", c1.toString());
  }

  @Test
  public void testTimesWithAlpha() {
    Complex c1 = new Complex(2.0, 3.0);
    double alpha = 2.0;

    c1.times(alpha);
    assertEquals("4.0 + 6.0i", c1.toString());
  }

  @Test
  public void testConjugate() {
    Complex c1 = new Complex(2.0, 3.0);

    c1.conjugate();
    assertEquals("2.0 - 3.0i", c1.toString());
  }

  @Test
  public void testStaticPlus() {
    Complex c1 = new Complex(2.0, 3.0);
    Complex c2 = new Complex(1.0, 2.0);

    Complex result = Complex.plus(c1, c2);
    assertEquals("3.0 + 5.0i", result.toString());
  }

  @Test
  public void testStaticMultiplyWithDouble() {
    Complex c1 = new Complex(2.0, 3.0);
    double d = 2.0;

    Complex result = Complex.multiply(c1, d);
    assertEquals("4.0 + 6.0i", result.toString());
  }

  @Test
  public void testStaticMultiplyWithComplex() {
    Complex c1 = new Complex(2.0, 3.0);
    Complex c2 = new Complex(1.0, 2.0);

    Complex result = Complex.multiply(c1, c2);
    assertEquals("-4.0 + 7.0i", result.toString());
  }

  @Test
  public void testInitializationOfI() {
    Complex i = Complex.I;
    assertEquals("0.0 + 1.0i", i.toString());
  }

}