import org.junit.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat_withWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        
        format.format(fraction, buffer, fieldPosition);
        
        assertEquals("1 1/2", buffer.toString());
    }
    
    @Test
    public void testFormat_withNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-2, 3);
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        
        format.format(fraction, buffer, fieldPosition);
        
        assertEquals("-2 / 3", buffer.toString());
    }
    
    @Test
    public void testFormat_withNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-5, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        
        format.format(fraction, buffer, fieldPosition);
        
        assertEquals("-2 1/2", buffer.toString());
    }
    
    @Test
    public void testParse_withValidFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 1/2";
        ParsePosition parsePosition = new ParsePosition(0);
        
        Fraction fraction = format.parse(source, parsePosition);
        
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }
    
    @Test
    public void testParse_withInvalidFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 1//2";
        ParsePosition parsePosition = new ParsePosition(0);
        
        Fraction fraction = format.parse(source, parsePosition);
        
        assertNull(fraction);
        assertEquals(1, parsePosition.getErrorIndex());
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        
        assertNotNull(wholeFormat);
    }
    
    @Test
    public void testSetWholeFormat_withNullFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        try {
            format.setWholeFormat(null);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            assertEquals("Whole format must not be null", e.getMessage());
        }
    }
}