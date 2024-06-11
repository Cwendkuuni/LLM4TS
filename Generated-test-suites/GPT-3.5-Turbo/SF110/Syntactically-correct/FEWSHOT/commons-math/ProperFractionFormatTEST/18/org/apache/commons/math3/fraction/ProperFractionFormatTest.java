package org.apache.commons.math3.fraction;

import org.junit.Assert;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testConstructor1() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Assert.assertNotNull(format.getWholeFormat());
        Assert.assertNotNull(format.getNumeratorFormat());
        Assert.assertNotNull(format.getDenominatorFormat());
    }
    
    @Test
    public void testConstructor2() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat fractionFormat = new ProperFractionFormat(format);
        
        Assert.assertEquals(format, fractionFormat.getWholeFormat());
        Assert.assertEquals(format, fractionFormat.getNumeratorFormat());
        Assert.assertEquals(format, fractionFormat.getDenominatorFormat());
    }
    
    @Test
    public void testConstructor3() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getCurrencyInstance();
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance();
        
        ProperFractionFormat fractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        
        Assert.assertEquals(wholeFormat, fractionFormat.getWholeFormat());
        Assert.assertEquals(numeratorFormat, fractionFormat.getNumeratorFormat());
        Assert.assertEquals(denominatorFormat, fractionFormat.getDenominatorFormat());
    }
    
    @Test
    public void testFormat() {
        ProperFractionFormat fractionFormat = new ProperFractionFormat();
        
        Fraction fraction = new Fraction(1, 2);
        
        Assert.assertEquals("1 / 2", fractionFormat.format(fraction));
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat fractionFormat = new ProperFractionFormat();
        
        Fraction expectedFraction = new Fraction(3, 4);
        Fraction actualFraction = fractionFormat.parse("3 / 4");
        
        Assert.assertEquals(expectedFraction, actualFraction);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat fractionFormat = new ProperFractionFormat();
        
        NumberFormat format = NumberFormat.getInstance();
        fractionFormat.setWholeFormat(format);
        
        Assert.assertEquals(format, fractionFormat.getWholeFormat());
    }
}