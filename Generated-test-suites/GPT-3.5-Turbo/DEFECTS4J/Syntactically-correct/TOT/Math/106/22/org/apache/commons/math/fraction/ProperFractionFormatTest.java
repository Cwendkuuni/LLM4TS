import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() throws Exception {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        // Typical use case
        Fraction fraction = new Fraction(3, 4);
        String expected = "3/4";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
        
        // Edge case: Whole number format enabled
        Fraction fraction2 = new Fraction(5, 2);
        String expected2 = "2 1/2";
        String actual2 = properFractionFormat.format(fraction2);
        assertEquals(expected2, actual2);
        
        // Error scenario: Fraction with negative sign not allowed
        Fraction fraction3 = new Fraction(-3, 4);
        String expected3 = null;
        String actual3 = properFractionFormat.format(fraction3);
        assertEquals(expected3, actual3);
    }
    
    @Test
    public void testParse() {
        // Typical use case
        String source = "2 3/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(11, 4);
        Fraction actual = properFractionFormat.parse(source, pos);
        assertNotNull(actual);
        assertEquals(expected, actual);
        
        // Edge case: Fraction without whole number
        String source2 = "1/2";
        ParsePosition pos2 = new ParsePosition(0);
        Fraction expected2 = new Fraction(1, 2);
        Fraction actual2 = properFractionFormat.parse(source2, pos2);
        assertNotNull(actual2);
        assertEquals(expected2, actual2);
        
        // Error scenario: Invalid proper fraction format
        String source3 = "-3/4";
        ParsePosition pos3 = new ParsePosition(0);
        Fraction expected3 = null;
        Fraction actual3 = properFractionFormat.parse(source3, pos3);
        assertEquals(expected3, actual3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormat_NullFormat() {
        NumberFormat format = null;
        properFractionFormat.setWholeFormat(format);
    }
    
    @Test
    public void testGetWholeFormat() {
        NumberFormat expected = NumberFormat.getInstance();
        properFractionFormat.setWholeFormat(expected);
        NumberFormat actual = properFractionFormat.getWholeFormat();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConstructor_DefaultNumberFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }
    
    @Test
    public void testConstructor_CustomNumberFormat() {
        NumberFormat expected = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(expected);
        assertNotNull(format);
        assertEquals(expected, format.getWholeFormat());
    }
    
    @Test
    public void testConstructor_CustomNumberFormats() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getCurrencyInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(format);
        assertEquals(wholeFormat, format.getWholeFormat());
        assertEquals(numeratorFormat, format.getNumeratorFormat());
        assertEquals(denominatorFormat, format.getDenominatorFormat());
    }
}