import org.junit.Test;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertEquals("1 / 2", pff.format(new Fraction(1, 2), new StringBuffer(), null).toString());
        assertEquals("-3 / 4", pff.format(new Fraction(-3, 4), new StringBuffer(), null).toString());
        assertEquals("2 / 1", pff.format(new Fraction(2, 1), new StringBuffer(), null).toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertNotNull(pff.getWholeFormat());
    }

    @Test
    public void testParse() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction fraction = pff.parse("1 / 2", null);
        assertEquals(new Fraction(1, 2), fraction);
        Fraction fraction2 = pff.parse("3", null);
        assertEquals(new Fraction(3, 1), fraction2);
        Fraction fraction3 = pff.parse("-5 / 2", null);
        assertEquals(new Fraction(-5, 2), fraction3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat pff = new ProperFractionFormat();
        pff.setWholeFormat(null);
    }

}