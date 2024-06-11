import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat_positiveWholeAndFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        String expected = "3 1/2";
        assertEquals(expected, format.format(fraction, new StringBuffer(), null).toString());
    }

    @Test
    public void testFormat_negativeWholeAndFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 2);
        String expected = "-3 1/2";
        assertEquals(expected, format.format(fraction, new StringBuffer(), null).toString());
    }

    @Test
    public void testFormat_onlyFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        String expected = "1/2";
        assertEquals(expected, format.format(fraction, new StringBuffer(), null).toString());
    }

    @Test
    public void testFormat_zeroWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        String expected = "1/2";
        assertEquals(expected, format.format(fraction, new StringBuffer(), null).toString());
    }

    @Test
    public void testFormat_largeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(Integer.MAX_VALUE, 2);
        String expected = Integer.MAX_VALUE + " 1/2";
        assertEquals(expected, format.format(fraction, new StringBuffer(), null).toString());
    }

    @Test
    public void testFormat_largeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, Integer.MAX_VALUE);
        String expected = "0 3/" + Integer.MAX_VALUE;
        assertEquals(expected, format.format(fraction, new StringBuffer(), null).toString());
    }

    @Test
    public void testFormat_largeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, Integer.MAX_VALUE);
        String expected = "0 3/" + Integer.MAX_VALUE;
        assertEquals(expected, format.format(fraction, new StringBuffer(), null).toString());
    }

    @Test
    public void testParse_positiveWholeAndFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 1/2";
        Fraction expected = new Fraction(7, 2);
        assertEquals(expected, format.parse(source, null));
    }

    @Test
    public void testParse_negativeWholeAndFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-3 1/2";
        Fraction expected = new Fraction(-7, 2);
        assertEquals(expected, format.parse(source, null));
    }

    @Test
    public void testParse_onlyFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1/2";
        Fraction expected = new Fraction(1, 2);
        assertEquals(expected, format.parse(source, null));
    }

    @Test(expected = ParseException.class)
    public void testParse_invalidMultipleMinusSigns() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-3 -1/2";
        format.parse(source, null);
    }

    @Test(expected = ParseException.class)
    public void testParse_invalidDenominatorFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 1/2/3";
        format.parse(source, null);
    }

    @Test(expected = ParseException.class)
    public void testParse_invalidNumeratorFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 1/2a";
        format.parse(source, null);
    }
}