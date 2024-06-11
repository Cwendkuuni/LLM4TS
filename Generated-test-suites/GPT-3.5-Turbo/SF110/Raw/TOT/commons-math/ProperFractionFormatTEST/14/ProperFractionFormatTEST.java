import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    // Test case for format() method
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Typical use case
        Fraction fraction1 = new Fraction(3, 4);
        StringBuffer expectedOutput1 = new StringBuffer("3 / 4");
        assertEquals(expectedOutput1.toString(), format.format(fraction1, new StringBuffer(), null).toString());
        
        // Edge case - Fraction is a whole number
        Fraction fraction2 = new Fraction(5, 5);
        StringBuffer expectedOutput2 = new StringBuffer("1");
        assertEquals(expectedOutput2.toString(), format.format(fraction2, new StringBuffer(), null).toString());
        
        // Error scenario - Fraction is negative
        Fraction fraction3 = new Fraction(-2, 3);
        StringBuffer expectedOutput3 = new StringBuffer("-2 / 3");
        assertEquals(expectedOutput3.toString(), format.format(fraction3, new StringBuffer(), null).toString());
    }

    // Test case for getWholeFormat() method
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        NumberFormat wholeFormat = format.getWholeFormat();
        
        // Typical use case - Whole format should be the default number format
        assertEquals(NumberFormat.getInstance(), wholeFormat);
    }

    // Test case for parse() method
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Typical use case - Parsing a fraction in the format "3 / 4"
        String input1 = "3 / 4";
        Fraction expectedOutput1 = new Fraction(3, 4);
        assertEquals(expectedOutput1, format.parse(input1, null));
        
        // Edge case - Parsing a whole number "5"
        String input2 = "5";
        Fraction expectedOutput2 = new Fraction(5, 1);
        assertEquals(expectedOutput2, format.parse(input2, null));
        
        // Error scenario - Parsing a negative fraction "-2 / 3"
        String input3 = "-2 / 3";
        assertNull(format.parse(input3, null));
        
        // Error scenario - Parsing an invalid fraction "3 / a"
        String input4 = "3 / a";
        assertNull(format.parse(input4, null));
    }

    // Test case for setWholeFormat() method
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        NumberFormat wholeFormat = NumberFormat.getCurrencyInstance();
        format.setWholeFormat(wholeFormat);
        
        // Whole format should be set to the specified number format
        assertEquals(wholeFormat, format.getWholeFormat());
    }

}