import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(3, 2);

        StringBuffer result = format.format(fraction, sb, pos);

        assertEquals("1 1 / 2", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("1 1 / 2", pos);

        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat numberFormat = NumberFormat.getInstance();
        format.setWholeFormat(numberFormat);

        assertEquals(numberFormat, format.getWholeFormat());
    }

    @Test
    public void testNullWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        try {
            format.setWholeFormat(null);
            fail("Exception not thrown");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

}