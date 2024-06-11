import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test with whole number
        assertEquals("1", format.format(new Fraction(1)));
        
        // Test with fraction
        assertEquals("1 / 2", format.format(new Fraction(1, 2)));
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test with whole number
        Fraction fraction1 = format.parse("2", null);
        assertEquals(2, fraction1.getNumerator());
        assertEquals(1, fraction1.getDenominator());
        
        // Test with fraction
        Fraction fraction2 = format.parse("1 / 2", null);
        assertEquals(1, fraction2.getNumerator());
        assertEquals(2, fraction2.getDenominator());
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test default whole format
        assertNotNull(format.getWholeFormat());
        
        // Test set whole format
        format.setWholeFormat(NumberFormat.getInstance());
        assertNotNull(format.getWholeFormat());
    }
    
    @Test
    public void testConstructor() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        
        // Test constructor with whole format
        ProperFractionFormat format1 = new ProperFractionFormat(wholeFormat);
        assertNotNull(format1.getWholeFormat());
        
        // Test constructor with all formats
        ProperFractionFormat format2 = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(format2.getWholeFormat());
        assertNotNull(format2.getNumeratorFormat());
        assertNotNull(format2.getDenominatorFormat());
    }
}