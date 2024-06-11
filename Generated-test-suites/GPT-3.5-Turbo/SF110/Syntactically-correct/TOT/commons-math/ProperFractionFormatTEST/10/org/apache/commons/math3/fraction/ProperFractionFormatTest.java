import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.MathParseException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
        assertNotNull(format.getWholeFormat());
        assertTrue(format.getWholeFormat() instanceof DecimalFormat);
        assertNotNull(format.getNumeratorFormat());
        assertTrue(format.getNumeratorFormat() instanceof DecimalFormat);
        assertNotNull(format.getDenominatorFormat());
        assertTrue(format.getDenominatorFormat() instanceof DecimalFormat);
    }

    @Test
    public void testConstructorWithFormat() {
        NumberFormat format = new DecimalFormat("0.00");
        ProperFractionFormat properFormat = new ProperFractionFormat(format);

        assertNotNull(properFormat);
        assertEquals(format, properFormat.getWholeFormat());
        assertEquals(format, properFormat.getNumeratorFormat());
        assertEquals(format, properFormat.getDenominatorFormat());
    }

    @Test
    public void testConstructorWithAllFormats() {
        NumberFormat wholeFormat = new DecimalFormat("0");
        NumberFormat numeratorFormat = new DecimalFormat("0.00");
        NumberFormat denominatorFormat = new DecimalFormat("0.0000");
        ProperFractionFormat properFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        assertNotNull(properFormat);
        assertEquals(wholeFormat, properFormat.getWholeFormat());
        assertEquals(numeratorFormat, properFormat.getNumeratorFormat());
        assertEquals(denominatorFormat, properFormat.getDenominatorFormat());
    }
    
    @Test
    public void testFormatWithWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer sb = new StringBuffer();
        Fraction fraction = new Fraction(5, 3);
        FieldPosition pos = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, sb, pos);
        assertEquals("1 2 / 3", result.toString());
        assertEquals(0, pos.getBeginIndex());
        assertEquals(6, pos.getEndIndex());
    }
    
    @Test
    public void testFormatWithoutWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer sb = new StringBuffer();
        Fraction fraction = new Fraction(2, 3);
        FieldPosition pos = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, sb, pos);
        assertEquals("2 / 3", result.toString());
        assertEquals(0, pos.getBeginIndex());
        assertEquals(5, pos.getEndIndex());
    }
    
    @Test
    public void testFormatWithNegativeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer sb = new StringBuffer();
        Fraction fraction = new Fraction(-2, 3);
        FieldPosition pos = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, sb, pos);
        assertEquals("-2 / 3", result.toString());
        assertEquals(0, pos.getBeginIndex());
        assertEquals(6, pos.getEndIndex());
    }
    
    @Test
    public void testParseWithValidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result1 = format.parse("3 2 / 5", pos);
        assertEquals(5, result1.getDenominator());
        assertEquals(17, result1.getNumerator());

        Fraction result2 = format.parse("1 / 2", pos);
        assertEquals(2, result2.getDenominator());
        assertEquals(1, result2.getNumerator());
        
        Fraction result3 = format.parse("0 / 1", pos);
        assertEquals(1, result3.getDenominator());
        assertEquals(0, result3.getNumerator());
    }
    
    @Test
    public void testParseWithInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result1 = format.parse("1 1 2 / 3", pos);
        assertNull(result1);
        assertEquals(0, pos.getIndex());
        
        Fraction result2 = format.parse("1 -- / 2", pos);
        assertNull(result2);
        assertEquals(0, pos.getIndex());
        
        Fraction result3 = format.parse("-1 / -2", pos);
        assertNull(result3);
        assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testParseWithInvalidWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result1 = format.parse("1.5 1 / 3", pos);
        assertNull(result1);
        assertEquals(0, pos.getIndex());

        Fraction result2 = format.parse("a / 2", pos);
        assertNull(result2);
        assertEquals(0, pos.getIndex());
        
        Fraction result3 = format.parse("", pos);
        assertNull(result3);
        assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat newFormat = new DecimalFormat("0.0");
        
        format.setWholeFormat(newFormat);
        assertEquals(newFormat, format.getWholeFormat());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}