import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;

import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    private ProperFractionFormat format;

    @Before
    public void setup() {
        format = new ProperFractionFormat();
    }

    @Test
    public void testFormat_PositiveWholeNumber() {
        Fraction fraction = new Fraction(5, 1);
        String expected = "5";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_NegativeWholeNumber() {
        Fraction fraction = new Fraction(-2, 1);
        String expected = "-2";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_PositiveFractionWithWholeNumber() {
        Fraction fraction = new Fraction(7, 2);
        String expected = "3 1/2";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_NegativeFractionWithWholeNumber() {
        Fraction fraction = new Fraction(-7, 2);
        String expected = "-3 1/2";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_ImproperPositiveFraction() {
        Fraction fraction = new Fraction(7, 3);
        String expected = "2 1/3";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_ImproperNegativeFraction() {
        Fraction fraction = new Fraction(-7, 3);
        String expected = "-2 1/3";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_PositiveWholeNumber() {
        String source = "10";
        Fraction expected = new Fraction(10, 1);
        Fraction result = format.parse(source);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_NegativeWholeNumber() {
        String source = "-5";
        Fraction expected = new Fraction(-5, 1);
        Fraction result = format.parse(source);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_PositiveFractionWithWholeNumber() {
        String source = "3 1/2";
        Fraction expected = new Fraction(7, 2);
        Fraction result = format.parse(source);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_NegativeFractionWithWholeNumber() {
        String source = "-3 1/2";
        Fraction expected = new Fraction(-7, 2);
        Fraction result = format.parse(source);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_InvalidFractionWithMinusSignInTheMiddle() {
        String source = "3 -1/2";
        Fraction result = format.parse(source);
        assertNull(result);
    }

    @Test
    public void testParse_InvalidFractionWithMultipleMinusSigns() {
        String source = "-3 -1/2";
        Fraction result = format.parse(source);
        assertNull(result);
    }

    @Test
    public void testParse_ImproperPositiveFraction() {
        String source = "2 1/3";
        Fraction expected = new Fraction(7, 3);
        Fraction result = format.parse(source);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_ImproperNegativeFraction() {
        String source = "-2 1/3";
        Fraction expected = new Fraction(-7, 3);
        Fraction result = format.parse(source);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_InvalidFractionWithMissingNumerator() {
        String source = "5 / 2";
        Fraction result = format.parse(source);
        assertNull(result);
    }

    @Test
    public void testParse_InvalidFractionWithMissingDenominator() {
        String source = "5 1 /";
        Fraction result = format.parse(source);
        assertNull(result);
    }
}