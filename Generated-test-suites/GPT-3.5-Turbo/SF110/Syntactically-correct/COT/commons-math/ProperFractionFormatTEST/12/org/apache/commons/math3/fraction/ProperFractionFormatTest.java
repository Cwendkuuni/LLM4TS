import org.junit.Test;
import org.junit.Assert;

public class ProperFractionFormatTest {
    
    @Test
    public void testProperFractionFormatDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        Assert.assertNotNull(format.getWholeFormat());
    }
    
    @Test
    public void testProperFractionFormatConstructor() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat fractionFormat = new ProperFractionFormat(format);
        Assert.assertEquals(format, fractionFormat.getWholeFormat());
    }
    
    @Test
    public void testProperFractionFormatCustomConstructor() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        
        ProperFractionFormat fractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        
        Assert.assertEquals(wholeFormat, fractionFormat.getWholeFormat());
        Assert.assertEquals(numeratorFormat, fractionFormat.getNumeratorFormat());
        Assert.assertEquals(denominatorFormat, fractionFormat.getDenominatorFormat());
    }
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, pos);
        Assert.assertEquals("1 1 / 2", result.toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        Assert.assertEquals(wholeFormat, format.getWholeFormat());
    }
    
    @Test
    public void testParseEmptyString() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        Assert.assertNull(result);
        Assert.assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testParseInvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        Assert.assertNull(result);
        Assert.assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testParseNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        Assert.assertNull(result);
        Assert.assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testParseNegativeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 / -2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        Assert.assertNull(result);
        Assert.assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        Assert.assertEquals(wholeFormat, format.getWholeFormat());
    }
}

