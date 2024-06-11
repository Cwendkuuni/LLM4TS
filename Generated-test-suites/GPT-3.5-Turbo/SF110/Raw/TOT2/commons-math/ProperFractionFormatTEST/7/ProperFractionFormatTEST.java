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
        // Typical use case
        assertEquals("1 / 2", properFractionFormat.format(new Fraction(1, 2), new StringBuffer(), null).toString());
        
        // Edge case
        assertEquals("5 / 2", properFractionFormat.format(new Fraction(5, 2), new StringBuffer(), null).toString());
        
        // Error scenario
        assertThrows(NullPointerException.class, () -> properFractionFormat.format(null, new StringBuffer(), null));
    }

    @Test
    public void testParse() {
        // Typical use case
        assertEquals(new Fraction(3, 4), properFractionFormat.parse("3 / 4", new ParsePosition(0)));

        // Edge case
        assertEquals(new Fraction(1, 1), properFractionFormat.parse("1", new ParsePosition(0)));
        
        // Error scenario
        assertNull(properFractionFormat.parse("abc", new ParsePosition(0)));
    }

    @Test
    public void testWholeFormat() {
        // Typical use case
        properFractionFormat.setWholeFormat(NumberFormat.getIntegerInstance());
        assertEquals(NumberFormat.getIntegerInstance(), properFractionFormat.getWholeFormat());
        
        // Edge case
        assertNull(properFractionFormat.getWholeFormat().parse("abc", new ParsePosition(0)));
        
        // Error scenario
        assertThrows(NullArgumentException.class, () -> properFractionFormat.setWholeFormat(null));
    }
}