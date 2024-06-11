package org.apache.commons.math3.fraction;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction fraction1 = new Fraction(3, 2);
        assertEquals("1 1 / 2", format.format(fraction1).toString());
        
        Fraction fraction2 = new Fraction(5, 4);
        assertEquals("1 1 / 4", format.format(fraction2).toString());
        
        Fraction fraction3 = new Fraction(7, 3);
        assertEquals("2 1 / 3", format.format(fraction3).toString());
    }

    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction fraction1 = format.parse("1 1 / 2");
        assertEquals(3, fraction1.getNumerator());
        assertEquals(2, fraction1.getDenominator());
        
        Fraction fraction2 = format.parse("1 1 / 4");
        assertEquals(5, fraction2.getNumerator());
        assertEquals(4, fraction2.getDenominator());
        
        Fraction fraction3 = format.parse("2 1 / 3");
        assertEquals(7, fraction3.getNumerator());
        assertEquals(3, fraction3.getDenominator());
    }

    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format.getWholeFormat());
    }

    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(NumberFormat.getInstance());
        
        assertNotNull(format.getWholeFormat());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}