import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        
        // Test formatting positive fraction
        Fraction fraction1 = new Fraction(3, 4);
        String expected1 = "3 / 4";
        String actual1 = formatter.format(fraction1);
        assertEquals(expected1, actual1);
        
        // Test formatting negative fraction
        Fraction fraction2 = new Fraction(-2, 5);
        String expected2 = "-2 / 5";
        String actual2 = formatter.format(fraction2);
        assertEquals(expected2, actual2);
        
        // Test formatting whole number
        Fraction fraction3 = new Fraction(7);
        String expected3 = "7";
        String actual3 = formatter.format(fraction3);
        assertEquals(expected3, actual3);
        
        // Test formatting zero
        Fraction fraction4 = new Fraction(0);
        String expected4 = "0";
        String actual4 = formatter.format(fraction4);
        assertEquals(expected4, actual4);
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        
        // Test parsing positive fraction
        String input1 = "3 / 5";
        Fraction expected1 = new Fraction(3, 5);
        Fraction actual1 = formatter.parse(input1);
        assertEquals(expected1, actual1);
        
        // Test parsing negative fraction
        String input2 = "-2 / 7";
        Fraction expected2 = new Fraction(-2, 7);
        Fraction actual2 = formatter.parse(input2);
        assertEquals(expected2, actual2);
        
        // Test parsing whole number
        String input3 = "9";
        Fraction expected3 = new Fraction(9);
        Fraction actual3 = formatter.parse(input3);
        assertEquals(expected3, actual3);
        
        // Test parsing improper fraction
        String input4 = "7 / 3";
        Fraction actual4 = formatter.parse(input4);
        assertNull(actual4);
        
        // Test parsing invalid input
        String input5 = "abc";
        Fraction actual5 = formatter.parse(input5);
        assertNull(actual5);
    }
    
    @Test
    public void testEquals() {
        ProperFractionFormat formatter1 = new ProperFractionFormat();
        ProperFractionFormat formatter2 = new ProperFractionFormat();
        ProperFractionFormat formatter3 = new ProperFractionFormat();
        formatter3.setWholeFormat(NumberFormat.getIntegerInstance());
        
        // Test equal formatter objects
        assertTrue(formatter1.equals(formatter2));
        
        // Test non-equal formatter objects
        assertFalse(formatter1.equals(formatter3));
    }
    
    @Test
    public void testHashCode() {
        ProperFractionFormat formatter1 = new ProperFractionFormat();
        ProperFractionFormat formatter2 = new ProperFractionFormat();
        
        // Test equal formatter objects have the same hash code
        assertEquals(formatter1.hashCode(), formatter2.hashCode());
    }
}