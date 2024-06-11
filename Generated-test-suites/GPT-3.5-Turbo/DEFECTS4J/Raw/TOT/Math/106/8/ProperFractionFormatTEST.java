import org.junit.Test;

import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    // Test cases for format() method
    
    // Typical use case for format() method
    @Test
    public void format_typical() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer expected = new StringBuffer("3 / 4");
        StringBuffer actual = format.format(fraction, new StringBuffer(), null);
        assertEquals(expected.toString(), actual.toString());
    }
    
    // Edge case for format() method - negative whole number
    @Test
    public void format_negativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-5, 7);
        StringBuffer expected = new StringBuffer("-5 / 7");
        StringBuffer actual = format.format(fraction, new StringBuffer(), null);
        assertEquals(expected.toString(), actual.toString());
    }
    
    // Error scenario for format() method - null fraction
    @Test(expected = NullPointerException.class)
    public void format_nullFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.format(null, new StringBuffer(), null);
    }
    
    // Test cases for parse() method
    
    // Typical use case for parse() method
    @Test
    public void parse_typical() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 / 4";
        Fraction expected = new Fraction(3, 4);
        Fraction actual = format.parse(source);
        assertEquals(expected, actual);
    }
    
    // Edge case for parse() method - negative whole number
    @Test
    public void parse_negativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-5 / 7";
        Fraction expected = new Fraction(-5, 7);
        Fraction actual = format.parse(source);
        assertEquals(expected, actual);
    }
    
    // Error scenario for parse() method - invalid format
    @Test(expected = ParseException.class)
    public void parse_invalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "abc";
        format.parse(source);
    }

}