package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {
    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        Assert.assertEquals("3 / 4", properFractionFormat.format(new Fraction(3, 4), new StringBuffer(), new FieldPosition(0)).toString());
        Assert.assertEquals("1 / 2", properFractionFormat.format(new Fraction(1, 2), new StringBuffer(), new FieldPosition(0)).toString());
        Assert.assertEquals("5 / 3", properFractionFormat.format(new Fraction(5, 3), new StringBuffer(), new FieldPosition(0)).toString());
        Assert.assertEquals("2", properFractionFormat.format(new Fraction(2), new StringBuffer(), new FieldPosition(0)).toString());
    }

    @Test
    public void testGetWholeFormat() {
        Assert.assertNotNull(properFractionFormat.getWholeFormat());
    }

    @Test
    public void testParse() {
        ParsePosition pos = new ParsePosition(0);
        Assert.assertEquals(new Fraction(3, 4), properFractionFormat.parse("3 / 4", pos));
        pos = new ParsePosition(0);
        Assert.assertEquals(new Fraction(1, 2), properFractionFormat.parse("1 / 2", pos));
        pos = new ParsePosition(0);
        Assert.assertEquals(new Fraction(5, 3), properFractionFormat.parse("5 / 3", pos));
        pos = new ParsePosition(0);
        Assert.assertEquals(new Fraction(2), properFractionFormat.parse("2", pos));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        properFractionFormat.setWholeFormat(null);
    }
}