package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test case 1: fraction = 3/4
        Fraction fraction1 = new Fraction(3, 4);
        String expected1 = "3 / 4";
        String actual1 = format.format(fraction1);
        assertEquals(expected1, actual1);
        
        // Test case 2: fraction = 7/2
        Fraction fraction2 = new Fraction(7, 2);
        String expected2 = "3 1 / 2";
        String actual2 = format.format(fraction2);
        assertEquals(expected2, actual2);
        
        // Test case 3: fraction = -5/3
        Fraction fraction3 = new Fraction(-5, 3);
        String expected3 = "-1 2 / 3";
        String actual3 = format.format(fraction3);
        assertEquals(expected3, actual3);
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test case 1: source = "3 / 4"
        String source1 = "3 / 4";
        Fraction expected1 = new Fraction(3, 4);
        Fraction actual1 = format.parse(source1);
        assertEquals(expected1, actual1);
        
        // Test case 2: source = "3 1 / 2"
        String source2 = "3 1 / 2";
        Fraction expected2 = new Fraction(7, 2);
        Fraction actual2 = format.parse(source2);
        assertEquals(expected2, actual2);
        
        // Test case 3: source = "-1 2 / 3"
        String source3 = "-1 2 / 3";
        Fraction expected3 = new Fraction(-5, 3);
        Fraction actual3 = format.parse(source3);
        assertEquals(expected3, actual3);
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test case 1: default whole format
        NumberFormat expected1 = NumberFormat.getInstance();
        NumberFormat actual1 = format.getWholeFormat();
        assertEquals(expected1, actual1);
        
        // Test case 2: change whole format
        NumberFormat wholeFormat = NumberFormat.getPercentInstance();
        format.setWholeFormat(wholeFormat);
        NumberFormat expected2 = wholeFormat;
        NumberFormat actual2 = format.getWholeFormat();
        assertEquals(expected2, actual2);
    }
    
}