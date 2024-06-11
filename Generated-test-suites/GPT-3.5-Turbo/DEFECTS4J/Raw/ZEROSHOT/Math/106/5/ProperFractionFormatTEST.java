import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    private ProperFractionFormat fractionFormat;

    @Before
    public void setUp() {
        fractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer buffer = new StringBuffer();
        fractionFormat.format(fraction, buffer, null);
        assertEquals("3 / 4", buffer.toString());
    }

    @Test
    public void testFormatWithWhole() {
        Fraction fraction = new Fraction(5, 4);
        StringBuffer buffer = new StringBuffer();
        fractionFormat.format(fraction, buffer, null);
        assertEquals("1 1/4", buffer.toString());
    }

    @Test
    public void testParse() {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = fractionFormat.parse(source, pos);
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testParseWithWhole() {
        String source = "1 1/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = fractionFormat.parse(source, pos);
        assertNotNull(fraction);
        assertEquals(5, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testParseInvalidSource() {
        String source = "invalid";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = fractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }

    @Test
    public void testParseInvalidSourceWithInvalidWhole() {
        String source = "3 1/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = fractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(2, pos.getErrorIndex());
    }

    @Test
    public void testParseInvalidSourceWithInvalidDenominator() {
        String source = "3 / invalid";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = fractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(4, pos.getErrorIndex());
    }

    @Test
    public void testParseInvalidSourceWithInvalidFormat() {
        String source = "invalid / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = fractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(7, pos.getErrorIndex());
    }
}