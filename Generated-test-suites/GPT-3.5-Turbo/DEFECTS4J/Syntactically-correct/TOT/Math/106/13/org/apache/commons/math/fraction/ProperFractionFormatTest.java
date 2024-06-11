import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
  private ProperFractionFormat properFractionFormat;

  @Before
  public void setUp() {
    properFractionFormat = new ProperFractionFormat();
  }

  @Test
  public void testFormat_withPositiveWholeNumber_returnFormattedFraction() {
    Fraction fraction = new Fraction(3, 2);
    String expected = "1 1/2";
    String actual = properFractionFormat.format(fraction);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormat_withNegativeWholeNumber_returnFormattedFraction() {
    Fraction fraction = new Fraction(-5, 4);
    String expected = "-1 1/4";
    String actual = properFractionFormat.format(fraction);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormat_withZeroWholeNumber_returnFormattedFraction() {
    Fraction fraction = new Fraction(3, 4);
    String expected = "3/4";
    String actual = properFractionFormat.format(fraction);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormat_withPositiveFraction_returnFormattedFraction() {
    Fraction fraction = new Fraction(7, 9);
    String expected = "7/9";
    String actual = properFractionFormat.format(fraction);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormat_withNegativeFraction_returnFormattedFraction() {
    Fraction fraction = new Fraction(-9, 10);
    String expected = "-9/10";
    String actual = properFractionFormat.format(fraction);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFormat_withZeroFraction_returnFormattedFraction() {
    Fraction fraction = new Fraction(0, 1);
    String expected = "0";
    String actual = properFractionFormat.format(fraction);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testParse_withValidInput_returnCorrectFraction() {
    String input = "2 3/4";
    Fraction expected = new Fraction(11, 4);
    Fraction actual = properFractionFormat.parse(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testParse_withValidInputWholeOnly_returnCorrectFraction() {
    String input = "7";
    Fraction expected = new Fraction(7, 1);
    Fraction actual = properFractionFormat.parse(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testParse_withValidInputFractionOnly_returnCorrectFraction() {
    String input = "3/5";
    Fraction expected = new Fraction(3, 5);
    Fraction actual = properFractionFormat.parse(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testParse_withInvalidInput_returnNull() {
    String input = "2 -3/4";
    Fraction actual = properFractionFormat.parse(input);
    Assert.assertNull(actual);
  }

  @Test
  public void testParse_withEmptyInput_returnNull() {
    String input = "";
    Fraction actual = properFractionFormat.parse(input);
    Assert.assertNull(actual);
  }

  @Test
  public void testParse_withInvalidWhole_returnNull() {
    String input = "5.2 3/4";
    Fraction actual = properFractionFormat.parse(input);
    Assert.assertNull(actual);
  }

  @Test
  public void testParse_withInvalidFraction_returnNull() {
    String input = "2 3/0";
    Fraction actual = properFractionFormat.parse(input);
    Assert.assertNull(actual);
  }
}