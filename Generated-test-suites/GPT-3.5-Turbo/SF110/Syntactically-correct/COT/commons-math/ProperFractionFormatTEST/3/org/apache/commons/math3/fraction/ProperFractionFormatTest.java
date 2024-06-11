
package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import java.text.NumberFormat;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat format;
    
    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        assertEquals(expected, format.format(fraction));
    }
    
    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        format.setWholeFormat(null);
    }
    
    @Test
    public void testParse() {
        String source = "5 / 8";
        Fraction expected = new Fraction(5, 8);
        assertEquals(expected, format.parse(source));
    }
    
    @Test
    public void testParseInvalidSource() {
        String source = "5.8";
        Fraction result = format.parse(source);
        assertNull(result);
    }
    
    @Test
    public void testParseInvalidFraction() {
        String source = "5 / -8";
        Fraction result = format.parse(source);
        assertNull(result);
    }
}
