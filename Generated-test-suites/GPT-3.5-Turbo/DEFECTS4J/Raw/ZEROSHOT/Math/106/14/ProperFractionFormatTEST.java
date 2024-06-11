import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("3 / 2", format.format(new Fraction(3, 2), new StringBuffer(), null).toString());
        assertEquals("7 / 5", format.format(new Fraction(7, 5), new StringBuffer(), null).toString());
        assertEquals("1 / 3", format.format(new Fraction(1, 3), new StringBuffer(), null).toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals(new Fraction(3, 2), format.parse("3 / 2", null));
        assertEquals(new Fraction(7, 5), format.parse("7 / 5", null));
        assertEquals(new Fraction(1, 3), format.parse("1 / 3", null));
    }

    @Test
    public void testParseInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNull(format.parse("3 -1/2", null));
        assertNull(format.parse("3 1 / 2", null));
    }
}