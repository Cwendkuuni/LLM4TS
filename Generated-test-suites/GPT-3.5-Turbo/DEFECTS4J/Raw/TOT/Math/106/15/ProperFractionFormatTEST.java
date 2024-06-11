import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat format;
    
    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat_WholeNumber() {
        Fraction fraction = new Fraction(4);
        String expected = "4";
        
        String result = format.format(fraction);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormat_ImproperFraction() {
        Fraction fraction = new Fraction(7, 2);
        String expected = "3 1 / 2";
        
        String result = format.format(fraction);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormat_NegativeFraction() {
        Fraction fraction = new Fraction(-5, 2);
        String expected = "-2 1 / 2";
        
        String result = format.format(fraction);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormat_ZeroDenominator() {
        Fraction fraction = new Fraction(3, 0);
        String expected = "3 / 0";
        
        String result = format.format(fraction);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse_ValidWholeNumber() {
        String source = "3";
        Fraction expected = new Fraction(3);
        
        Fraction result = format.parse(source);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse_ValidImproperFraction() {
        String source = "3 1 / 2";
        Fraction expected = new Fraction(7, 2);
        
        Fraction result = format.parse(source);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse_ValidNegativeFraction() {
        String source = "-2 1 / 2";
        Fraction expected = new Fraction(-5, 2);
        
        Fraction result = format.parse(source);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse_InvalidExpression() {
        String source = "-3 - 1 / 2";
        Fraction expected = null;
        
        Fraction result = format.parse(source);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse_InvalidNumber() {
        String source = "2 af / 3";
        Fraction expected = null;
        
        Fraction result = format.parse(source);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse_InvalidDenominator() {
        String source = "1 / af";
        Fraction expected = null;
        
        Fraction result = format.parse(source);
        
        assertEquals(expected, result);
    }
}