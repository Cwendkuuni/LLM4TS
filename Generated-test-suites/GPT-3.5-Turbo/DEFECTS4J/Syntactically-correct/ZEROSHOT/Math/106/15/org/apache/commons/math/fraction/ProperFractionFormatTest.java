import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 4);
        String expected = "1 1 / 4";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 / 4";
        Fraction expected = new Fraction(3, 4);
        Fraction result = format.parse(source);
        assertEquals(expected, result);
    }

    @Test
    public void testParseWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 1 / 4";
        Fraction expected = new Fraction(5, 4);
        Fraction result = format.parse(source);
        assertEquals(expected, result);
    }

    @Test
    public void testParseInvalidFractionExpression() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 - 1 / 2";
        Fraction result = format.parse(source);
        assertNull(result);
    }
}