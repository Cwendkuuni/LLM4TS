import org.junit.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
  
  ProperFractionFormat format;
  
  @Before
  public void setup() {
    format = new ProperFractionFormat();
  }
  
  @Test
  public void testFormat() {
    // test case 1
    Fraction fraction1 = new Fraction(1, 2);
    StringBuffer expected1 = new StringBuffer("1 / 2");
    StringBuffer result1 = format.format(fraction1, new StringBuffer(), null);
    assertEquals(expected1.toString(), result1.toString());
    
    // test case 2
    Fraction fraction2 = new Fraction(3, 4);
    StringBuffer expected2 = new StringBuffer("3 / 4");
    StringBuffer result2 = format.format(fraction2, new StringBuffer(), null);
    assertEquals(expected2.toString(), result2.toString());
    
    // test case 3
    Fraction fraction3 = new Fraction(5, 3);
    StringBuffer expected3 = new StringBuffer("1 2 / 3");
    StringBuffer result3 = format.format(fraction3, new StringBuffer(), null);
    assertEquals(expected3.toString(), result3.toString());
  }
  
  @Test
  public void testGetWholeFormat() {
    NumberFormat wholeFormat = format.getWholeFormat();
    assertNotNull(wholeFormat);
  }
  
  @Test
  public void testParse() {
    // test case 1
    String source1 = "1 / 2";
    ParsePosition pos1 = new ParsePosition(0);
    Fraction result1 = format.parse(source1, pos1);
    assertEquals(new Fraction(1, 2), result1);
    
    // test case 2
    String source2 = "3 / 4";
    ParsePosition pos2 = new ParsePosition(0);
    Fraction result2 = format.parse(source2, pos2);
    assertEquals(new Fraction(3, 4), result2);
    
    // test case 3
    String source3 = "1 2 / 3";
    ParsePosition pos3 = new ParsePosition(0);
    Fraction result3 = format.parse(source3, pos3);
    assertEquals(new Fraction(5, 3), result3);
  }
  
  @Test
  public void testSetWholeFormat() {
    NumberFormat newFormat = NumberFormat.getInstance();
    format.setWholeFormat(newFormat);
    
    NumberFormat resultFormat = format.getWholeFormat();
    assertEquals(newFormat, resultFormat);
  }
}