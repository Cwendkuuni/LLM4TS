package org.apache.commons.math.fraction;
import org.junit.Assert;
import org.junit.Test;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Test
    public void testFormat() {
        properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer toAppendTo = new StringBuffer();
        properFractionFormat.format(fraction, toAppendTo, null);
        Assert.assertEquals("1 1/2", toAppendTo.toString());
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        properFractionFormat = new ProperFractionFormat(numberFormat);
        Assert.assertEquals(numberFormat, properFractionFormat.getWholeFormat());
    }

    @Test
    public void testParse() {
        properFractionFormat = new ProperFractionFormat();
        String source = "1 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        Assert.assertEquals(3, fraction.getNumerator());
        Assert.assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        properFractionFormat = new ProperFractionFormat();
        properFractionFormat.setWholeFormat(numberFormat);
        Assert.assertEquals(numberFormat, properFractionFormat.getWholeFormat());
    }

}