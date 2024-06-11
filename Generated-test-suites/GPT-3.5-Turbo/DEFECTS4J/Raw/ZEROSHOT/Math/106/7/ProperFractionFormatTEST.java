import static org.junit.Assert.*;
import org.junit.Test;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;

public class ProperFractionFormatTest {
  
  // Test format method
  @Test
  public void testFormat() {
    ProperFractionFormat properFractionFormat = new ProperFractionFormat();

    // Test case 1
    Fraction fraction1 = new Fraction(1, 2);
    StringBuffer toAppendTo1 = new StringBuffer();
    StringBuffer expected1 = new StringBuffer();
    expected1.append("1 / 2");
    FieldPosition pos1 = new FieldPosition(0);
    assertEquals(expected1, properFractionFormat.format(fraction1, toAppendTo1, pos1));

    // Test case 2
    Fraction fraction2 = new Fraction(-3, 2);
    StringBuffer toAppendTo2 = new StringBuffer();
    StringBuffer expected2 = new StringBuffer();
    expected2.append("-3 / 2");
    FieldPosition pos2 = new FieldPosition(0);
    assertEquals(expected2, properFractionFormat.format(fraction2, toAppendTo2, pos2));
  }
  
  // Test getWholeFormat method
  @Test
  public void testGetWholeFormat() {
      ProperFractionFormat properFractionFormat = new ProperFractionFormat();
      NumberFormat expected = properFractionFormat.getWholeFormat();
      assertEquals(expected, properFractionFormat.getWholeFormat());
  }
  
  // Test parse method
  @Test
  public void testParse() {
    ProperFractionFormat properFractionFormat = new ProperFractionFormat();
    String source = "1 / 2";
    ParsePosition pos = new ParsePosition(0);
    Fraction expected = new Fraction(1, 2);
    assertEquals(expected, properFractionFormat.parse(source, pos));
    
    String invalidSource = "1 2/3";
    ParsePosition invalidPos = new ParsePosition(0);
    assertNull(properFractionFormat.parse(invalidSource, invalidPos));
  }
  
  // Test setWholeFormat method
  @Test(expected = IllegalArgumentException.class)
  public void testSetWholeFormat() {
    ProperFractionFormat properFractionFormat = new ProperFractionFormat();
    properFractionFormat.setWholeFormat(null);
  }
}