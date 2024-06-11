package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private static final double DELTA = 1e-15;
    private ProperFractionFormat format;
    
    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 2);
        String expectedResult = "1 1 / 2";
        assertEquals(expectedResult, format.format(fraction));
        
        fraction = new Fraction(5, 4);
        expectedResult = "1 1 / 4";
        assertEquals(expectedResult, format.format(fraction));
        
        fraction = new Fraction(7, 3);
        expectedResult = "2 1 / 3";
        assertEquals(expectedResult, format.format(fraction));
        
        fraction = new Fraction(-3, 2);
        expectedResult = "-1 1 / 2";
        assertEquals(expectedResult, format.format(fraction));
        
        fraction = new Fraction(-7, 3);
        expectedResult = "-2 1 / 3";
        assertEquals(expectedResult, format.format(fraction));
    }
    
    @Test
    public void testParse() {
        String source = "1 1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction expectedFraction = new Fraction(3, 2);
        Fraction parsedFraction = format.parse(source, pos);
        assertNotNull(parsedFraction);
        assertEquals(expectedFraction.getNumerator(), parsedFraction.getNumerator());
        assertEquals(expectedFraction.getDenominator(), parsedFraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
        
        source = "1 1 / 4";
        pos = new ParsePosition(0);
        expectedFraction = new Fraction(5, 4);
        parsedFraction = format.parse(source, pos);
        assertNotNull(parsedFraction);
        assertEquals(expectedFraction.getNumerator(), parsedFraction.getNumerator());
        assertEquals(expectedFraction.getDenominator(), parsedFraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
        
        source = "2 1 / 3";
        pos = new ParsePosition(0);
        expectedFraction = new Fraction(7, 3);
        parsedFraction = format.parse(source, pos);
        assertNotNull(parsedFraction);
        assertEquals(expectedFraction.getNumerator(), parsedFraction.getNumerator());
        assertEquals(expectedFraction.getDenominator(), parsedFraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
        
        source = "-1 1 / 2";
        pos = new ParsePosition(0);
        expectedFraction = new Fraction(-3, 2);
        parsedFraction = format.parse(source, pos);
        assertNotNull(parsedFraction);
        assertEquals(expectedFraction.getNumerator(), parsedFraction.getNumerator());
        assertEquals(expectedFraction.getDenominator(), parsedFraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
        
        source = "-2 1 / 3";
        pos = new ParsePosition(0);
        expectedFraction = new Fraction(-7, 3);
        parsedFraction = format.parse(source, pos);
        assertNotNull(parsedFraction);
        assertEquals(expectedFraction.getNumerator(), parsedFraction.getNumerator());
        assertEquals(expectedFraction.getDenominator(), parsedFraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
        
        source = "3 / 4";
        pos = new ParsePosition(0);
        expectedFraction = new Fraction(3, 4);
        parsedFraction = format.parse(source, pos);
        assertNull(parsedFraction);
        assertEquals(0, pos.getIndex());
        assertEquals(4, pos.getErrorIndex());
        
        source = "-3 -1 / 2";
        pos = new ParsePosition(0);
        parsedFraction = format.parse(source, pos);
        assertNull(parsedFraction);
        assertEquals(0, pos.getIndex());
        assertEquals(6, pos.getErrorIndex());
    }
    
    @Test
    public void testWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}