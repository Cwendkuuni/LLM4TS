package org.apache.commons.math.fraction;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        NumberFormat format = NumberFormat.getInstance();
        properFractionFormat = new ProperFractionFormat(format);
    }

    @Test
    public void testFormat_PositiveFraction_ReturnsStringRepresentation() {
        Fraction fraction = new Fraction(3, 4);

        String result = properFractionFormat.format(fraction);
        String expected = "3 / 4";

        assertEquals(expected, result);
    }

    @Test
    public void testFormat_ProperFractionWithWholeNumber_ReturnsStringRepresentation() {
        Fraction fraction = new Fraction(3, 2);

        String result = properFractionFormat.format(fraction);
        String expected = "1 1 / 2";

        assertEquals(expected, result);
    }

    @Test
    public void testFormat_NegativeFraction_ReturnsStringRepresentation() {
        Fraction fraction = new Fraction(-2, 5);

        String result = properFractionFormat.format(fraction);
        String expected = "-2 / 5";

        assertEquals(expected, result);
    }

    @Test
    public void testFormat_ZeroFraction_ReturnsStringRepresentation() {
        Fraction fraction = new Fraction(0, 1);

        String result = properFractionFormat.format(fraction);
        String expected = "0 / 1";

        assertEquals(expected, result);
    }

    @Test
    public void testParse_ValidFractionString_ReturnsFractionObject() {
        String fractionStr = "3 / 4";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionStr, pos);
        Fraction expected = new Fraction(3, 4);

        assertEquals(expected, result);
        assertEquals(fractionStr.length(), pos.getIndex());
    }

    @Test
    public void testParse_ValidFractionStringWithWholeNumber_ReturnsFractionObject() {
        String fractionStr = "1 1 / 2";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionStr, pos);
        Fraction expected = new Fraction(3, 2);

        assertEquals(expected, result);
        assertEquals(fractionStr.length(), pos.getIndex());
    }

    @Test
    public void testParse_ValidNegativeFractionString_ReturnsFractionObject() {
        String fractionStr = "-2 / 5";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionStr, pos);
        Fraction expected = new Fraction(-2, 5);

        assertEquals(expected, result);
        assertEquals(fractionStr.length(), pos.getIndex());
    }

    @Test
    public void testParse_ValidZeroFractionString_ReturnsFractionObject() {
        String fractionStr = "0 / 1";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionStr, pos);
        Fraction expected = new Fraction(0, 1);

        assertEquals(expected, result);
        assertEquals(fractionStr.length(), pos.getIndex());
    }

    @Test
    public void testParse_InvalidFractionString_ReturnsNull() {
        String fractionStr = "3 4 / 5";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionStr, pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParse_InvalidFractionStringWithInvalidWholeNumber_ReturnsNull() {
        String fractionStr = "abc 1 / 2";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionStr, pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParse_InvalidFractionStringWithInvalidNumerator_ReturnsNull() {
        String fractionStr = "1 / abc";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionStr, pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParse_InvalidFractionStringWithInvalidDenominator_ReturnsNull() {
        String fractionStr = "1 / 0";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionStr, pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
    }

}