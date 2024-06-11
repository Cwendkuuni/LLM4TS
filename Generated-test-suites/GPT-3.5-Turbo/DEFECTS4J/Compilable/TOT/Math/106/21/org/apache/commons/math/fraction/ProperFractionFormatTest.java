package org.apache.commons.math.fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParsePosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = numberFormat;
        NumberFormat denominatorFormat = numberFormat;
        properFractionFormat = new ProperFractionFormat(numberFormat, numeratorFormat, denominatorFormat);
    }

    @Test
    public void format_shouldReturnProperlyFormattedFractionString() {
        // Arrange
        Fraction fraction = new Fraction(5, 2);
        StringBuffer stringBuffer = new StringBuffer();

        // Act
        properFractionFormat.format(fraction, stringBuffer, null);

        // Assert
        assertEquals(stringBuffer.toString(), "2 1/2");
    }

    @Test
    public void format_shouldReturnProperlyFormattedFractionStringWhenWholeNumberIsNegative() {
        // Arrange
        Fraction fraction = new Fraction(-5, 2);
        StringBuffer stringBuffer = new StringBuffer();

        // Act
        properFractionFormat.format(fraction, stringBuffer, null);

        // Assert
        assertEquals(stringBuffer.toString(), "-2 1/2");
    }

    @Test
    public void format_shouldReturnProperlyFormattedFractionStringWhenFractionIsImproper() {
        // Arrange
        Fraction fraction = new Fraction(9, 2);
        StringBuffer stringBuffer = new StringBuffer();

        // Act
        properFractionFormat.format(fraction, stringBuffer, null);

        // Assert
        assertEquals(stringBuffer.toString(), "4 1/2");
    }

    @Test
    public void format_shouldReturnProperlyFormattedFractionStringWhenFractionHasNegativeNumerator() {
        // Arrange
        Fraction fraction = new Fraction(5, -2);
        StringBuffer stringBuffer = new StringBuffer();

        // Act
        properFractionFormat.format(fraction, stringBuffer, null);

        // Assert
        assertEquals(stringBuffer.toString(), "-2 1/2");
    }

    @Test
    public void format_shouldReturnProperlyFormattedFractionStringWhenFractionHasNegativeDenominator() {
        // Arrange
        Fraction fraction = new Fraction(5, -2);
        StringBuffer stringBuffer = new StringBuffer();

        // Act
        properFractionFormat.format(fraction, stringBuffer, null);

        // Assert
        assertEquals(stringBuffer.toString(), "-2 1/2");
    }

    @Test
    public void parse_shouldReturnFractionObjectWhenStringIsProperlyFormatted() {
        // Arrange
        String source = "2 3/4";
        ParsePosition pos = new ParsePosition(0);

        // Act
        Fraction fraction = properFractionFormat.parse(source, pos);

        // Assert
        assertEquals(fraction.getNumerator(), 11);
        assertEquals(fraction.getDenominator(), 4);
    }

    @Test
    public void parse_shouldReturnFractionObjectWhenStringIsImproperlyFormatted() {
        // Arrange
        String source = "5/4";
        ParsePosition pos = new ParsePosition(0);

        // Act
        Fraction fraction = properFractionFormat.parse(source, pos);

        // Assert
        assertEquals(fraction.getNumerator(), 5);
        assertEquals(fraction.getDenominator(), 4);
    }

    @Test
    public void parse_shouldReturnNullWhenStringIsInvalid() {
        // Arrange
        String source = "5/0";
        ParsePosition pos = new ParsePosition(0);

        // Act
        Fraction fraction = properFractionFormat.parse(source, pos);

        // Assert
        assertNull(fraction);
    }

    @Test
    public void parse_shouldReturnNullWhenStringIsNotProperFraction() {
        // Arrange
        String source = "2 3 4";
        ParsePosition pos = new ParsePosition(0);

        // Act
        Fraction fraction = properFractionFormat.parse(source, pos);

        // Assert
        assertNull(fraction);
    }

}