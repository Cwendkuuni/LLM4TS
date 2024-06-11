import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(4, 5);
        String result = format.format(fraction, new StringBuffer(), null);
        String expected = "4 / 5";
        assertEquals(expected, result);
    }

    @Test
    public void testCustomFormat() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance();
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = new Fraction(4, 5);
        String result = format.format(fraction, new StringBuffer(), null);
        String expected = "4 % % / 5 %%";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat(null, getNumeratorFormat(), getDenominatorFormat());
    }

    @Test(expected = ParseException.class)
    public void testNegativeWholeNumber() {
        String input = "-3 1/2";
        ParsePosition pos = new ParsePosition(0);
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse(input, pos);
        fail("Expected ParseException");
    }

    @Test(expected = ParseException.class)
    public void testInvalidFormat() {
        String input = "-3 -1/2";
        ParsePosition pos = new ParsePosition(0);
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse(input, pos);
        fail("Expected ParseException");
    }

    // Other test cases for edge cases and exceptional scenarios can be added here
}