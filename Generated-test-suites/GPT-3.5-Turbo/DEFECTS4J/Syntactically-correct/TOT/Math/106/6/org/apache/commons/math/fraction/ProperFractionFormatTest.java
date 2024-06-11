import static org.junit.Assert.*;

import java.text.NumberFormat;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;


public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFormat;
    
    @Before
    public void setUp() {
        NumberFormat format = NumberFormat.getInstance();
        properFormat = new ProperFractionFormat(format);
    }
    
    @Test
    public void testFormat_WholeFraction() {
        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1/2";
        String actual = properFormat.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormat_ImproperFraction() {
        Fraction fraction = new Fraction(5, 2);
        String expected = "2 1/2";
        String actual = properFormat.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormat_WholeNumber() {
        Fraction fraction = new Fraction(5, 1);
        String expected = "5";
        String actual = properFormat.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormat_ZeroFraction() {
        Fraction fraction = new Fraction(0, 4);
        String expected = "0";
        String actual = properFormat.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormat_NegativeFraction() {
        Fraction fraction = new Fraction(-3, 2);
        String expected = "-1 1/2";
        String actual = properFormat.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_ValidWholeFraction() {
        String fractionString = "2 3/4";
        Fraction expected = new Fraction(11, 4);
        Fraction actual = properFormat.parse(fractionString);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_ValidImproperFraction() {
        String fractionString = "10/3";
        Fraction expected = new Fraction(10, 3);
        Fraction actual = properFormat.parse(fractionString);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_ValidWholeNumber() {
        String fractionString = "7";
        Fraction expected = new Fraction(7, 1);
        Fraction actual = properFormat.parse(fractionString);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_ValidZeroFraction() {
        String fractionString = "0";
        Fraction expected = new Fraction(0, 1);
        Fraction actual = properFormat.parse(fractionString);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_ValidNegativeFraction() {
        String fractionString = "-2 1/3";
        Fraction expected = new Fraction(-7, 3);
        Fraction actual = properFormat.parse(fractionString);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_InvalidFraction_NoWholePart() {
        String fractionString = "1/2";
        Fraction actual = properFormat.parse(fractionString);
        assertNull(actual);
    }
    
    @Test
    public void testParse_InvalidFraction_InvalidWholePart() {
        String fractionString = "- 1/2";
        Fraction actual = properFormat.parse(fractionString);
        assertNull(actual);
    }
    
    @Test
    public void testParse_InvalidFraction_InvalidNumerator() {
        String fractionString = "3/abc";
        Fraction actual = properFormat.parse(fractionString);
        assertNull(actual);
    }
    
    @Test
    public void testParse_InvalidFraction_InvalidDenominator() {
        String fractionString = "4/0";
        Fraction actual = properFormat.parse(fractionString);
        assertNull(actual);
    }
}