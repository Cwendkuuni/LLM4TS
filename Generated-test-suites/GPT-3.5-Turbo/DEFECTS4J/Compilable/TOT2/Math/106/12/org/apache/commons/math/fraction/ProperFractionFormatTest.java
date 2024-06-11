package org.apache.commons.math.fraction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = properFractionFormat.format(fraction, buffer, pos);
        assertEquals("3 / 2", result.toString());
    }

    @Test
    public void testParse_ValidInput() {
        String input = "5 1/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(input, pos);
        assertNotNull(result);
        assertEquals(16, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testParse_InvalidInput() {
        String input = "-5 -1/3"; // Invalid input with double negative sign
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(input, pos);
        assertNull(result);
    }

    @Test
    public void testParse_InvalidFraction() {
        String input = "3 2/"; // Invalid fraction without denominator
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(input, pos);
        assertNull(result);
    }
}