import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1/2";
        assertEquals(expected, format.format(fraction));
    }

    @Test
    public void testFormatWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 2);
        String expected = "-1 1/2";
        assertEquals(expected, format.format(fraction));
    }

    @Test
    public void testFormatWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 2);
        String expected = "2 1/2";
        assertEquals(expected, format.format(fraction));
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 1/2";
        Fraction expected = new Fraction(7, 2);
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParseWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-1 2/3";
        Fraction expected = new Fraction(-5, 3);
        assertEquals(expected, format.parse(input));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-3 -1/2";
        format.parse(input);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3/2";
        format.parse(input);
    }
}