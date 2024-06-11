import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1/2";
        assertEquals(expected, format.format(fraction));
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction expected = new Fraction(1, 2);
        Fraction parsed = format.parse("1/2");
        assertEquals(expected, parsed);
    }
    
    @Test
    public void testEdgeCases() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test negative whole number
        Fraction fraction1 = new Fraction(-5, 4);
        String expected1 = "-1 1/4";
        assertEquals(expected1, format.format(fraction1));
        
        // Test negative numerator and denominator
        Fraction fraction2 = new Fraction(-3, -2);
        String expected2 = "1 1/2";
        assertEquals(expected2, format.format(fraction2));
    }
    
    @Test
    public void testExceptionHandling() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test invalid format (should throw ParseException)
        try {
            format.parse("3 - 1/2");
            fail("ParseException should have been thrown");
        } catch (ParseException e) {
            // Expected exception
        }
    }
}