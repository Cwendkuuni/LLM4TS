package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction1 = new Fraction(1,2);
        Fraction fraction2 = new Fraction(3,4);
        Fraction fraction3 = new Fraction(-5,8);
        
        assertEquals("1 / 2", format.format(fraction1));
        assertEquals("3 / 4", format.format(fraction2));
        assertEquals("-5 / 8", format.format(fraction3));
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        String str1 = "1 / 2";
        String str2 = "3 / 4";
        String str3 = "-5 / 8";
        
        Fraction expected1 = new Fraction(1, 2);
        Fraction expected2 = new Fraction(3, 4);
        Fraction expected3 = new Fraction(-5, 8);
        
        assertEquals(expected1, format.parse(str1));
        assertEquals(expected2, format.parse(str2));
        assertEquals(expected3, format.parse(str3));
    }
}