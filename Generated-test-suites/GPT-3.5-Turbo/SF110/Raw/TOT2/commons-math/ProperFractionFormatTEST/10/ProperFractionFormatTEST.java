import org.junit.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatConstructor1() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        assertNotNull(formatter);
    }

    @Test
    public void testProperFractionFormatConstructor2() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat formatter = new ProperFractionFormat(format);
        assertNotNull(formatter);
    }

    @Test
    public void testProperFractionFormatConstructor3() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat formatter = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(formatter);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(3, 2);
        StringBuffer result = formatter.format(fraction, buffer, pos);
        assertEquals("1 1 / 2", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = formatter.parse("1 1 / 2", pos);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat format = NumberFormat.getNumberInstance();
        ProperFractionFormat formatter = new ProperFractionFormat(format);
        assertEquals(format, formatter.getWholeFormat());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        NumberFormat format = NumberFormat.getNumberInstance();
        formatter.setWholeFormat(format);
        assertEquals(format, formatter.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        formatter.setWholeFormat(null);
    }
}