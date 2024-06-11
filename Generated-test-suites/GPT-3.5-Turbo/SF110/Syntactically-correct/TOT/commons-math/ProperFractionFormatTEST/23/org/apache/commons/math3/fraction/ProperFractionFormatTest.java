import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.MathParseException;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Typical use case: fraction with whole part
        Fraction fraction1 = new Fraction(3, 2);
        String expected1 = "1 1 / 2";
        assertEquals(expected1, format.format(fraction1));
        
        // Typical use case: fraction without whole part
        Fraction fraction2 = new Fraction(1, 3);
        String expected2 = "1 / 3";
        assertEquals(expected2, format.format(fraction2));
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Typical use case: parsing fraction with whole part
        String input1 = "1 1 / 2";
        Fraction expected1 = new Fraction(3, 2);
        assertEquals(expected1, format.parse(input1));
        
        // Typical use case: parsing fraction without whole part
        String input2 = "3 / 4";
        Fraction expected2 = new Fraction(3, 4);
        assertEquals(expected2, format.parse(input2));
        
        // Error scenario: parsing invalid fraction
        String input3 = "1 2 /";
        try {
            format.parse(input3);
            fail("Expected MathParseException");
        } catch (MathParseException ex) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Typical use case: getting default whole format
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Typical use case: setting valid whole format
        NumberFormat newWholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(newWholeFormat);
        assertEquals(newWholeFormat, format.getWholeFormat());
        
        // Error scenario: setting null whole format
        try {
            format.setWholeFormat(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException ex) {
            // Expected exception
        }
    }
}