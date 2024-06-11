import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

  @Test
  public void testFormatWithPositiveFraction() {
    ProperFractionFormat format = new ProperFractionFormat();
    Fraction fraction = new Fraction(3, 2);
    String expected = "1 1/2";
    assertEquals(expected, format.format(fraction));
  }

  @Test
  public void testFormatWithNegativeFraction() {
    ProperFractionFormat format = new ProperFractionFormat();
    Fraction fraction = new Fraction(-4, 3);
    String expected = "-1 1/3";
    assertEquals(expected, format.format(fraction));
  }

  @Test
  public void testFormatWithZeroWholeNumber() {
    ProperFractionFormat format = new ProperFractionFormat();
    Fraction fraction = new Fraction(2, 3);
    String expected = "2/3";
    assertEquals(expected, format.format(fraction));
  }

  @Test
  public void testParseWithValidProperFractionString() {
    ProperFractionFormat format = new ProperFractionFormat();
    String fractionString = "1 3/4";
    Fraction expected = new Fraction(7, 4);
    assertEquals(expected, format.parse(fractionString));
  }

  @Test
  public void testParseWithValidImproperFractionString() {
    ProperFractionFormat format = new ProperFractionFormat();
    String fractionString = "4/3";
    Fraction expected = new Fraction(4, 3);
    assertEquals(expected, format.parse(fractionString));
  }

  @Test
  public void testParseWithValidFractionStringWithNegativeWholeNumber() {
    ProperFractionFormat format = new ProperFractionFormat();
    String fractionString = "-2 1/5";
    Fraction expected = new Fraction(-11, 5);
    assertEquals(expected, format.parse(fractionString));
  }

  @Test
  public void testParseWithInvalidFractionString() {
    ProperFractionFormat format = new ProperFractionFormat();
    String fractionString = "1/2/3";
    assertNull(format.parse(fractionString));
  }

}