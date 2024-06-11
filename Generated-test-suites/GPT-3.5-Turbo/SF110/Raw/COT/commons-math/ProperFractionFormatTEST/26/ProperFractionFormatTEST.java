
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
  
  private ProperFractionFormat properFractionFormat;
  
  @Before
  public void setUp() {
    properFractionFormat = new ProperFractionFormat();
  }
  
  @Test
  public void testFormatNoWholeNumber() {
    Fraction fraction = new Fraction(1, 2);
    StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), new FieldPosition(0));
    assertEquals("1 / 2", result.toString());
  }
  
  @Test
  public void testFormatWithWholeNumber() {
    Fraction fraction = new Fraction(5, 2);
    StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), new FieldPosition(0));
    assertEquals("2 1 / 2", result.toString());
  }
  
  @Test
  public void testFormatNegativeFraction() {
    Fraction fraction = new Fraction(-3, 4);
    StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), new FieldPosition(0));
    assertEquals("-3 / 4", result.toString());
  }
  
  @Test
  public void testGetWholeFormat() {
    NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
    assertNotNull(wholeFormat);
  }
  
  @Test
  public void testParseNoWholeNumber() {
    Fraction fraction = properFractionFormat.parse("1 / 2", new ParsePosition(0));
    assertNotNull(fraction);
    assertEquals(1, fraction.getNumerator());
    assertEquals(2, fraction.getDenominator());
  }
  
  @Test
  public void testParseWithWholeNumber() {
    Fraction fraction = properFractionFormat.parse("3 1 / 2", new ParsePosition(0));
    assertNotNull(fraction);
    assertEquals(7, fraction.getNumerator());
    assertEquals(2, fraction.getDenominator());
  }
  
  @Test
  public void testParseNegativeFraction() {
    Fraction fraction = properFractionFormat.parse("-3 / 4", new ParsePosition(0));
    assertNotNull(fraction);
    assertEquals(-3, fraction.getNumerator());
    assertEquals(4, fraction.getDenominator());
  }
  
  @Test
  public void testSetWholeFormat() {
    NumberFormat format = NumberFormat.getInstance();
    properFractionFormat.setWholeFormat(format);
    assertEquals(format, properFractionFormat.getWholeFormat());
  }
  
}
