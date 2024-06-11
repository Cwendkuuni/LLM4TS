import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;

import static org.junit.Assert.*;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat fractionFormat;
    
    @Before
    public void setup() {
        fractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        String formattedFraction = fractionFormat.format(new Fraction(3, 4));
        assertEquals("3 / 4", formattedFraction);
    }
    
    @Test
    public void testFormatWithWholeNumber() {
        String formattedFraction = fractionFormat.format(new Fraction(5, 4));
        assertEquals("1 1/4", formattedFraction);
    }
    
    @Test
    public void testFormatWithNegativeFraction() {
        String formattedFraction = fractionFormat.format(new Fraction(-3, 4));
        assertEquals("-3 / 4", formattedFraction);
    }
    
    @Test
    public void testParse() {
        Fraction parsedFraction = fractionFormat.parse("3 / 4");
        assertEquals(new Fraction(3, 4), parsedFraction);
    }
    
    @Test
    public void testParseWithWholeNumber() {
        Fraction parsedFraction = fractionFormat.parse("1 1/4");
        assertEquals(new Fraction(5, 4), parsedFraction);
    }
    
    @Test
    public void testParseWithNegativeFraction() {
        Fraction parsedFraction = fractionFormat.parse("-3 / 4");
        assertEquals(new Fraction(-3, 4), parsedFraction);
    }
    
    @Test
    public void testParseWithInvalidFraction() {
        Fraction parsedFraction = fractionFormat.parse("4 3");
        assertNull(parsedFraction);
    }
    
    @Test
    public void testParseWithInvalidFormat() {
        Fraction parsedFraction = fractionFormat.parse("1/2/3");
        assertNull(parsedFraction);
    }
}