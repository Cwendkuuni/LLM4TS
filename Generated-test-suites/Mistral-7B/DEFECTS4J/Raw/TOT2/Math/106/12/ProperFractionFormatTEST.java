import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        String result = format.format(fraction, new StringBuffer(), null);
        assertEquals("1 / 2", result.toString());
    }

    @Test
    public void testCustomFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = new Fraction(1, 2);
        String result = format.format(fraction, new StringBuffer(), null);
        assertEquals("1 / 2", result.toString());
    }

    @Test
    public void testFormatNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-1, 1);
        String result = format.format(fraction, new StringBuffer(), null);
        assertEquals("-1 / 1", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() {
        ProperFractionFormat format = new ProperFractionFormat(null, null, null);
    }

    @Test
    public void testParseProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "1 2/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
        assertEquals(new Fraction(1, 3), fraction);
        assertEquals(5, pos.getIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidInput1() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-3 -1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidInput2() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 1";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidInput3() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 1/0";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
    }

}