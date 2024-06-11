import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
  
  @Test
  public void testFormat() {
    ProperFractionFormat format = new ProperFractionFormat();
    assertEquals("1 / 2", format.format(new Fraction(1, 2)));
    assertEquals("3 / 4", format.format(new Fraction(3, 4)));
    assertEquals("-5 / 6", format.format(new Fraction(-5, 6)));
    assertEquals("7 1/2", format.format(new Fraction(15, 2)));
    assertEquals("-8 3/4", format.format(new Fraction(-35, 4)));
  }
  
  @Test
  public void testParse() {
    ProperFractionFormat format = new ProperFractionFormat();
    assertEquals(new Fraction(1, 2), format.parse("1 / 2"));
    assertEquals(new Fraction(3, 4), format.parse("3 / 4"));
    assertEquals(new Fraction(-5, 6), format.parse("-5 / 6"));
    assertEquals(new Fraction(15, 2), format.parse("7 1/2"));
    assertEquals(new Fraction(-35, 4), format.parse("-8 3/4"));
  }
  
  @Test
  public void testParse_invalidInput() {
    ProperFractionFormat format = new ProperFractionFormat();
    assertNull(format.parse("1/2/3"));
    assertNull(format.parse("1 /2"));
    assertNull(format.parse("/2"));
    assertNull(format.parse("1 /"));
    assertNull(format.parse("1 /2/3"));
    assertNull(format.parse("1 / 2 /3"));
    assertNull(format.parse("1 / 2/ 3"));
    assertNull(format.parse("1 / 2/3 "));
    assertNull(format.parse("1 / 2/3/4"));
  }
  
  @Test
  public void testGetWholeFormat() {
    ProperFractionFormat format = new ProperFractionFormat();
    assertNotNull(format.getWholeFormat());
  }
  
  @Test
  public void testSetWholeFormat() {
    ProperFractionFormat format = new ProperFractionFormat();
    NumberFormat newFormat = NumberFormat.getInstance();
    format.setWholeFormat(newFormat);
    assertSame(newFormat, format.getWholeFormat());
  }
}