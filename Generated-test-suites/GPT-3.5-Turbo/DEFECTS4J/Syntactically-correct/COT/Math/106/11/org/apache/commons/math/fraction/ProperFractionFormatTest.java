import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.text.DecimalFormat;

public class ProperFractionFormatTest {
    
    ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
        
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        assertEquals(expected, properFractionFormat.format(fraction));
    }
    
    @Test
    public void testParse_ValidProperFractionFormat() {
        String fractionString = "3 / 4";
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, properFractionFormat.parse(fractionString));
    }
    
    @Test(expected = ParseException.class)
    public void testParse_InvalidProperFractionFormat() {
        String fractionString = "3 4";
        properFractionFormat.parse(fractionString);
    }
    
    @Test
    public void testParse_ImproperFractionFormat() {
        String fractionString = "5 / 4";
        Fraction expected = new Fraction(5, 4);
        assertEquals(expected, properFractionFormat.parse(fractionString));
    }
    
    @Test
    public void testParse_WholeNumberFormat() {
        String fractionString = "2";
        Fraction expected = new Fraction(2);
        assertEquals(expected, properFractionFormat.parse(fractionString));
    }
    
    @Test(expected = ParseException.class)
    public void testParse_InvalidWholeNumberFormat() {
        String fractionString = "-2";
        properFractionFormat.parse(fractionString);
    }
    
    @Test(expected = ParseException.class)
    public void testParse_InvalidFractionFormat() {
        String fractionString = "3 /";
        properFractionFormat.parse(fractionString);
    }
    
    @Test(expected = ParseException.class)
    public void testParse_EmptyString() {
        String fractionString = "";
        properFractionFormat.parse(fractionString);
    }
    
}