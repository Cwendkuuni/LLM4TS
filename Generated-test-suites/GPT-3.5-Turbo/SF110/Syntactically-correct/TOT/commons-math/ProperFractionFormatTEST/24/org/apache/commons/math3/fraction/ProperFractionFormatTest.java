import org.junit.Test;
import org.apache.commons.math3.fraction.*;

public class ProperFractionFormatTest {

    // Test cases for format() method
    // typical use case
    @Test
    public void format_HandlesTypicalUseCase() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        String result = format.format(fraction, new StringBuffer(), null).toString();
        assertEquals("3 / 4", result);
    }

    // edge case: fraction has whole number part
    @Test
    public void format_HandlesWholeNumberPart() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(7, 4);
        String result = format.format(fraction, new StringBuffer(), null).toString();
        assertEquals("1 3 / 4", result);
    }

    // edge case: fraction is negative
    @Test
    public void format_HandlesNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 2);
        String result = format.format(fraction, new StringBuffer(), null).toString();
        assertEquals("- 3 / 2", result);
    }

    // Test cases for getWholeFormat() method
    // typical use case
    @Test
    public void getWholeFormat_HandlesTypicalUseCase() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat result = format.getWholeFormat();
        assertNotNull(result);
    }

    // Test cases for parse() method
    // typical use case
    @Test
    public void parse_HandlesTypicalUseCase() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("3 / 4", null);
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, result);
    }

    // error scenario: invalid input format
    @Test
    public void parse_HandlesInvalidInputFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("invalid", null);
        assertNull(result);
    }

    // edge case: negative numerator
    @Test
    public void parse_HandlesNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("-3 / 4", null);
        Fraction expected = new Fraction(-3, 4);
        assertEquals(expected, result);
    }

    // edge case: negative denominator
    @Test
    public void parse_HandlesNegativeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("3 / -4", null);
        Fraction expected = new Fraction(3, -4);
        assertEquals(expected, result);
    }

    // Test cases for setWholeFormat() method
    // typical use case
    @Test
    public void setWholeFormat_HandlesTypicalUseCase() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        format.setWholeFormat(numberFormat);
        NumberFormat result = format.getWholeFormat();
        assertEquals(numberFormat, result);
    }

}