import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        assertEquals("-3 1/2", properFractionFormat.format(new Fraction(-7, 2), new StringBuffer(), null).toString());
    }

    @Test
    public void testParse_ValidInput() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("1 5/2", pos);
        assertNotNull(fraction);
        assertEquals(11, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testParse_InvalidInput() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("1 -5/2", pos);
        assertNull(fraction);
    }

}