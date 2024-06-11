import org.junit.Before;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat_withValidFraction_returnFormattedString() {
        Fraction fraction = new Fraction(5, 2);
        String expected = "2 1 / 2";

        String result = properFractionFormat.format(fraction);

        assertEquals(expected, result);
    }

    @Test
    public void testFormat_withZeroWholeNumber_returnFractionStringWithoutWhole() {
        Fraction fraction = new Fraction(4, 5);
        String expected = "4 / 5";

        String result = properFractionFormat.format(fraction);

        assertEquals(expected, result);
    }

    @Test
    public void testFormat_withNegativeFraction_returnFormattedString() {
        Fraction fraction = new Fraction(-3, 2);
        String expected = "-1 1 / 2";

        String result = properFractionFormat.format(fraction);

        assertEquals(expected, result);
    }

    @Test
    public void testFormat_withZeroFraction_returnProperFraction() {
        Fraction fraction = new Fraction(0, 5);
        String expected = "0";

        String result = properFractionFormat.format(fraction);

        assertEquals(expected, result);
    }
    
    @Test
    public void testFormat_withFractionAndInvalidWholeNumberFormat_throwException() {
        properFractionFormat.setWholeFormat(NumberFormat.getCurrencyInstance(Locale.US));
        Fraction fraction = new Fraction(3, 2);

        assertThrows(IllegalArgumentException.class, () -> properFractionFormat.format(fraction));
    }

    @Test
    public void testParse_withValidFraction_returnParsedFraction() {
        String fractionString = "-3 1 / 4";
        Fraction expected = new Fraction(-13, 4);

        Fraction result = properFractionFormat.parse(fractionString);

        assertEquals(expected, result);
    }

    @Test
    public void testParse_withFractionWithoutWhole_returnParsedFraction() {
        String fractionString = "5 / 6";
        Fraction expected = new Fraction(5, 6);

        Fraction result = properFractionFormat.parse(fractionString);

        assertEquals(expected, result);
    }

    @Test
    public void testParse_withInvalidFraction_throwParseException() {
        String fractionString = "2 % 3";

        assertThrows(ParseException.class, () -> properFractionFormat.parse(fractionString));
    }

    @Test
    public void testParse_withInvalidWhole_throwParseException() {
        String fractionString = "a 1 / 4";

        assertThrows(ParseException.class, () -> properFractionFormat.parse(fractionString));
    }

    @Test
    public void testSetWholeFormat_withValidFormat_setWholeFormat() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        properFractionFormat.setWholeFormat(format);

        assertEquals(format, properFractionFormat.getWholeFormat());
    }

    @Test
    public void testSetWholeFormat_withNullFormat_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> properFractionFormat.setWholeFormat(null));
    }
}