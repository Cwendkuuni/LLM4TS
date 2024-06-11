import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.*;
import org.junit.*;

public class ProperFractionFormatTest {

    private ProperFractionFormat format;
    private Fraction fraction;

    @Before
    public void setUp() {
        format = new ProperFractionFormat();
        fraction = new Fraction(3, 4);
    }

    @Test
    public void testFormat_WholeNumber() {
        Fraction wholeNumber = new Fraction(2, 1);
        String expected = "2";
        assertEquals(expected, format.format(wholeNumber));
    }

    @Test
    public void testFormat_PositiveFraction() {
        String expected = "3 / 4";
        assertEquals(expected, format.format(fraction));
    }

    @Test
    public void testFormat_ZeroFraction() {
        Fraction zeroFraction = new Fraction(0, 1);
        String expected = "0";
        assertEquals(expected, format.format(zeroFraction));
    }

    @Test
    public void testFormat_NegativeFraction() {
        Fraction negativeFraction = new Fraction(-3, 4);
        String expected = "-3 / 4";
        assertEquals(expected, format.format(negativeFraction));
    }

    @Test
    public void testFormat_PositiveWholeFraction() {
        Fraction positiveWholeFraction = new Fraction(5, 4);
        String expected = "1 1 / 4";
        assertEquals(expected, format.format(positiveWholeFraction));
    }

    @Test
    public void testFormat_NegativeWholeFraction() {
        Fraction negativeWholeFraction = new Fraction(-5, 4);
        String expected = "-1 1 / 4";
        assertEquals(expected, format.format(negativeWholeFraction));
    }

    @Test
    public void testParse_WholeNumber() {
        String input = "2";
        Fraction expected = new Fraction(2, 1);
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParse_PositiveFraction() {
        String input = "3 / 4";
        Fraction expected = fraction;
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParse_ZeroFraction() {
        String input = "0";
        Fraction expected = new Fraction(0, 1);
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParse_NegativeFraction() {
        String input = "-3 / 4";
        Fraction expected = new Fraction(-3, 4);
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParse_PositiveWholeFraction() {
        String input = "1 1 / 4";
        Fraction expected = new Fraction(5, 4);
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParse_NegativeWholeFraction() {
        String input = "-1 1 / 4";
        Fraction expected = new Fraction(-5, 4);
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParse_InvalidFormat() {
        String input = "2/3/4";
        Fraction expected = null;
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParse_InvalidWholeNumber() {
        String input = "-2 3 / 4";
        Fraction expected = null;
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParse_InvalidNumerator() {
        String input = "2 / -3 4";
        Fraction expected = null;
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParse_InvalidDenominator() {
        String input = "2 / 3 -4";
        Fraction expected = null;
        assertEquals(expected, format.parse(input));
    }
}