
package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer result = format.format(new Fraction(1, 2), new StringBuffer(), null);
        assertEquals("1 / 2", result.toString());
        
        result = format.format(new Fraction(3, 4), new StringBuffer(), null);
        assertEquals("3 / 4", result.toString());
        
        result = format.format(new Fraction(5, 6), new StringBuffer(), null);
        assertEquals("5 / 6", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("1 / 2", null);
        assertEquals(new Fraction(1, 2), result);
        
        result = format.parse("3 / 4", null);
        assertEquals(new Fraction(3, 4), result);
        
        result = format.parse("5 / 6", null);
        assertEquals(new Fraction(5, 6), result);
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat result = format.getWholeFormat();
        assertNotNull(result);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(0);
        format.setMinimumIntegerDigits(2);
        format.setMaximumIntegerDigits(2);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerOnly(true);
        format.setParseIntegerField(4);
        format.setParseIntegerField(7);
        format.setParseIntegerField(10);
        
        format.setWholeFormat(format);
        assertEquals(format, format.getWholeFormat());
    }
    
}
