package org.apache.commons.math3.fraction;

import org.junit.Assert;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void format_WithPositiveWholeAndPositiveFraction_ReturnsFormattedString() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 5);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, position);
        
        String expected = "3 / 5";
        Assert.assertEquals(expected, result.toString());
    }
    
    @Test
    public void format_WithNegativeWholeAndPositiveFraction_ReturnsFormattedString() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-2, 3);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, position);
        
        String expected = "-2 / 3";
        Assert.assertEquals(expected, result.toString());
    }
    
    @Test
    public void format_WithZeroWholeAndPositiveFraction_ReturnsFormattedString() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, position);
        
        String expected = "0 / 4";
        Assert.assertEquals(expected, result.toString());
    }
    
    @Test
    public void format_WithPositiveWholeAndNegativeFraction_ReturnsFormattedString() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, -5);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, position);
        
        String expected = "2 / -5";
        Assert.assertEquals(expected, result.toString());
    }
    
    @Test
    public void format_WithZeroWholeAndNegativeFraction_ReturnsFormattedString() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, -3);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, position);
        
        String expected = "0 / -3";
        Assert.assertEquals(expected, result.toString());
    }
    
    @Test
    public void format_WithPositiveWholeAndZeroFraction_ReturnsFormattedString() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(4, 0);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, position);
        
        String expected = "4 / 0";
        Assert.assertEquals(expected, result.toString());
    }
    
    @Test
    public void format_WithZeroWholeAndZeroFraction_ReturnsFormattedString() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 0);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, position);
        
        String expected = "0 / 0";
        Assert.assertEquals(expected, result.toString());
    }
    
    @Test
    public void parse_WithValidFractionString_ReturnsParsedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "3 / 5";
        ParsePosition position = new ParsePosition(0);
        
        Fraction result = format.parse(fractionString, position);
        Fraction expected = new Fraction(3, 5);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void parse_WithInvalidFractionString_ReturnsNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "3/5";
        ParsePosition position = new ParsePosition(0);
        
        Fraction result = format.parse(fractionString, position);
        
        Assert.assertEquals(null, result);
    }
    
    @Test
    public void parse_WithFractionStringContainingInvalidCharacters_ReturnsNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "3 / 5a";
        ParsePosition position = new ParsePosition(0);
        
        Fraction result = format.parse(fractionString, position);
        
        Assert.assertEquals(null, result);
    }
    
    @Test
    public void parse_WithFractionStringContainingNegativeWhole_ReturnsParsedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "-2 / 3";
        ParsePosition position = new ParsePosition(0);
        
        Fraction result = format.parse(fractionString, position);
        Fraction expected = new Fraction(-2, 3);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void parse_WithFractionStringContainingZeroWhole_ReturnsParsedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "0 / 4";
        ParsePosition position = new ParsePosition(0);
        
        Fraction result = format.parse(fractionString, position);
        Fraction expected = new Fraction(0, 4);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void parse_WithInvalidWhole_ReturnsNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "a / 3";
        ParsePosition position = new ParsePosition(0);
        
        Fraction result = format.parse(fractionString, position);
        
        Assert.assertEquals(null, result);
    }
    
    @Test
    public void parse_WithZeroDenominator_ReturnsNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "2 / 0";
        ParsePosition position = new ParsePosition(0);
        
        Fraction result = format.parse(fractionString, position);
        
        Assert.assertEquals(null, result);
    }
    
    @Test
    public void parse_WithNegativeDenominator_ReturnsNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "2 / -5";
        ParsePosition position = new ParsePosition(0);
        
        Fraction result = format.parse(fractionString, position);
        
        Assert.assertEquals(null, result);
    }
    
    @Test
    public void parse_WithInvalidDenominator_ReturnsNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "2 / a";
        ParsePosition position = new ParsePosition(0);
        
        Fraction result = format.parse(fractionString, position);
        
        Assert.assertEquals(null, result);
    }
}