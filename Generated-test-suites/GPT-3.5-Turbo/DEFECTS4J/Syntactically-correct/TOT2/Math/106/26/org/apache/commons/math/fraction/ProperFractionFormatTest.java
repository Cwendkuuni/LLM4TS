import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        assertEquals("1 / 2", format.format(fraction));
    }

    @Test
    public void testParseValidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction expected = new Fraction(3, 2);
        Fraction result = format.parse("1 1/2");
        assertEquals(expected, result);
    }

    @Test
    public void testParseInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("-1 -1/2");
        assertNull(result);
    }

    @Test
    public void testParseNoFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction expected = new Fraction(3, 1);
        Fraction result = format.parse("3");
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-1, 2);
        assertEquals("-1 / 2", format.format(fraction));
    }

    @Test
    public void testParseNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction expected = new Fraction(-3, 2);
        Fraction result = format.parse("-1 1/2");
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.parse("abc");
    }

}