import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

  @Test
  public void testFormat() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    
    // typical use case
    Fraction fraction = new Fraction(3, 2);
    String expected = "1 1/2";
    String actual = formatter.format(fraction);
    assertEquals(expected, actual);
    
    // edge case: whole number equals 0
    fraction = new Fraction(2, 3);
    expected = "2/3";
    actual = formatter.format(fraction);
    assertEquals(expected, actual);
    
    // edge case: numerator equals 0
    fraction = new Fraction(0, 5);
    expected = "0";
    actual = formatter.format(fraction);
    assertEquals(expected, actual);
    
    // edge case: denominator equals 1
    fraction = new Fraction(3, 1);
    expected = "3";
    actual = formatter.format(fraction);
    assertEquals(expected, actual);
    
    // error scenario: invalid fraction
    fraction = new Fraction(-3, -2);
    expected = "-3/-2";
    actual = formatter.format(fraction);
    assertNotEquals(expected, actual);
  }
  
  @Test
  public void testParse() {
    ProperFractionFormat formatter = new ProperFractionFormat();
    
    // typical use case
    String fractionString = "1 1/2";
    Fraction expected = new Fraction(3, 2);
    Fraction actual = formatter.parse(fractionString);
    assertEquals(expected, actual);
    
    // edge case: whole number equals 0
    fractionString = "0 2/3";
    expected = new Fraction(2, 3);
    actual = formatter.parse(fractionString);
    assertEquals(expected, actual);
    
    // edge case: numerator equals 0
    fractionString = "0";
    expected = new Fraction(0);
    actual = formatter.parse(fractionString);
    assertEquals(expected, actual);
    
    // edge case: denominator equals 1
    fractionString = "3";
    expected = new Fraction(3);
    actual = formatter.parse(fractionString);
    assertEquals(expected, actual);
    
    // error scenario: invalid fraction
    fractionString = "-3/-2";
    expected = null;
    actual = formatter.parse(fractionString);
    assertEquals(expected, actual);
  }
  
}