import org.junit.*;


public class ProperFractionFormatTest {

  @Test
  public void testFormat() {
    ProperFractionFormat format = new ProperFractionFormat();
    
    // Test case 1: fraction with whole number
    Fraction fraction1 = new Fraction(2, 3);
    StringBuffer expected1 = new StringBuffer();
    expected1.append("0 2 / 3");
    StringBuffer result1 = format.format(fraction1, new StringBuffer(), null);
    Assert.assertEquals(expected1.toString(), result1.toString());
    
    // Test case 2: fraction without whole number
    Fraction fraction2 = new Fraction(5, 8);
    StringBuffer expected2 = new StringBuffer();
    expected2.append("5 / 8");
    StringBuffer result2 = format.format(fraction2, new StringBuffer(), null);
    Assert.assertEquals(expected2.toString(), result2.toString());
    
    // Test case 3: fraction with negative whole number
    Fraction fraction3 = new Fraction(-7, 9);
    StringBuffer expected3 = new StringBuffer();
    expected3.append("0 7 / 9");
    StringBuffer result3 = format.format(fraction3, new StringBuffer(), null);
    Assert.assertEquals(expected3.toString(), result3.toString());
  }

  
  @Test
  public void testParse() {
    ProperFractionFormat format = new ProperFractionFormat();
    
    // Test case 1: valid fraction with whole number
    String source1 = "2 3 / 4";
    Fraction expected1 = new Fraction(11, 4);
    ParsePosition pos1 = new ParsePosition(0);
    Fraction result1 = format.parse(source1, pos1);
    Assert.assertEquals(expected1.getNumerator(), result1.getNumerator());
    Assert.assertEquals(expected1.getDenominator(), result1.getDenominator());
    Assert.assertEquals(source1.length(), pos1.getIndex());
    
    // Test case 2: valid fraction without whole number
    String source2 = "5 / 7";
    Fraction expected2 = new Fraction(5, 7);
    ParsePosition pos2 = new ParsePosition(0);
    Fraction result2 = format.parse(source2, pos2);
    Assert.assertEquals(expected2.getNumerator(), result2.getNumerator());
    Assert.assertEquals(expected2.getDenominator(), result2.getDenominator());
    Assert.assertEquals(source2.length(), pos2.getIndex());
    
    // Test case 3: invalid fraction (negative numerator)
    String source3 = "-2 / 5";
    ParsePosition pos3 = new ParsePosition(0);
    Fraction result3 = format.parse(source3, pos3);
    Assert.assertNull(result3);
    Assert.assertEquals(0, pos3.getIndex());
    
    // Test case 4: invalid fraction (missing denominator)
    String source4 = "3 ";
    ParsePosition pos4 = new ParsePosition(0);
    Fraction result4 = format.parse(source4, pos4);
    Assert.assertNull(result4);
    Assert.assertEquals(0, pos4.getIndex());
  }
  
  @Test
  public void testGetWholeFormat() {
    ProperFractionFormat format = new ProperFractionFormat();
    format.setWholeFormat(NumberFormat.getInstance());
    
    Assert.assertNotNull(format.getWholeFormat());
  }
  
  @Test(expected = NullArgumentException.class)
  public void testSetWholeFormatWithNull() {
    ProperFractionFormat format = new ProperFractionFormat();
    format.setWholeFormat(null);
  }
  
}