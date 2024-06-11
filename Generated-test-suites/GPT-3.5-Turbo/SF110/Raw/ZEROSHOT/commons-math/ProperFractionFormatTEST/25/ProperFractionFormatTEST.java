package org.apache.commons.math3.fraction;

import java.text.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat format;

    public ProperFractionFormatTest() {
        format = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(5, 2);
        String expected = "2 1/2";
        String actual = format.format(fraction);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() {
        String source = "2 1/2";
        Fraction expected = new Fraction(5, 2);
        Fraction actual = format.parse(source);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseInvalidFormat() {
        String source = "2.5";
        Fraction expected = null;
        Fraction actual = format.parse(source);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseInvalidFraction() {
        String source = "2 -1/2";
        Fraction expected = null;
        Fraction actual = format.parse(source);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseWholeNumber() {
        String source = "2";
        Fraction expected = new Fraction(2, 1);
        Fraction actual = format.parse(source);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFractionOnly() {
        String source = "1/2";
        Fraction expected = new Fraction(1, 2);
        Fraction actual = format.parse(source);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseNegativeFraction() {
        String source = "-1/2";
        Fraction expected = new Fraction(-1, 2);
        Fraction actual = format.parse(source);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseNegativeWholeNumber() {
        String source = "-2";
        Fraction expected = new Fraction(-2, 1);
        Fraction actual = format.parse(source);
        assertEquals(expected, actual);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        format.setWholeFormat(null);
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}