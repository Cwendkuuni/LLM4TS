import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        Fraction fraction = new Fraction(-3, 2);
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = properFractionFormat.format(fraction, buffer, null);
        assertNotNull(result);
        assertEquals("-1 1 / 2", result.toString());
        
        fraction = new Fraction(3, 2);
        buffer = new StringBuffer();
        result = properFractionFormat.format(fraction, buffer, null);
        assertNotNull(result);
        assertEquals("1 1 / 2", result.toString());
        
        fraction = new Fraction(0, 2);
        buffer = new StringBuffer();
        result = properFractionFormat.format(fraction, buffer, null);
        assertNotNull(result);
        assertEquals("0 / 2", result.toString());
    }
    
    @Test
    public void testParse() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("-1 1 / 2", pos);
        assertNotNull(fraction);
        assertEquals(-3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse("1 1 / 2", pos);
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse("0 / 2", pos);
        assertNotNull(fraction);
        assertEquals(0, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }
    
    @Test
    public void testSetWholeFormat() {
        NumberFormat format = NumberFormat.getNumberInstance();
        properFractionFormat.setWholeFormat(format);
        assertEquals(format, properFractionFormat.getWholeFormat());
    }
}