import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {
    private ProperFractionFormat properFractionFormat;
    private NumberFormat numberFormat;

    @Before
    public void setUp() {
        numberFormat = NumberFormat.getInstance();
        properFractionFormat = new ProperFractionFormat(numberFormat);
    }

    @Test
    public void format_shouldReturnProperlyFormattedString() {
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        String result = properFractionFormat.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void format_shouldReturnProperlyFormattedString_whenFractionIsNegative() {
        Fraction fraction = new Fraction(-5, 2);
        String expected = "-5 / 2";
        String result = properFractionFormat.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void format_shouldReturnProperlyFormattedString_whenFractionIsMixedNumber() {
        Fraction fraction = new Fraction(-7, 2);
        String expected = "-3 1/2";
        String result = properFractionFormat.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void parse_shouldReturnFraction_whenStringIsProperlyFormatted() throws ParseException {
        String source = "3 / 4";
        Fraction expected = new Fraction(3, 4);
        Fraction result = properFractionFormat.parse(source);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void parse_shouldThrowParseException_whenStringIsNotProperlyFormatted() throws ParseException {
        String source = "3 / 4 / 5";
        properFractionFormat.parse(source);
    }

    @Test(expected = ParseException.class)
    public void parse_shouldThrowParseException_whenStringContainsInvalidCharacters() throws ParseException {
        String source = "3 1/2 abc";
        properFractionFormat.parse(source);
    }

    @Test
    public void parse_shouldReturnFraction_whenStringIsMixedNumber() throws ParseException {
        String source = "-3 1/2";
        Fraction expected = new Fraction(-7, 2);
        Fraction result = properFractionFormat.parse(source);
        assertEquals(expected, result);
    }

    @Test
    public void parse_shouldReturnFractionWithNegativeNumerator_whenStringIsNegativeMixedNumber() throws ParseException {
        String source = "-5 3/4";
        Fraction expected = new Fraction(-23, 4);
        Fraction result = properFractionFormat.parse(source);
        assertEquals(expected, result);
    }
    
    @Test
    public void parse_shouldReturnFractionWithWholeNumber_whenStringIsFraction() throws ParseException {
        String source = "2 / 3";
        Fraction expected = new Fraction(2, 3);
        Fraction result = properFractionFormat.parse(source);
        assertEquals(expected, result);
    }
    
    @Test
    public void parse_shouldReturnFractionWithDenominator1_whenStringIsWholeNumber() throws ParseException {
        String source = "-5";
        Fraction expected = new Fraction(-5);
        Fraction result = properFractionFormat.parse(source);
        assertEquals(expected, result);
    }
    
    @Test
    public void parse_shouldReturnNull_whenStringIsInvalidWholeNumber() {
        String source = "abc";
        Fraction result = properFractionFormat.parse(source);
        assertNull(result);
    }
    
    @Test
    public void parse_shouldReturnNull_whenStringIsEmpty() {
        String source = "";
        Fraction result = properFractionFormat.parse(source);
        assertNull(result);
    }
}