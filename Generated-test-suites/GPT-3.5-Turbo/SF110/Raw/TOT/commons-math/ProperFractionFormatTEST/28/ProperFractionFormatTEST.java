import org.apache.commons.math3.fraction.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat_withWholeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1 / 2";
        
        assertEquals(expected, properFractionFormat.format(fraction));
    }
    
    @Test
    public void testFormat_withoutWholeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        String expected = "1 / 2";
        
        assertEquals(expected, properFractionFormat.format(fraction));
    }
    
    @Test
    public void testFormat_withNegativeFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(-1, 2);
        String expected = "-1 / 2";
        
        assertEquals(expected, properFractionFormat.format(fraction));
    }
    
    @Test
    public void testFormat_withWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(NumberFormat.getIntegerInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());
        Fraction fraction = new Fraction(3, 2);
        String expected = "1.5";
        
        assertEquals(expected, properFractionFormat.format(fraction));
    }
    
    @Test
    public void testParse_validFractionString() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "1 1 / 2";
        Fraction expected = new Fraction(3, 2);
        
        assertEquals(expected, properFractionFormat.parse(fractionString));
    }
    
    @Test
    public void testParse_validFractionString_withoutWholeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "1 / 2";
        Fraction expected = new Fraction(1, 2);
        
        assertEquals(expected, properFractionFormat.parse(fractionString));
    }
    
    @Test
    public void testParse_validFractionString_withNegativeFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "-1 / 2";
        Fraction expected = new Fraction(-1, 2);
        
        assertEquals(expected, properFractionFormat.parse(fractionString));
    }
    
    @Test
    public void testParse_validFractionString_withWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(NumberFormat.getIntegerInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());
        String fractionString = "1.5";
        Fraction expected = new Fraction(3, 2);
        
        assertEquals(expected, properFractionFormat.parse(fractionString));
    }
    
    @Test
    public void testParse_invalidFractionString() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "1 ab / 2";
        
        assertNull(properFractionFormat.parse(fractionString));
    }
}