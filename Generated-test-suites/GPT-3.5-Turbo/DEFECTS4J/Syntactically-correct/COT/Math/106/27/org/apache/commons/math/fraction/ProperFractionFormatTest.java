import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        assertEquals(expected, format.format(fraction));
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 / 4";
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, format.parse(source));
    }

    // Edge cases and exception handling scenarios

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3/4";
        format.parse(source);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-3 1/2";
        format.parse(source);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "A / 4";
        format.parse(source);
    }

    @Test
    public void testParseNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-3 / 4";
        Fraction expected = new Fraction(-3, 4);
        assertEquals(expected, format.parse(source));
    }

}