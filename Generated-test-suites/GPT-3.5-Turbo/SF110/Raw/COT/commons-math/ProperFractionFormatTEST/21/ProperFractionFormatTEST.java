import org.junit.Test;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    // Test the default constructor
    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
        assertEquals(FractionFormat.getDefaultNumberFormat(), wholeFormat);
    }

    // Test the constructor with NumberFormat parameter
    @Test
    public void testNumberFormatConstructor() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(format);
        assertNotNull(properFractionFormat);
        assertEquals(format, properFractionFormat.getWholeFormat());
    }

    // Test the constructor with wholeFormat, numeratorFormat, and denominatorFormat parameters
    @Test
    public void testThreeArgConstructor() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getCurrencyInstance();
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(format);
        assertEquals(wholeFormat, format.getWholeFormat());
        assertEquals(numeratorFormat, format.getNumeratorFormat());
        assertEquals(denominatorFormat, format.getDenominatorFormat());
    }

    // Test the format method with a fraction that has a non-zero whole number
    @Test
    public void testFormatWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, 3);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("2", result.toString());
    }

    // Test the format method with a fraction that has a zero whole number
    @Test
    public void testFormatWithZeroWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("1 / 2", result.toString());
    }

    // Test the format method with a fraction that has a negative whole number
    @Test
    public void testFormatWithNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-1, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("-1 / 2", result.toString());
    }

    // Test the format method with a fraction that has a negative numerator
    @Test
    public void testFormatWithNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, -2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("-1 / 2", result.toString());
    }

    // Test the format method with a fraction that has a negative denominator
    @Test
    public void testFormatWithNegativeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        fraction = fraction.reciprocal();
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("-1 / 2", result.toString());
    }

    // Test the format method with a fraction that is equal to 0
    @Test
    public void testFormatWithZeroFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 1);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("0", result.toString());
    }

    // Test the format method with a fraction that is equal to 1
    @Test
    public void testFormatWithOneFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 1);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("1", result.toString());
    }

    // Test the format method with a fraction that is negative and equal to 1
    @Test
    public void testFormatWithNegativeOneFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-1, 1);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("-1", result.toString());
    }

    // Test the parse method with a valid fraction string
    @Test
    public void testParseValidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "2 / 3";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(fractionString, position);
        assertNotNull(fraction);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    // Test the parse method with an invalid fraction string
    @Test
    public void testParseInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "2 3";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(fractionString, position);
        assertNull(fraction);
    }

    // Test the parse method with a fraction string that has a negative numerator
    @Test
    public void testParseNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "-2 / 3";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(fractionString, position);
        assertNotNull(fraction);
        assertEquals(-2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    // Test the parse method with a fraction string that has a negative denominator
    @Test
    public void testParseNegativeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "2 / -3";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(fractionString, position);
        assertNotNull(fraction);
        assertEquals(-2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    // Test the parse method with a fraction string that has a negative whole number
    @Test
    public void testParseNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "-2 1 / 3";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(fractionString, position);
        assertNotNull(fraction);
        assertEquals(-7, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    // Test the parse method with a fraction string that has an invalid numerator
    @Test
    public void testParseInvalidNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "2.5 / 3";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(fractionString, position);
        assertNull(fraction);
    }

    // Test the parse method with a fraction string that has an invalid denominator
    @Test
    public void testParseInvalidDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "2 / 3.5";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(fractionString, position);
        assertNull(fraction);
    }

    // Test the parse method with a fraction string that has invalid characters
    @Test
    public void testParseInvalidCharacters() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "2 + 3";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(fractionString, position);
        assertNull(fraction);
    }

    // Test the setWholeFormat method with null format argument
    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}