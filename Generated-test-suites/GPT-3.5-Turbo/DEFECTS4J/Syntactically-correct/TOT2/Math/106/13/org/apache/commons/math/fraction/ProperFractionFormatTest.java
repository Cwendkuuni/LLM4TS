import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("1 / 2", format.format(new Fraction(1, 2)));
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("3 1/2");
        assertEquals(7, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("5", format.getWholeFormat().format(5));
    }

    @Test
    public void testInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNull(format.parse("3 -1/2"));
    }

    @Test
    public void testNoFractionPart() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("5");
        assertEquals(5, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }
}