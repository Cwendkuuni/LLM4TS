
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.FieldPosition;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFormat;
    
    @Before
    public void setUp() {
        properFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat_WholeAndFraction() {
        StringBuffer expected = new StringBuffer();
        expected.append("1 2 / 3");
        
        Fraction fraction = new Fraction(5, 3);
        StringBuffer actual = properFormat.format(fraction, new StringBuffer(), new FieldPosition(0));
        
        Assert.assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testFormat_OnlyFraction() {
        StringBuffer expected = new StringBuffer();
        expected.append("2 / 3");

        Fraction fraction = new Fraction(2, 3);
        StringBuffer actual = properFormat.format(fraction, new StringBuffer(), new FieldPosition(0));

        Assert.assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testFormat_NoFraction() {
        StringBuffer expected = new StringBuffer();
        expected.append("2");

        Fraction fraction = new Fraction(2);
        StringBuffer actual = properFormat.format(fraction, new StringBuffer(), new FieldPosition(0));

        Assert.assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testFormat_NegativeFraction() {
        StringBuffer expected = new StringBuffer();
        expected.append("-1 2 / 3");

        Fraction fraction = new Fraction(-5, 3);
        StringBuffer actual = properFormat.format(fraction, new StringBuffer(), new FieldPosition(0));
        
        Assert.assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = properFormat.getWholeFormat();
        
        Assert.assertNotNull(wholeFormat);
    }
    
    @Test
    public void testParse_ValidFraction() {
        String fractionString = "1 2 / 3";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction expected = new Fraction(5, 3);
        Fraction actual = properFormat.parse(fractionString, pos);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_InvalidFraction() {
        String fractionString = "1 2 3";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction actual = properFormat.parse(fractionString, pos);
        
        Assert.assertNull(actual);
        Assert.assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testSetWholeFormat_ValidFormat() {
        NumberFormat newFormat = NumberFormat.getInstance();
        
        properFormat.setWholeFormat(newFormat);
        
        Assert.assertEquals(newFormat, properFormat.getWholeFormat());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat_NullFormat() {
        properFormat.setWholeFormat(null);
    }

}
