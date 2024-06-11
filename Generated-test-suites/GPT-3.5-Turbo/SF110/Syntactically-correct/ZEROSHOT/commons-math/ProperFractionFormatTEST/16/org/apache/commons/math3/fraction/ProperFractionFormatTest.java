package org.apache.commons.math3.fraction;

import org.junit.Before;
import org.junit.Test;
import java.text.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
    private ProperFractionFormat properFraction;

    @Before
    public void setUp() {
        properFraction = new ProperFractionFormat();
    }

    @Test
    public void format_withProperFraction_returnsExpectedString() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        assertEquals("3 / 4", properFraction.format(fraction, toAppendTo, pos).toString());
    }

    @Test
    public void format_withWholeNumber_returnsExpectedString() {
        Fraction fraction = new Fraction(5, 1);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        assertEquals("5", properFraction.format(fraction, toAppendTo, pos).toString());
    }

    @Test
    public void format_withNegativeFraction_returnsExpectedString() {
        Fraction fraction = new Fraction(-2, 5);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        assertEquals("-2 / 5", properFraction.format(fraction, toAppendTo, pos).toString());
    }

    @Test
    public void getWholeFormat_returnsWholeFormat() {
        NumberFormat wholeFormat = properFraction.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test
    public void parse_withValidString_returnsExpectedFraction() {
        String source = "2 / 3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFraction.parse(source, pos);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void parse_withInvalidString_returnsNull() {
        String source = "invalid";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFraction.parse(source, pos);
        assertNull(fraction);
    }

    @Test
    public void setWholeFormat_withValidFormat_setsWholeFormat() {
        NumberFormat format = NumberFormat.getInstance();
        properFraction.setWholeFormat(format);
        assertEquals(format, properFraction.getWholeFormat());
    }
}