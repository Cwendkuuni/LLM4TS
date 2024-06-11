package org.apache.commons.math.fraction;

import org.junit.Test;
import org.junit.Assert;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer result = new StringBuffer();
        formatter.format(fraction, result, null);
        Assert.assertEquals("1 1/2", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = formatter.parse("1 1/2", pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(3, 2), fraction);
    }

    @Test
    public void testParseError() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = formatter.parse("1 1 / 2", pos);
        Assert.assertNull(fraction);
    }

    @Test
    public void testWholeFormat() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        NumberFormat format = new DecimalFormat("#.##");
        formatter.setWholeFormat(format);
        Assert.assertEquals(format, formatter.getWholeFormat());
    }

}