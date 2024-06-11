import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        // Typical use case
        ProperFractionFormat properFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        StringBuffer formattedFraction = properFormat.format(fraction, buffer, fieldPosition);
        assertEquals("3 / 2", formattedFraction.toString());
    }
    
    @Test
    public void testParse() {
        // Typical use case
        ProperFractionFormat properFormat = new ProperFractionFormat();
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsedFraction = properFormat.parse("3 1/2", parsePosition);
        assertEquals(new Fraction(7, 2), parsedFraction);
        
        // Edge case - negative whole, numerator, and denominator
        ParsePosition parsePosition2 = new ParsePosition(0);
        Fraction parsedFraction2 = properFormat.parse("-3 -1/2", parsePosition2);
        assertNull(parsedFraction2); // Expecting ParseException
        
        // Error scenario - invalid fraction format
        ParsePosition parsePosition3 = new ParsePosition(0);
        Fraction parsedFraction3 = properFormat.parse("3 1/2 3/4", parsePosition3);
        assertNull(parsedFraction3); // Expecting ParseException
    }
    
}