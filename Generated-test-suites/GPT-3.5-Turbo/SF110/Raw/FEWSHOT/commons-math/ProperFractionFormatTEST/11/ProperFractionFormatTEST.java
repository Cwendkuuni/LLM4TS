package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setup() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        // Test case 1
        Fraction fraction1 = new Fraction(1, 2);
        StringBuffer toAppendTo1 = new StringBuffer();
        FieldPosition pos1 = new FieldPosition(0);
        StringBuffer expected1 = new StringBuffer("1 / 2");
        StringBuffer actual1 = properFractionFormat.format(fraction1, toAppendTo1, pos1);
        assertEquals(expected1.toString(), actual1.toString());

        // Test case 2
        Fraction fraction2 = new Fraction(3, 4);
        StringBuffer toAppendTo2 = new StringBuffer();
        FieldPosition pos2 = new FieldPosition(0);
        StringBuffer expected2 = new StringBuffer("3 / 4");
        StringBuffer actual2 = properFractionFormat.format(fraction2, toAppendTo2, pos2);
        assertEquals(expected2.toString(), actual2.toString());

        // Additional test cases can be added similar to the above format.
    }

    @Test
    public void testParse() {
        // Test case 1
        String source1 = "1 / 2";
        ParsePosition pos1 = new ParsePosition(0);
        Fraction expected1 = new Fraction(1, 2);
        Fraction actual1 = properFractionFormat.parse(source1, pos1);
        assertNotNull(actual1);
        assertEquals(expected1.getNumerator(), actual1.getNumerator());
        assertEquals(expected1.getDenominator(), actual1.getDenominator());

        // Test case 2
        String source2 = "3 / 4";
        ParsePosition pos2 = new ParsePosition(0);
        Fraction expected2 = new Fraction(3, 4);
        Fraction actual2 = properFractionFormat.parse(source2, pos2);
        assertNotNull(actual2);
        assertEquals(expected2.getNumerator(), actual2.getNumerator());
        assertEquals(expected2.getDenominator(), actual2.getDenominator());

        // Additional test cases can be added similar to the above parse.
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat expectedFormat = NumberFormat.getNumberInstance();
        NumberFormat actualFormat = properFractionFormat.getWholeFormat();
        assertNotNull(actualFormat);
        assertEquals(expectedFormat.getClass(), actualFormat.getClass());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat_nullFormat() {
        properFractionFormat.setWholeFormat(null);
    }
}