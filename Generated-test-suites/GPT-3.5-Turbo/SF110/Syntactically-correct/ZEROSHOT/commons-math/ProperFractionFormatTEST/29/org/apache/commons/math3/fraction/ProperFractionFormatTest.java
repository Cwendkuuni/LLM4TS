
package org.apache.commons.math3.fraction;

import org.junit.Assert;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.FieldPosition;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test with positive fraction
        Fraction fraction = new Fraction(3, 5);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, pos);

        Assert.assertEquals("0 3 / 5", result.toString());
        Assert.assertEquals(2, pos.getBeginIndex());
        Assert.assertEquals(5, pos.getEndIndex());

        // Test with negative fraction
        fraction = new Fraction(-1, 4);
        buffer = new StringBuffer();
        pos = new FieldPosition(0);
        result = format.format(fraction, buffer, pos);

        Assert.assertEquals("-1 / 4", result.toString());
        Assert.assertEquals(0, pos.getBeginIndex());
        Assert.assertEquals(7, pos.getEndIndex());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();

        Assert.assertNotNull(wholeFormat);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);

        Assert.assertNotNull(fraction);
        Assert.assertEquals(11, pos.getIndex());
        Assert.assertEquals(3, fraction.getNumerator());
        Assert.assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        format.setWholeFormat(wholeFormat);

        Assert.assertEquals(wholeFormat, format.getWholeFormat());
    }
}
