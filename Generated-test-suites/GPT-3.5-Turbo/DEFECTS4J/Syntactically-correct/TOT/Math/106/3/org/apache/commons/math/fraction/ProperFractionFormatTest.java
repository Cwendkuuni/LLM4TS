import org.junit.Test;
import static org.junit.Assert.*;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.util.MathUtils;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFractionFormat;
    
    public ProperFractionFormatTest() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat_positiveFraction() {
        Fraction fraction = new Fraction(2, 3);
        String expected = "2 / 3";
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        properFractionFormat.format(fraction, buffer, fieldPosition);
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    public void testFormat_negativeFraction() {
        Fraction fraction = new Fraction(-5, 7);
        String expected = "-5 / 7";
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        properFractionFormat.format(fraction, buffer, fieldPosition);
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    public void testFormat_mixedFraction() {
        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1/2";
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        properFractionFormat.format(fraction, buffer, fieldPosition);
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    public void testFormat_zeroNumerator() {
        Fraction fraction = new Fraction(0, 5);
        String expected = "0";
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        properFractionFormat.format(fraction, buffer, fieldPosition);
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    public void testFormat_zeroDenominator() {
        Fraction fraction = new Fraction(6, 0);
        String expected = "Infinity";
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        properFractionFormat.format(fraction, buffer, fieldPosition);
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        NumberFormat numberFormat = properFractionFormat.getWholeFormat();
        assertNotNull(numberFormat);
    }
    
    @Test
    public void testGetWholeFormat() {
        NumberFormat numberFormat = properFractionFormat.getWholeFormat();
        assertNotNull(numberFormat);
    }
    
    @Test
    public void testParse_validFraction() {
        String source = "3 1/4";
        Fraction expected = new Fraction(13, 4);
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, parsePosition);
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse_invalidFraction() {
        String source = "3 -1/4";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, parsePosition);
        assertNull(result);
        int errorIndex = parsePosition.getErrorIndex();
        assertEquals(2, errorIndex);
    }
    
    @Test
    public void testParse_justNumerator() {
        String source = "7";
        Fraction expected = new Fraction(7, 1);
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, parsePosition);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSetWholeFormat() {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        properFractionFormat.setWholeFormat(numberFormat);
        NumberFormat result = properFractionFormat.getWholeFormat();
        assertEquals(numberFormat, result);
    }
}