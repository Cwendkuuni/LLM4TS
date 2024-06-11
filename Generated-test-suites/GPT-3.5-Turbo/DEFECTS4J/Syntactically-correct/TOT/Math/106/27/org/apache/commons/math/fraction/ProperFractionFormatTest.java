import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {
    
    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        // Verify that format is not null
        assertNotNull(format);
    }
    
    @Test
    public void testCustomConstructor() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance());
        // Verify that format is not null
        assertNotNull(format);
    }
    
    @Test
    public void testFormatWithPositiveFraction() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance());
        Fraction fraction = new Fraction(3, 4);
        
        String expected = "3 / 4";
        String actual = format.format(fraction);
        
        // Verify that the actual formatted string matches the expected string
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance());
        Fraction fraction = new Fraction(-5, 7);
        
        String expected = "-5 / 7";
        String actual = format.format(fraction);
        
        // Verify that the actual formatted string matches the expected string
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance());
        Fraction fraction = new Fraction(2, 1);
        
        String expected = "2";
        String actual = format.format(fraction);
        
        // Verify that the actual formatted string matches the expected string
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance());
        Fraction fraction = new Fraction(-4, 1);
        
        String expected = "-4";
        String actual = format.format(fraction);
        
        // Verify that the actual formatted string matches the expected string
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseWithValidInput() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance());
        String input = "5 / 8";
        Fraction expected = new Fraction(5, 8);
        
        Fraction actual = format.parse(input);
        
        // Verify that the actual parsed fraction matches the expected fraction
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseWithInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance());
        String input = "3 -1/4";
        
        Fraction actual = format.parse(input);
        
        // Verify that the actual parsed fraction is null (indicating an invalid input)
        assertNull(actual);
    }
    
    @Test
    public void testParseWithEmptyInput() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance());
        String input = "";
        
        Fraction actual = format.parse(input);
        
        // Verify that the actual parsed fraction is null (indicating an invalid input)
        assertNull(actual);
    }
}