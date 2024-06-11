import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
        assertEquals(FractionFormat.getDefaultNumberFormat(), format.getWholeFormat());
    }
    
    @Test
    public void testParameterizedConstructor() {
        NumberFormat wholeFormat = NumberFormat.getCurrencyInstance();
        NumberFormat numeratorFormat = NumberFormat.getPercentInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(format);
        assertEquals(wholeFormat, format.getWholeFormat());
        assertEquals(numeratorFormat, format.getNumeratorFormat());
        assertEquals(denominatorFormat, format.getDenominatorFormat());
    }
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        StringBuffer result = format.format(new Fraction(2, 3));
        assertEquals("2 / 3", result.toString());
        
        result = format.format(new Fraction(5, 4));
        assertEquals("1 1 / 4", result.toString());
        
        result = format.format(new Fraction(0, 7));
        assertEquals("0 / 1", result.toString());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction result = format.parse("2 / 3");
        assertEquals(new Fraction(2, 3), result);
        
        result = format.parse("1 1 / 4");
        assertEquals(new Fraction(5, 4), result);
        
        result = format.parse("0 / 1");
        assertEquals(new Fraction(0, 1), result);
    }
    
    @Test
    public void testParseInvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction result = format.parse("2 3");
        assertNull(result);
        
        result = format.parse("2 /");
        assertNull(result);
        
        result = format.parse("/ 3");
        assertNull(result);
    }
    
    @Test
    public void testParseNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction result = format.parse("-2 / 3");
        assertEquals(new Fraction(-2, 3), result);
        
        result = format.parse("-1 1 / 4");
        assertEquals(new Fraction(-5, 4), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
    
}