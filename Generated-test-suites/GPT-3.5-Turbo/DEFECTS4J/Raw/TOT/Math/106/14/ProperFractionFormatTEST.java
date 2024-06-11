import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;
import java.text.ParseException;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    // Test for format(Fraction, StringBuffer, FieldPosition)

    @Test
    public void testFormat_fractionWithWholeNumber() {
        Fraction fraction = new Fraction(7, 2);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("3 1/2", buffer.toString());
    }

    @Test
    public void testFormat_fractionWithZeroWholeNumber() {
        Fraction fraction = new Fraction(1, 2);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("1/2", buffer.toString());
    }

    // Test for parse(String, ParsePosition)
    
    @Test
     public void testParse_validFractionWithWholeNumber() {
         String source = "3 1/2";
         ParsePosition pos = new ParsePosition(0);
         Fraction fraction = properFractionFormat.parse(source, pos);
         assertEquals(new Fraction(7, 2), fraction);
         assertEquals(source.length(), pos.getIndex());
     }

    @Test
    public void testParse_validFractionWithZeroWholeNumber() {
        String source = "0 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(1, 2), fraction);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testParse_validFractionWithoutWholeNumber() {
        String source = "1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(1, 2), fraction);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testParse_invalidFractionWithNegativeWholeNumber() {
        String source = "-3 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(1, pos.getErrorIndex());
    }

    @Test
    public void testParse_invalidFractionWithNegativeNumerators() {
        String source = "3 -1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(3, pos.getErrorIndex());
    }

    @Test
    public void testParse_invalidFractionWithoutNumerator() {
        String source = "3 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(3, pos.getErrorIndex());
    }

    @Test
    public void testParse_invalidFractionWithoutDenominator() {
        String source = "3 1/ ";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(4, pos.getErrorIndex());
    }

    @Test
    public void testParse_invalidFractionWithInvalidSeparator() {
        String source = "3 1,2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(2, pos.getErrorIndex());
    }

    // Additional tests for setWholeFormat(NumberFormat)

    @Test
    public void testSetWholeFormat_validWholeFormat() {
        NumberFormat format = NumberFormat.getInstance();
        properFractionFormat.setWholeFormat(format);
        assertEquals(format, properFractionFormat.getWholeFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormat_nullWholeFormat() {
        properFractionFormat.setWholeFormat(null);
    }

}