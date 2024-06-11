package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test 1
        Fraction fraction1 = new Fraction(3, 4);
        String expectedOutput1 = "3 / 4";
        assertEquals(expectedOutput1, format.format(fraction1).toString());
        
        // Test 2
        Fraction fraction2 = new Fraction(-2, 7);
        String expectedOutput2 = "-2 / 7";
        assertEquals(expectedOutput2, format.format(fraction2).toString());

        // Test 3
        Fraction fraction3 = new Fraction(0, 5);
        String expectedOutput3 = "0 / 1";
        assertEquals(expectedOutput3, format.format(fraction3).toString());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test 1
        String input1 = "3 / 4";
        Fraction expectedOutput1 = new Fraction(3, 4);
        assertEquals(expectedOutput1, format.parse(input1));
        
        // Test 2
        String input2 = "-2 / 7";
        Fraction expectedOutput2 = new Fraction(-2, 7);
        assertEquals(expectedOutput2, format.parse(input2));
        
        // Test 3
        String input3 = "0 / 1";
        Fraction expectedOutput3 = new Fraction(0, 1);
        assertEquals(expectedOutput3, format.parse(input3));
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format.getWholeFormat());
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}