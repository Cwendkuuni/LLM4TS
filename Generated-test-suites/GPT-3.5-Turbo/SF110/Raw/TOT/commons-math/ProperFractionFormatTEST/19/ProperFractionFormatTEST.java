import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    private ProperFractionFormat pff;

    @Before
    public void setUp() {
        pff = new ProperFractionFormat();
    }

    @Test
    public void testFormat_WithFraction_ReturnsFormattedString() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer expected = new StringBuffer("3 / 4");
        StringBuffer result = pff.format(fraction, new StringBuffer(), null);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testFormat_WithWholeNumber_ReturnsFormattedString() {
        Fraction fraction = new Fraction(10, 1);
        StringBuffer expected = new StringBuffer("10");
        StringBuffer result = pff.format(fraction, new StringBuffer(), null);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testFormat_WithNegativeFraction_ReturnsFormattedString() {
        Fraction fraction = new Fraction(-2, 3);
        StringBuffer expected = new StringBuffer("-2 / 3");
        StringBuffer result = pff.format(fraction, new StringBuffer(), null);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testParse_WithValidFraction_ReturnsFractionObject() {
        String source = "5 / 10";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(1, 2);
        Fraction result = pff.parse(source, pos);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_WithInvalidFraction_ReturnsNull() {
        String source = "2 / -1";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = pff.parse(source, pos);
        assertNull(result);
    }

    @Test
    public void testParse_WithInvalidSource_ReturnsNull() {
        String source = "abc";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = pff.parse(source, pos);
        assertNull(result);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat_WithNullFormat_ThrowsNullArgumentException() {
        NumberFormat format = null;
        pff.setWholeFormat(format);
    }

}