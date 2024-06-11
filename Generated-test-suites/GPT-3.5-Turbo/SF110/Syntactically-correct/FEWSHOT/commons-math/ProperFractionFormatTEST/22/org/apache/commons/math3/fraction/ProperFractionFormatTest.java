// Testing the ProperFractionFormat class

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
    private ProperFractionFormat format;

    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        // Test formatting a fraction with positive numerator and denominator
        assertEquals("1 / 2", format.format(new Fraction(1, 2), new StringBuffer(), null).toString());

        // Test formatting a fraction with negative numerator and positive denominator
        assertEquals("-1 / 2", format.format(new Fraction(-1, 2), new StringBuffer(), null).toString());

        // Test formatting a fraction with positive numerator and negative denominator
        assertEquals("-1 / 2", format.format(new Fraction(1, -2), new StringBuffer(), null).toString());

        // Test formatting a fraction with negative numerator and negative denominator
        assertEquals("1 / 2", format.format(new Fraction(-1, -2), new StringBuffer(), null).toString());

        // Test formatting a whole number
        assertEquals("1", format.format(new Fraction(1, 1), new StringBuffer(), null).toString());
    }

    @Test
    public void testParse() {
        // Test parsing a valid fraction string
        assertEquals(new Fraction(1, 2), format.parse("1 / 2", null));

        // Test parsing a valid whole number string
        assertEquals(new Fraction(5, 1), format.parse("5", null));

        // Test parsing an invalid fraction string
        assertNull(format.parse("1 2", null));

        // Test parsing an invalid whole number string
        assertNull(format.parse("5 / 1", null));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullFormat() {
        format.setWholeFormat(null);
    }
}