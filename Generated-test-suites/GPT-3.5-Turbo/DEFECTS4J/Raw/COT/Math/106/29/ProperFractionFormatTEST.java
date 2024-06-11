import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setup() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        assertEquals(expected, properFractionFormat.format(fraction));
    }
    
    @Test
    public void testFormatWithNegativeWhole() {
        Fraction fraction = new Fraction(-3, 4);
        String expected = "-3 / 4";
        assertEquals(expected, properFractionFormat.format(fraction));
    }
    
    @Test
    public void testFormatWithWholeNumber() {
        Fraction fraction = new Fraction(2, 1);
        String expected = "2";
        assertEquals(expected, properFractionFormat.format(fraction));
    }
    
    @Test
    public void testFormatWithNegativeWholeNumber() {
        Fraction fraction = new Fraction(-2, 1);
        String expected = "-2";
        assertEquals(expected, properFractionFormat.format(fraction));
    }
    
    @Test
    public void testParse() {
        String source = "3 / 4";
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, properFractionFormat.parse(source));
    }
    
    @Test
    public void testParseWithNegativeWhole() {
        String source = "-3 / 4";
        Fraction expected = new Fraction(-3, 4);
        assertEquals(expected, properFractionFormat.parse(source));
    }
    
    @Test
    public void testParseWithWholeNumber() {
        String source = "2";
        Fraction expected = new Fraction(2, 1);
        assertEquals(expected, properFractionFormat.parse(source));
    }
    
    @Test
    public void testParseWithNegativeWholeNumber() {
        String source = "-2";
        Fraction expected = new Fraction(-2, 1);
        assertEquals(expected, properFractionFormat.parse(source));
    }
    
    @Test
    public void testParseWithInvalidFraction() {
        String source = "-3 -1/2";
        try {
            properFractionFormat.parse(source);
            fail("ParseException expected but not thrown");
        } catch (ParseException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testFormatNullFraction() {
        Fraction fraction = null;
        String expected = "";
        assertEquals(expected, properFractionFormat.format(fraction));
    }
    
    @Test
    public void testParseNullString() {
        String source = null;
        assertNull(properFractionFormat.parse(source));
    }
}