package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testFormatProperFraction() {
        ProperFractionFormat pff = new ProperFractionFormat();

        Fraction f1 = new Fraction(1, 2);
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        pff.format(f1, sb, pos);
        assertEquals("-1/2", sb.toString());

        pff = new ProperFractionFormat(NumberFormat.getNumberInstance());
        sb = new StringBuffer();
        pos = new FieldPosition(0, 0);

        pff.format(f1, sb, pos);
        assertEquals("0 - 1/2", sb.toString());

        pff = new ProperFractionFormat(NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());
        sb = new StringBuffer();
        pos = new FieldPosition(0, 0);

        pff.format(f1, sb, pos);
        assertEquals("0 - 1/2", sb.toString());
    }

    @Test
    public void testFormatImproperFraction() {
        ProperFractionFormat pff = new ProperFractionFormat(NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());

        Fraction f1 = new Fraction(5, 2);
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        pff.format(f1, sb, pos);
        assertEquals("2 1/2", sb.toString());
    }

    @Test
    public void testFormatWholeNumber() {
        ProperFractionFormat pff = new ProperFractionFormat();

        Fraction f1 = new Fraction(1, 1);
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        pff.format(f1, sb, pos);
        assertEquals("1", sb.toString());

        pff = new ProperFractionFormat(NumberFormat.getNumberInstance());
        sb = new StringBuffer();
        pos = new FieldPosition(0, 0);

        pff.format(f1, sb, pos);
        assertEquals("1", sb.toString());

        pff = new ProperFractionFormat(NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());
        sb = new StringBuffer();
        pos = new FieldPosition(0, 0);

        pff.format(f1, sb, pos);
        assertEquals("1", sb.toString());
    }

    @Test
    public void testParseProperFraction() {
        ProperFractionFormat pff = new ProperFractionFormat();

        ParsePosition pp = new ParsePosition(0);
        Fraction f1 = pff.parse("-1/2", null, pp);
        assertEquals(new Fraction(-1, 2), f1);
        assertEquals(4, pp.getIndex());

        f1 = pff.parse("0 - 1/2", null, pp);
        assertEquals(new Fraction(-1, 2), f1);
        assertEquals(7, pp.getIndex());

        f1 = pff.parse("0 - 1/2", null, pp);
        assertEquals(null, f1);
        assertEquals(7, pp.getIndex());
        assertEquals(7, pp.getErrorIndex());

        pff = new ProperFractionFormat(NumberFormat.getNumberInstance());
        pp = new ParsePosition(0);

        Fraction f2 = pff.parse("0 - 1/2", null, pp);
        assertEquals(new Fraction(0, 1), f2);
        assertEquals(4, pp.getIndex());

        f2 = pff.parse("2 1/2", null, pp);
        assertEquals(new Fraction(5, 2), f2);
        assertEquals(6, pp.getIndex());

        f2 = pff.parse("-2 1/2", null, pp);
        assertEquals(new Fraction(-5, 2), f2);
        assertEquals(7, pp.getIndex());

        f2 = pff.parse("-2 1/2", null, pp);
        assertEquals(null, f2);
        assertEquals(7, pp.getIndex());
        assertEquals(7, pp.getErrorIndex());

        pff = new ProperFractionFormat(NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());
        pp = new ParsePosition(0);

        Fraction f3 = pff.parse("0 - 1/2", null, pp);
        assertEquals(new Fraction(0, 1), f3);
        assertEquals(4, pp.getIndex());

        f3 = pff.parse("2 1/2", null, pp);
        assertEquals(new Fraction(5, 2), f3);
        assertEquals(6, pp.getIndex());

        f3 = pff.parse("-2 1/2", null, pp);
        assertEquals(new Fraction(-5, 2), f3);
        assertEquals(7, pp.getIndex());

        f3 = pff.parse("-2 1/2", null, pp);
        assertEquals(null, f3);
        assertEquals(7, pp.getIndex());
        assertEquals(7, pp.getErrorIndex());
    }

    @Test
    public void testParseImproperFraction() {
        ProperFractionFormat pff = new ProperFractionFormat(NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());

        ParsePosition pp = new ParsePosition(0);
        Fraction f1 = pff.parse("2 1/2", null, pp);
        assertEquals(new Fraction(5, 2), f1);
        assertEquals(6, pp.getIndex());

        f1 = pff.parse("2", null, pp);
        assertEquals(new Fraction(2, 1), f1);
        assertEquals(3, pp.getIndex());

        f1 = pff.parse("2", null, pp);
        assertEquals(new Fraction(2, 1), f1);
        assertEquals(3, pp.getIndex());

        f1 = pff.parse("-2", null, pp);
        assertEquals(new Fraction(-2, 1), f1);
        assertEquals(4, pp.getIndex());

        f1 = pff.parse("-2", null, pp);
        assertEquals(new Fraction(-2, 1), f1);
        assertEquals(4, pp.getIndex());

        f1 = pff.parse("-2", null, pp);
        assertEquals(null, f1);
        assertEquals(4, pp.getIndex());
        assertEquals(4, pp.getErrorIndex());

        f1 = pff.parse("2 / 1", null, pp);
        assertEquals(new Fraction(2, 1), f1);
        assertEquals(6, pp.getIndex());

        f1 = pff.parse("2 / 1", null, pp);
        assertEquals(new Fraction(2, 1), f1);
        assertEquals(6, pp.getIndex());

        f1 = pff.parse("-2 / 1", null, pp);
        assertEquals(new Fraction(-2, 1), f1);
        assertEquals(7, pp.getIndex());

        f1 = pff.parse("-2 / 1", null, pp);
        assertEquals(null, f1);
        assertEquals(7, pp.getIndex());
        assertEquals(7, pp.getErrorIndex());
    }

    @Test
    public void testParseWholeNumber() {
        ProperFractionFormat pff = new ProperFractionFormat();

        ParsePosition pp = new ParsePosition(0);
        Fraction f1 = pff.parse("-1", null, pp);
        assertEquals(new Fraction(-1, 1), f1);
        assertEquals(2, pp.getIndex());

        f1 = pff.parse("1", null, pp);
        assertEquals(new Fraction(1, 1), f1);
        assertEquals(2, pp.getIndex());

        f1 = pff.parse("1", null, pp);
        assertEquals(new Fraction(1, 1), f1);
        assertEquals(2, pp.getIndex());

        f1 = pff.parse("-1", null, pp);
        assertEquals(new Fraction(-1, 1), f1);
        assertEquals(2, pp.getIndex());

        f1 = pff.parse("-1", null, pp);
        assertEquals(new Fraction(-1, 1), f1);
        assertEquals(2, pp.getIndex());

        f1 = pff.parse("-1", null, pp);
        assertEquals(null, f1);
        assertEquals(2, pp.getIndex());
        assertEquals(2, pp.getErrorIndex());

        pff = new ProperFractionFormat(NumberFormat.getNumberInstance());
        pp = new ParsePosition(0);

        Fraction f2 = pff.parse("1", null, pp);
        assertEquals(new Fraction(1, 1), f2);
        assertEquals(2, pp.getIndex());

        f2 = pff.parse("1", null, pp);
        assertEquals(new Fraction(1, 1), f2);
        assertEquals(2, pp.getIndex());

        f2 = pff.parse("-1", null, pp);
        assertEquals(new Fraction(-1, 1), f2);
        assertEquals(3, pp.getIndex());

        f2 = pff.parse("-1", null, pp);
        assertEquals(new Fraction(-1, 1), f2);
        assertEquals(3, pp.getIndex());

        f2 = pff.parse("-1", null, pp);
        assertEquals(null, f2);
        assertEquals(3, pp.getIndex());
        assertEquals(3, pp.getErrorIndex());

        pff = new ProperFractionFormat(NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());
        pp = new ParsePosition(0);

        Fraction f3 = pff.parse("1", null, pp);
        assertEquals(new Fraction(1, 1), f3);
        assertEquals(2, pp.getIndex());

        f3 = pff.parse("1", null, pp);
        assertEquals(new Fraction(1, 1), f3);
        assertEquals(2, pp.getIndex());

        f3 = pff.parse("-1", null, pp);
        assertEquals(new Fraction(-1, 1), f3);
        assertEquals(3, pp.getIndex());

        f3 = pff.parse("-1", null, pp);
        assertEquals(new Fraction(-1, 1), f3);
        assertEquals(3, pp.getIndex());

        f3 = pff.parse("-1", null, pp);
        assertEquals(null, f3);
        assertEquals(3, pp.getIndex());
        assertEquals(3, pp.getErrorIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        NumberFormat nf = NumberFormat.getNumberInstance();

        pff.setWholeFormat(nf);

        Fraction f1 = new Fraction(1, 2);
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        pff.format(f1, sb, pos);
        assertEquals("1", sb.toString());

        pff = new ProperFractionFormat(nf, NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());

        pff.format(f1, sb, pos);
        assertEquals("1", sb.toString());
    }

}