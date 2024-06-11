import static org.junit.Assert.*;

import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        assertEquals("3 / 2", format.format(fraction));
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("3 1/2");
        assertEquals(7, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testExceptionHandling() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Invalid input format
        try {
            format.parse("3/2");
            fail("ParseException expected");
        } catch (ParseException e) {
            // Expected exception
        }
        
        try {
            format.parse("-3 -1/2");
            fail("ParseException expected");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testEdgeCases() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Testing max values for numerator and denominator
        Fraction maxFraction = new Fraction(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals("1", format.format(maxFraction));

        // Testing negative whole number
        Fraction negativeFraction = new Fraction(-3, 2);
        assertEquals("-1 1/2", format.format(negativeFraction));
    }

}