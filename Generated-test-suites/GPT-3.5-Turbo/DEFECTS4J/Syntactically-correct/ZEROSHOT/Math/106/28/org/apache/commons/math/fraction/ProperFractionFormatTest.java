import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        assertEquals("1 1/2", properFractionFormat.format(new Fraction(3, 2)));
        assertEquals("-1 1/2", properFractionFormat.format(new Fraction(-3, 2)));
        assertEquals("1 2/5", properFractionFormat.format(new Fraction(7, 5)));
        assertEquals("0", properFractionFormat.format(new Fraction(0)));
        assertEquals("1", properFractionFormat.format(new Fraction(1)));
        assertEquals("-1", properFractionFormat.format(new Fraction(-1)));
    }

    @Test
    public void testParse() {
        assertEquals(new Fraction(3, 2), properFractionFormat.parse("1 1/2"));
        assertEquals(new Fraction(-3, 2), properFractionFormat.parse("-1 1/2"));
        assertEquals(new Fraction(7, 5), properFractionFormat.parse("1 2/5"));
        assertNull(properFractionFormat.parse("abc"));
        assertNull(properFractionFormat.parse("1/2"));
        assertNull(properFractionFormat.parse("1/2/3"));
    }

}