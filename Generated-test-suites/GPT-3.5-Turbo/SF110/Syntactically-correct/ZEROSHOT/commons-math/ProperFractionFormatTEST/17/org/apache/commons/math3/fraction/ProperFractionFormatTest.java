import org.junit.Test;
import org.junit.Assert;

public class ProperFractionFormatTest {
  
  @Test
  public void testConstructor1() {
    ProperFractionFormat pff = new ProperFractionFormat();
    Assert.assertNotNull(pff);
  }
  
  @Test
  public void testConstructor2() {
    NumberFormat format = NumberFormat.getInstance();
    ProperFractionFormat pff = new ProperFractionFormat(format);
    Assert.assertNotNull(pff);
  }
  
  @Test
  public void testConstructor3() {
    NumberFormat wholeFormat = NumberFormat.getInstance();
    NumberFormat numeratorFormat = NumberFormat.getInstance();
    NumberFormat denominatorFormat = NumberFormat.getInstance();
    ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
    Assert.assertNotNull(pff);
  }
  
  @Test
  public void testFormat() {
    ProperFractionFormat pff = new ProperFractionFormat();
    Fraction fraction = new Fraction(2, 3);
    StringBuffer toAppendTo = new StringBuffer();
    FieldPosition pos = new FieldPosition(0);
    StringBuffer result = pff.format(fraction, toAppendTo, pos);
    
    Assert.assertNotNull(result);
    Assert.assertEquals("2 / 3", result.toString());
  }
  
  @Test
  public void testGetWholeFormat() {
    ProperFractionFormat pff = new ProperFractionFormat();
    NumberFormat wholeFormat = pff.getWholeFormat();
    
    Assert.assertNotNull(wholeFormat);
  }
  
  @Test
  public void testParse() {
    ProperFractionFormat pff = new ProperFractionFormat();
    String source = "2 / 3";
    ParsePosition pos = new ParsePosition(0);
    Fraction result = pff.parse(source, pos);
    
    Assert.assertNotNull(result);
    Assert.assertEquals(2, result.getNumerator());
    Assert.assertEquals(3, result.getDenominator());
  }
  
  @Test
  public void testSetWholeFormat() {
    ProperFractionFormat pff = new ProperFractionFormat();
    NumberFormat format = NumberFormat.getInstance();
    pff.setWholeFormat(format);
    
    NumberFormat wholeFormat = pff.getWholeFormat();
    
    Assert.assertEquals(format, wholeFormat);
  }
}