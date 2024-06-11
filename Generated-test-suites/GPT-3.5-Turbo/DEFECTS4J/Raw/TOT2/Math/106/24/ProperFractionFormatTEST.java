import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1/2";
        assertEquals(expected, format.format(fraction));
    }

    @Test
    public void testFormatWholeZero() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, 3);
        String expected = "2/3";
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
    public void testParseNoWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "1/2";
        Fraction expected = new Fraction(1, 2);
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParseNegativeWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-3 1/2";
        Fraction expected = new Fraction(-7, 2);
        assertEquals(expected, format.parse(input));
    }

    @Test
    public void testParseInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-3 -1/2";
        assertNull(format.parse(input));
    }
}