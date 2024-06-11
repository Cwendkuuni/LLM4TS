import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat format;
    
    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        // Test case 1: Proper fraction with whole number
        Fraction fraction1 = new Fraction(3, 2);
        String expected1 = "1 1/2";
        assertEquals(expected1, format.format(fraction1));
        
        // Test case 2: Proper fraction without whole number
        Fraction fraction2 = new Fraction(1, 3);
        String expected2 = "1/3";
        assertEquals(expected2, format.format(fraction2));
        
        // Test case 3: Proper fraction with negative value
        Fraction fraction3 = new Fraction(-5, 2);
        String expected3 = "-2 1/2";
        assertEquals(expected3, format.format(fraction3));
    }
    
    @Test
    public void testParse() {
        // Test case 1: Proper fraction with whole number
        String source1 = "2 3/4";
        Fraction expected1 = new Fraction(11, 4);
        assertEquals(expected1, format.parse(source1));
        
        // Test case 2: Proper fraction without whole number
        String source2 = "1/5";
        Fraction expected2 = new Fraction(1, 5);
        assertEquals(expected2, format.parse(source2));
        
        // Test case 3: Invalid input with minus sign in denominator
        String source3 = "3 -1/2";
        assertNull(format.parse(source3));
        
        // Test case 4: Invalid input with multiple minus signs
        String source4 = "-2 -1/2";
        assertNull(format.parse(source4));
    }
    
    @Test
    public void testSetWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}