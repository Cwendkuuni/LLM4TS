import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test a fraction without whole number
        Fraction fraction1 = new Fraction(1, 2);
        String result1 = format.format(fraction1);
        assertEquals("1 / 2", result1);
        
        // Test a fraction with a positive whole number
        Fraction fraction2 = new Fraction(3, 4);
        String result2 = format.format(fraction2);
        assertEquals("3 / 4", result2);
        
        // Test a fraction with a negative whole number
        Fraction fraction3 = new Fraction(-5, 6);
        String result3 = format.format(fraction3);
        assertEquals("-5 / 6", result3);
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test parsing a fraction without whole number
        Fraction result1 = format.parse("1 / 2");
        assertNotNull(result1);
        assertEquals(1, result1.getNumerator());
        assertEquals(2, result1.getDenominator());
        
        // Test parsing a fraction with a positive whole number
        Fraction result2 = format.parse("3 / 4");
        assertNotNull(result2);
        assertEquals(3, result2.getNumerator());
        assertEquals(4, result2.getDenominator());
        
        // Test parsing a fraction with a negative whole number
        Fraction result3 = format.parse("-5 / 6");
        assertNotNull(result3);
        assertEquals(-5, result3.getNumerator());
        assertEquals(6, result3.getDenominator());
        
        // Test parsing an invalid fraction with a negative whole number
        Fraction result4 = format.parse("-5 -1/2");
        assertNull(result4);
        
        // Test parsing an invalid fraction with an invalid format
        Fraction result5 = format.parse("abc");
        assertNull(result5);
    }
}