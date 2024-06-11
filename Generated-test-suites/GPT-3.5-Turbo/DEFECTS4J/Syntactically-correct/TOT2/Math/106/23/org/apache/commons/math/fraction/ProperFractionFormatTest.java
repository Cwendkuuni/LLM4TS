import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 2);
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, pos);
        assertEquals("1 1/2", result.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test
    public void testParse() {
        String source = "2 3/4";
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(11, 4), result);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat newFormat = NumberFormat.getInstance();
        format.setWholeFormat(newFormat);
        NumberFormat result = format.getWholeFormat();
        assertEquals(newFormat, result);
    }

    @Test
    public void testInvalidFormat() {
        String source = "-2 3/4"; // Invalid format with a negative sign in numerator
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testNoFractionFormat() {
        String source = "5"; // No fraction format, should return whole number
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(5, 1), result);
    }
}