import static org.junit.Assert.*;

import java.text.NumberFormat;
import java.text.ParseException;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() {
        NumberFormat format = NumberFormat.getInstance();
        properFractionFormat = new ProperFractionFormat(format);
    }
    
    @Test
    public void testFormat_WithPositiveFraction_ReturnsFormattedString() {
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        
        String actual = properFractionFormat.format(fraction);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormat_WithNegativeFraction_ReturnsFormattedString() {
        Fraction fraction = new Fraction(-5, 2);
        String expected = "-2 1/2";
        
        String actual = properFractionFormat.format(fraction);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormat_WithZeroFraction_ReturnsFormattedString() {
        Fraction fraction = new Fraction(0, 1);
        String expected = "0 / 1";
        
        String actual = properFractionFormat.format(fraction);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_WithValidFractionString_ReturnsFractionObject() {
        String fractionString = "2 3/4";
        Fraction expected = new Fraction(11, 4);
        
        Fraction actual = properFractionFormat.parse(fractionString);
        
        assertEquals(expected, actual);
    }
    
    @Test(expected = ParseException.class)
    public void testParse_WithInvalidFractionString_ThrowsParseException() {
        String fractionString = "2 -3/4";
        
        properFractionFormat.parse(fractionString);
    }
    
    @Test(expected = ParseException.class)
    public void testParse_WithInvalidWhitespaceInFractionString_ThrowsParseException() {
        String fractionString = "2 3 1/4";
        
        properFractionFormat.parse(fractionString);
    }
    
    @Test
    public void testParse_WithWholeNumberFractionString_ReturnsFractionObject() {
        String fractionString = "3";
        Fraction expected = new Fraction(3, 1);
        
        Fraction actual = properFractionFormat.parse(fractionString);
        
        assertEquals(expected, actual);
    }
}