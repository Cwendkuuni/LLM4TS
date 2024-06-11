import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 2);
        String result = format.format(fraction);
        assertEquals("2 1/2", result);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("2 1/2");
        assertEquals(new Fraction(5, 2), result);
    }

    @Test
    public void testParseNegativeWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("-2 1/2");
        assertEquals(new Fraction(-5, 2), result);
    }

    @Test
    public void testParseNoWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("1/2");
        assertEquals(new Fraction(1, 2), result);
    }
}