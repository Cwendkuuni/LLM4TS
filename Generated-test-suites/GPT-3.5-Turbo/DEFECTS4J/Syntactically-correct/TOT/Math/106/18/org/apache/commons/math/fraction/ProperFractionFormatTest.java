import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.NumberFormat;
import java.text.ParseException;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat format;
    
    @Before
    public void setup() {
        format = new ProperFractionFormat();
    }
    
    // Test format(Fraction fraction, StringBuffer toAppendTo, FieldPosition pos) method
    
    @Test
    public void testFormat_PositiveFraction_ReturnsProperFormatString() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, null);
        assertEquals("3/4", result.toString());
    }
    
    @Test
    public void testFormat_NegativeFraction_ReturnsProperFormatString() {
        Fraction fraction = new Fraction(-1, 2);
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, null);
        assertEquals("-1/2", result.toString());
    }
    
    @Test
    public void testFormat_FractionWithWholeNumber_ReturnsProperFormatString() {
        Fraction fraction = new Fraction(5, 2);
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, null);
        assertEquals("2 1/2", result.toString());
    }
    
    @Test
    public void testFormat_FractionWithZero_ReturnsProperFormatString() {
        Fraction fraction = new Fraction(0);
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, null);
        assertEquals("0", result.toString());
    }
    
    // Test getWholeFormat() method
    
    @Test
    public void testGetWholeFormat_InitializedWithDefaultNumberFormat_ReturnsDefaultNumberFormat() {
        NumberFormat defaultFormat = ProperFractionFormat.getDefaultNumberFormat();
        assertEquals(defaultFormat, format.getWholeFormat());
    }
    
    // Test getNumeratorFormat() and getDenominatorFormat() methods
    
    @Test
    public void testGetNumeratorFormat_InitializedWithDefaultNumberFormat_ReturnsDefaultNumberFormat() {
        NumberFormat defaultFormat = ProperFractionFormat.getDefaultNumberFormat();
        assertEquals(defaultFormat, format.getNumeratorFormat());
    }
    
    @Test
    public void testGetDenominatorFormat_InitializedWithDefaultNumberFormat_ReturnsDefaultNumberFormat() {
        NumberFormat defaultFormat = ProperFractionFormat.getDefaultNumberFormat();
        assertEquals(defaultFormat, format.getDenominatorFormat());
    }
    
    // Test parse(String source, ParsePosition pos) method
    
    @Test
    public void testParse_ValidProperFraction_ReturnsFractionObject() {
        String source = "2 3/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(11, 4), result);
        assertEquals(7, pos.getIndex());
        assertNull(pos.getErrorIndex());
    }
    
    @Test
    public void testParse_ValidImproperFraction_ReturnsFractionObject() {
        String source = "5/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(5, 2), result);
        assertEquals(3, pos.getIndex());
        assertNull(pos.getErrorIndex());
    }
    
    @Test
    public void testParse_ValidNegativeFraction_ReturnsFractionObject() {
        String source = "-1/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(-1, 3), result);
        assertEquals(4, pos.getIndex());
        assertNull(pos.getErrorIndex());
    }
    
    @Test
    public void testParse_ValidWholeNumber_ReturnsFractionObject() {
        String source = "7";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(7, 1), result);
        assertEquals(1, pos.getIndex());
        assertNull(pos.getErrorIndex());
    }
    
    @Test
    public void testParse_InvalidFractionWithTwoWholeNumbers_ReturnsNull() {
        String source = "-3 -1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertNull(result);
        assertEquals(1, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }
    
    @Test
    public void testParse_NonNumericCharacter_ReturnsNull() {
        String source = "abc";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }
    
    @Test
    public void testParse_InvalidFractionWithInvalidDenominator_ReturnsNull() {
        String source = "1/0";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertNull(result);
        assertEquals(1, pos.getIndex());
        assertEquals(2, pos.getErrorIndex());
    }
    
    // Test setWholeFormat(NumberFormat format) method
    
    @Test
    public void testSetWholeFormat_ValidNumberFormat_SetsWholeFormat() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        this.format.setWholeFormat(format);
        assertEquals(format, this.format.getWholeFormat());
    }
    
}