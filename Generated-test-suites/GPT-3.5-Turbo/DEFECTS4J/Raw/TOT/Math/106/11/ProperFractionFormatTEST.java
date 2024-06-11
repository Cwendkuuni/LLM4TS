import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(format);
        
        Fraction fraction = new Fraction(3, 5);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = properFractionFormat.format(fraction, buffer, position);
        
        assertEquals("3 / 5", result.toString());
        assertEquals(3, position.getBeginIndex());
        assertEquals(9, position.getEndIndex());
    }
    
    @Test
    public void testFormat_WithNegativeFraction() {
        Fraction fraction = new Fraction(-3, 5);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = properFractionFormat.format(fraction, buffer, position);
        
        assertEquals("-3 / 5", result.toString());
        assertEquals(0, position.getBeginIndex());
        assertEquals(9, position.getEndIndex());
    }
    
    @Test
    public void testFormat_WithZeroNumerator() {
        Fraction fraction = new Fraction(0, 5);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = properFractionFormat.format(fraction, buffer, position);
        
        assertEquals("0 / 5", result.toString());
        assertEquals(0, position.getBeginIndex());
        assertEquals(5, position.getEndIndex());
    }
    
    @Test
    public void testParse_WithValidString() {
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = properFractionFormat.parse("3 / 5", pos);
        
        assertEquals(new Fraction(3, 5), result);
        assertEquals(6, pos.getIndex());
    }
    
    @Test
    public void testParse_WithWhitespaceString() {
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = properFractionFormat.parse("   3   /  5   ", pos);
        
        assertEquals(new Fraction(3, 5), result);
        assertEquals(13, pos.getIndex());
    }
    
    @Test
    public void testParse_WithNegativeFractionString() {
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = properFractionFormat.parse("-3 / 5", pos);
        
        assertEquals(new Fraction(-3, 5), result);
        assertEquals(7, pos.getIndex());
    }
    
    @Test
    public void testParse_WithInvalidString() {
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = properFractionFormat.parse("3 - 5", pos);
        
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(2, pos.getErrorIndex());
    }
    
    @Test
    public void testParse_WithZeroNumeratorString() {
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = properFractionFormat.parse("0 / 5", pos);
        
        assertEquals(new Fraction(0, 5), result);
        assertEquals(5, pos.getIndex());
    }
    
    @Test
    public void testParse_WithZeroDenominatorString() {
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = properFractionFormat.parse("3 / 0", pos);
        
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(4, pos.getErrorIndex());
    }
    
    @Test
    public void testParse_WithInvalidWholeString() {
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = properFractionFormat.parse("3a / 5", pos);
        
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(1, pos.getErrorIndex());
    }
    
    @Test
    public void testParse_WithInvalidNumeratorString() {
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = properFractionFormat.parse("3 / 5a", pos);
        
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(5, pos.getErrorIndex());
    }
    
    @Test
    public void testParse_WithInvalidDenominatorString() {
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = properFractionFormat.parse("3 / a5", pos);
        
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(4, pos.getErrorIndex());
    }
    
    @Test
    public void testSetWholeFormat_WithNullFormat() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(format);
        
        try {
            properFractionFormat.setWholeFormat(null);
        } catch (IllegalArgumentException e) {
            assertEquals("whole format can not be null.", e.getMessage());
            return;
        }
        
        fail("Expected IllegalArgumentException");
    }
}