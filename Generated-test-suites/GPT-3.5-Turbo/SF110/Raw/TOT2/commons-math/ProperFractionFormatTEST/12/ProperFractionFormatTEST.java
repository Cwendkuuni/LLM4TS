import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer result = new StringBuffer();
        format.format(new Fraction(3, 2), result, null);
        assertEquals("1 1 / 2", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("1 1 / 2", null);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testWholeFormatConstructor() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, NumberFormat.getInstance(), NumberFormat.getInstance());
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getCurrencyInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test
    public void testNullWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        try {
            format.setWholeFormat(null);
            fail("Exception not thrown for null whole format");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testInvalidInputParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("1 1", pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
    }
}