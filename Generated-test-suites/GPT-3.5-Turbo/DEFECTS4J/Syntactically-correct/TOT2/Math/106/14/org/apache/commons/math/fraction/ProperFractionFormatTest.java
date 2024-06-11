import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, null);
        assertEquals("3 / 4", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("3 / 4", pos);
        assertEquals(new Fraction(3, 4), result);
    }

    @Test
    public void testParseWithWhiteSpace() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance());
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("  2 / 5", pos);
        assertEquals(new Fraction(2, 5), result);
    }

    @Test
    public void testParseWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("2 3 / 4", pos);
        assertEquals(new Fraction(11, 4), result);
    }

    @Test
    public void testParseInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("invalid", pos);
        assertNull(result);
    }

    @Test
    public void testFormatNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-1, 2);
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, null);
        assertEquals("-1 / 2", result.toString());
    }

    @Test
    public void testParseNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("-2 3 / 4", pos);
        assertEquals(new Fraction(-11, 4), result);
}

    @Test
    public void testParseInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("-2 3 4", pos);
        assertNull(result);
    }
}