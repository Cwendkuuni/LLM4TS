import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    private ProperFractionFormat format;

    @Before
    public void setup() {
        format = new ProperFractionFormat();
    }

    @Test
    public void testFormat_withValidFraction_returnsCorrectString() {
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_withValidNegativeFraction_returnsCorrectString() {
        Fraction fraction = new Fraction(-3, 4);
        String expected = "-3 / 4";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_withValidWholeNumber_returnsCorrectString() {
        Fraction fraction = new Fraction(5, 1);
        String expected = "5";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_withValidNegativeWholeNumber_returnsCorrectString() {
        Fraction fraction = new Fraction(-5, 1);
        String expected = "-5";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_withValidFractionAndWholeNumber_returnsCorrectString() {
        Fraction fraction = new Fraction(3, 4);
        fraction = fraction.add(2);
        String expected = "2 3 / 4";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_withValidNegativeFractionAndWholeNumber_returnsCorrectString() {
        Fraction fraction = new Fraction(-3, 4);
        fraction = fraction.add(2);
        String expected = "2 -3 / 4";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat_withZeroFraction_returnsCorrectString() {
        Fraction fraction = new Fraction(0, 4);
        String expected = "0";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testParse_withInvalidFraction_throwsParseException() {
        String input = "3/-4";
        format.parse(input);
    }

    @Test
    public void testParse_withValidFraction_returnsCorrectFraction() {
        String input = "3 / 4";
        Fraction expected = new Fraction(3, 4);
        Fraction result = format.parse(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_withValidNegativeFraction_returnsCorrectFraction() {
        String input = "-3 / 4";
        Fraction expected = new Fraction(-3, 4);
        Fraction result = format.parse(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_withValidWholeNumber_returnsCorrectFraction() {
        String input = "5";
        Fraction expected = new Fraction(5, 1);
        Fraction result = format.parse(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_withValidNegativeWholeNumber_returnsCorrectFraction() {
        String input = "-5";
        Fraction expected = new Fraction(-5, 1);
        Fraction result = format.parse(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_withValidFractionAndWholeNumber_returnsCorrectFraction() {
        String input = "2 3 / 4";
        Fraction expected = new Fraction(11, 4);
        Fraction result = format.parse(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_withValidNegativeFractionAndWholeNumber_returnsCorrectFraction() {
        String input = "2 -3 / 4";
        Fraction expected = new Fraction(5, 4);
        Fraction result = format.parse(input);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testParse_withInvalidFormat_throwsParseException() {
        String input = "abc";
        format.parse(input);
    }

    @Test(expected = ParseException.class)
    public void testParse_withIncompleteFraction_throwsParseException() {
        String input = "3 /";
        format.parse(input);
    }

}