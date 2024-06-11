package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction fraction1 = new Fraction(1, 2);
        assertEquals("1 / 2", format.format(fraction1));
        
        Fraction fraction2 = new Fraction(3, 4);
        assertEquals("3 / 4", format.format(fraction2));
        
        Fraction fraction3 = new Fraction(5, 6);
        assertEquals("5 / 6", format.format(fraction3));
        
        Fraction fraction4 = new Fraction(7, 8);
        assertEquals("7 / 8", format.format(fraction4));
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction fraction1 = format.parse("1 / 2");
        assertEquals(new Fraction(1, 2), fraction1);
        
        Fraction fraction2 = format.parse("3 / 4");
        assertEquals(new Fraction(3, 4), fraction2);
        
        Fraction fraction3 = format.parse("5 / 6");
        assertEquals(new Fraction(5, 6), fraction3);
        
        Fraction fraction4 = format.parse("7 / 8");
        assertEquals(new Fraction(7, 8), fraction4);
    }
    
    @Test
    public void testParseInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction fraction1 = format.parse("1 /");
        assertNull(fraction1);
        
        Fraction fraction2 = format.parse("3 / -4");
        assertNull(fraction2);
        
        Fraction fraction3 = format.parse("/ 6");
        assertNull(fraction3);
    }
}