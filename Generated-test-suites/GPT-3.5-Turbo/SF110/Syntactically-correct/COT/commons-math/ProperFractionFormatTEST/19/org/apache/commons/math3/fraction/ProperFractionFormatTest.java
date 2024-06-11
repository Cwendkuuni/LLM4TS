import org.junit.Test;
import org.junit.Assert;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test expected behavior
        Assert.assertEquals("", format.format(new Fraction(0, 1)));
    }
    
    @Test
    public void testParameterizedConstructor() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        
        // Test expected behavior
        Assert.assertEquals("", format.format(new Fraction(0, 1)));
    }
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0);
        
        // Test expected behavior
        format.format(new Fraction(0, 1), sb, fp);
        Assert.assertEquals("", sb.toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test expected behavior
        Assert.assertNotNull(format.getWholeFormat());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 / 2";
        ParsePosition pos = new ParsePosition(0);
        
        // Test expected behavior
        Fraction fraction = format.parse(source, pos);
        Assert.assertEquals(new Fraction(1, 2), fraction);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        
        // Test expected behavior
        format.setWholeFormat(wholeFormat);
        Assert.assertEquals(wholeFormat, format.getWholeFormat());
    }
}