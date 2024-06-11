import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFormat;

    @Before
    public void setUp() {
        properFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormatWithoutWholeNumber() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("1 / 2", properFormat.format(fraction));
    }

    @Test
    public void testFormatWithWholeNumber() {
        Fraction fraction = new Fraction(7, 2);
        assertEquals("3 1 / 2", properFormat.format(fraction));
    }

    @Test
    public void testParseValidFraction() {
        Fraction fraction = properFormat.parse("3 1 / 2");
        assertNotNull(fraction);
        assertEquals(7, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testParseInvalidFraction() {
        Fraction fraction = properFormat.parse("3 1 2");
        assertNull(fraction);
    }

    // Add more test cases to cover edge cases and exception handling scenarios
    
}