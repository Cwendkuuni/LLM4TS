package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat format;

    public ProperFractionFormatTest() {
        format = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(-4, 5);
        Fraction fraction3 = new Fraction(0, 7);

        StringBuffer sb1 = format.format(fraction1, new StringBuffer(), new FieldPosition(0));
        StringBuffer sb2 = format.format(fraction2, new StringBuffer(), new FieldPosition(0));
        StringBuffer sb3 = format.format(fraction3, new StringBuffer(), new FieldPosition(0));

        assertEquals("2 / 3", sb1.toString());
        assertEquals("-4 / 5", sb2.toString());
        assertEquals("0 / 7", sb3.toString());
    }

    @Test
    public void testParse() {
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction1 = format.parse("2 / 3", pos);
        Fraction fraction2 = format.parse("-4 / 5", pos);
        Fraction fraction3 = format.parse("0 / 7", pos);
        Fraction fraction4 = format.parse("5", pos);

        assertEquals(new Fraction(2, 3), fraction1);
        assertEquals(new Fraction(-4, 5), fraction2);
        assertEquals(new Fraction(0, 7), fraction3);
        assertEquals(new Fraction(5, 1), fraction4);
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat nf = NumberFormat.getInstance();
        format.setWholeFormat(nf);

        assertEquals(nf, format.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        format.setWholeFormat(null);
    }

}