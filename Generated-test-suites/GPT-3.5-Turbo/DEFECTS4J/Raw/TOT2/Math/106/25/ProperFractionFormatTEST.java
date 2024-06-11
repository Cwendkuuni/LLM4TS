
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(7, 2);
        StringBuffer buffer = new StringBuffer();
        format.format(fraction, buffer, null);
        assertEquals("3 1/2", buffer.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("3 1/2", pos);
        assertNotNull(fraction);
        assertEquals(new Fraction(7, 2), fraction);
    }

    @Test
    public void testParseErrorScenario() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("3 -1/2", pos);
        assertNotNull(fraction);
        assertEquals(new Fraction(-5, 2), fraction);
    }
}
