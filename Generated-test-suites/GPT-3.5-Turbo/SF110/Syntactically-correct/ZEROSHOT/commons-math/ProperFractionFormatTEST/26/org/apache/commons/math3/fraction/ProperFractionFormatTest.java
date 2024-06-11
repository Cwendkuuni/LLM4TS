
import org.apache.commons.math3.fraction.*;
import org.junit.Before;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void formatWithWholeNumberAndPositiveFraction() {
        Fraction fraction = new Fraction(7, 4);
        String expected = "1 3 / 4";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }

    @Test
    public void formatWithNegativeFraction() {
        Fraction fraction = new Fraction(-3, 5);
        String expected = "-3 / 5";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }

    @Test
    public void formatWithWholeNumberAndNegativeFraction() {
        Fraction fraction = new Fraction(-2, 3);
        String expected = "-2 / 3";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }

    @Test
    public void formatWithNoWholeNumberAndPositiveFraction() {
        Fraction fraction = new Fraction(2, 5);
        String expected = "2 / 5";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }

    @Test
    public void formatWithNoWholeNumberAndNegativeFraction() {
        Fraction fraction = new Fraction(-4, 7);
        String expected = "-4 / 7";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }

    @Test
    public void formatWithWholeNumberOnly() {
        Fraction fraction = new Fraction(8, 1);
        String expected = "8";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }

    @Test
    public void formatWithNoFraction() {
        Fraction fraction = new Fraction(0, 1);
        String expected = "0";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }

    @Test
    public void parseWithValidInput() throws ParseException {
        String input = "1 3 / 4";
        Fraction expected = new Fraction(7, 4);
        Fraction actual = properFractionFormat.parse(input);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void parseWithInvalidInput() throws ParseException {
        String input = "Invalid Fraction";
        properFractionFormat.parse(input);
    }

    @Test(expected = ParseException.class)
    public void parseWithInvalidFraction() throws ParseException {
        String input = "1 -1 / 4";
        properFractionFormat.parse(input);
    }
}

