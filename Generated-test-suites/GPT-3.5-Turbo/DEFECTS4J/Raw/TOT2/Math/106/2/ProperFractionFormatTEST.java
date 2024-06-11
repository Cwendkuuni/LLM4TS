import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("1 2/3", format.format(new Fraction(5, 3)).toString());
    }

    @Test
    public void testParse_validFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction expected = new Fraction(-7, 2);
        assertEquals(expected, format.parse("-3 1/2"));
    }

    @Test
    public void testParse_invalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNull(format.parse("-3 -1/2"));
    }
}