import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test case 1: Proper fraction
        String result1 = format.format(new Fraction(3, 2)).toString();
        assertEquals("3 / 2", result1);
        
        // Test case 2: Whole number
        String result2 = format.format(new Fraction(4)).toString();
        assertEquals("4", result2);
        
        // Test case 3: Negative proper fraction
        String result3 = format.format(new Fraction(-5, 3)).toString();
        assertEquals("-5 / 3", result3);
        
        // Test case 4: Negative whole number
        String result4 = format.format(new Fraction(-6)).toString();
        assertEquals("-6", result4);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test case 1: Proper fraction
        String source1 = "3 / 2";
        Fraction result1 = format.parse(source1);
        assertEquals(new Fraction(3, 2), result1);
        
        // Test case 2: Whole number
        String source2 = "4";
        Fraction result2 = format.parse(source2);
        assertEquals(new Fraction(4), result2);
        
        // Test case 3: Negative proper fraction
        String source3 = "-5 / 3";
        Fraction result3 = format.parse(source3);
        assertEquals(new Fraction(-5, 3), result3);
        
        // Test case 4: Negative whole number
        String source4 = "-6";
        Fraction result4 = format.parse(source4);
        assertEquals(new Fraction(-6), result4);
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test case 1: Default number format
        NumberFormat result1 = format.getWholeFormat();
        assertNotNull(result1);
        
        // Test case 2: Custom number format
        NumberFormat customFormat = NumberFormat.getIntegerInstance();
        format.setWholeFormat(customFormat);
        NumberFormat result2 = format.getWholeFormat();
        assertEquals(customFormat, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test case 1: Null format
        NumberFormat nullFormat = null;
        format.setWholeFormat(nullFormat);
    }
}