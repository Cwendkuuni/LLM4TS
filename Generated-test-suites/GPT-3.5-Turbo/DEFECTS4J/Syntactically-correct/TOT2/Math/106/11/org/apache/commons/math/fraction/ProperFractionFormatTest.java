import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    public void testFormatWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(7, 4);
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, null);
        assertEquals("1 3 / 4", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("3 / 4", pos);
        assertNotNull(result);
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testInvalidParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("3 4", pos);
        assertNull(result);
    }

    @Test
    public void testInvalidParseFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("3 / 4 5", pos);
        assertNull(result);
    }

    @Test
    public void testInvalidWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("-3 1 / 2", pos); //Invalid whole number format
        assertNull(result);
    }

    @Test
    public void testInvalidFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse("3 /", pos); //Invalid fraction format
        assertNull(result);
    }
    
}