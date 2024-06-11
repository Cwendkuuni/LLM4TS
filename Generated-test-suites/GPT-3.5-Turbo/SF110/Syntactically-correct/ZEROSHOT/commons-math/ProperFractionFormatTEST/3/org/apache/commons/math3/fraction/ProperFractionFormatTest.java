import org.junit.Test;

import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test case 1: Fraction with whole part
        assertEquals("3 1 / 2", format.format(new Fraction(7, 2)));
        
        // Test case 2: Fraction without whole part
        assertEquals("1 / 2", format.format(new Fraction(1, 2)));
        
        // Test case 3: Fraction with negative numerator
        assertEquals("-1 / 2", format.format(new Fraction(-1, 2)));
        
        // Test case 4: Fraction with negative whole part
        assertEquals("-2 3 / 4", format.format(new Fraction(-11, 4)));
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test case 1: Proper fraction with whole part
        assertEquals(new Fraction(3, 2), format.parse("3 1 / 2"));
        
        // Test case 2: Proper fraction without whole part
        assertEquals(new Fraction(1, 2), format.parse("1 / 2"));
        
        // Test case 3: Negative proper fraction
        assertEquals(new Fraction(-1, 2), format.parse("-1 / 2"));
        
        // Test case 4: Improper fraction
        assertNull(format.parse("7 / 2"));
        
        // Test case 5: Invalid format
        assertNull(format.parse("2 % 3 / 4"));
    }
}