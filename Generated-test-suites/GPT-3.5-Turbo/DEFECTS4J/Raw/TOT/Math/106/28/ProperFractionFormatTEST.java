import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {

  @Test
  public void testFormat_PositiveFraction() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    String result = formatter.format(new Fraction(3, 4));
    Assert.assertEquals("3 / 4", result);
  }

  @Test
  public void testFormat_NegativeFraction() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    String result = formatter.format(new Fraction(-5, 6));
    Assert.assertEquals("-5 / 6", result);
  }

  @Test
  public void testFormat_WholeNumber() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    String result = formatter.format(new Fraction(2, 1));
    Assert.assertEquals("2", result);
  }

  @Test
  public void testFormat_WholeNumberWithFraction() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    String result = formatter.format(new Fraction(5, 1));
    Assert.assertEquals("5", result);
  }

  @Test
  public void testFormat_ZeroFraction() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    String result = formatter.format(new Fraction(0, 1));
    Assert.assertEquals("0", result);
  }

  @Test
  public void testParse_ValidFractionString_PositiveFraction() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    Fraction result = formatter.parse("3 / 4");
    Assert.assertEquals(new Fraction(3, 4), result);
  }

  @Test
  public void testParse_ValidFractionString_NegativeFraction() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    Fraction result = formatter.parse("-5 / 6");
    Assert.assertEquals(new Fraction(-5, 6), result);
  }

  @Test
  public void testParse_ValidFractionString_WholeNumber() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    Fraction result = formatter.parse("2");
    Assert.assertEquals(new Fraction(2, 1), result);
  }

  @Test
  public void testParse_ValidFractionString_WholeNumberWithFraction() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    Fraction result = formatter.parse("5");
    Assert.assertEquals(new Fraction(5, 1), result);
  }

  @Test
  public void testParse_ValidFractionString_ZeroFraction() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    Fraction result = formatter.parse("0");
    Assert.assertEquals(new Fraction(0, 1), result);
  }

  @Test
  public void testParse_InvalidFractionString_InvalidDenominator() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    Fraction result = formatter.parse("3 / 0");
    Assert.assertNull(result);
  }

  @Test
  public void testParse_InvalidFractionString_MissingSlash() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    Fraction result = formatter.parse("3 4");
    Assert.assertNull(result);
  }

  @Test
  public void testParse_InvalidFractionString_InvalidWholeNumber() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    Fraction result = formatter.parse("- 3 / 4");
    Assert.assertNull(result);
  }

  @Test
  public void testParse_InvalidFractionString_InvalidMinusSignPosition() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    Fraction result = formatter.parse("- 3 / -4");
    Assert.assertNull(result);
  }
}