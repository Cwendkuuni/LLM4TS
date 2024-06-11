package org.apache.commons.math3.fraction;

import static org.junit.Assert.*; 
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        // Test 1
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 5);
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer expected = new StringBuffer();
        expected.append("3 / 5");
        format.format(fraction, result, pos);
        assertEquals(expected.toString(), result.toString());

        // Test 2
        format = new ProperFractionFormat();
        fraction = new Fraction(-4, 7);
        result = new StringBuffer();
        pos = new FieldPosition(0);
        expected = new StringBuffer();
        expected.append("-4 / 7");
        format.format(fraction, result, pos);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertEquals(FractionFormat.getDefaultNumberFormat(), wholeFormat);
    }

    @Test
    public void testParse() {
        // Test 1
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 / 3";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(2, 3);
        Fraction result = format.parse(source, pos);
        assertEquals(expected, result);

        // Test 2
        format = new ProperFractionFormat();
        source = "-5 / 6";
        pos = new ParsePosition(0);
        expected = new Fraction(-5, 6);
        result = format.parse(source, pos);
        assertEquals(expected, result);

    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

}