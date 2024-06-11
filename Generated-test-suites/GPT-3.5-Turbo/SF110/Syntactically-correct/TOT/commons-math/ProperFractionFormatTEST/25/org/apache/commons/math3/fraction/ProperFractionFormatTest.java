import org.junit.Test;
import org.junit.Before;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
    private ProperFractionFormat format;
    
    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat_withWholeNumber() {
        Fraction fraction = new Fraction(5, 1);
        String expected = "5";
        assertEquals(expected, format.format(fraction));
    }
    
    @Test
    public void testFormat_withNegativeFraction() {
        Fraction fraction = new Fraction(-3, 5);
        String expected = "-3 / 5";
        assertEquals(expected, format.format(fraction));
    }
    
    @Test
    public void testFormat_withNegativeWholeNumber() {
        Fraction fraction = new Fraction(-7, 3);
        String expected = "-2 1 / 3";
        assertEquals(expected, format.format(fraction));
    }
    
    @Test
    public void testFormat_withZeroFraction() {
        Fraction fraction = new Fraction(0, 8);
        String expected = "0";
        assertEquals(expected, format.format(fraction));
    }
    
    @Test
    public void testFormat_withWholeAndFraction() {
        Fraction fraction = new Fraction(23, 4);
        String expected = "5 3 / 4";
        assertEquals(expected, format.format(fraction));
    }
    
    @Test
    public void testParse_withValidFractionInput() {
        String input = "3 / 7";
        Fraction expected = new Fraction(3, 7);
        assertEquals(expected, format.parse(input));
    }
    
    @Test
    public void testParse_withValidWholeAndFractionInput() {
        String input = "2 4 / 5";
        Fraction expected = new Fraction(14, 5);
        assertEquals(expected, format.parse(input));
    }
    
    @Test
    public void testParse_withNegativeWholeAndFractionInput() {
        String input = "-1 2 / 3";
        Fraction expected = new Fraction(-5, 3);
        assertEquals(expected, format.parse(input));
    }
    
    @Test
    public void testParse_withInvalidInput() {
        String input = "abc";
        Fraction expected = null;
        assertEquals(expected, format.parse(input));
    }
    
    @Test
    public void testParse_withInvalidFractionInput() {
        String input = "3 /";
        Fraction expected = null;
        assertEquals(expected, format.parse(input));
    }
    
    @Test
    public void testParse_withNegativeDenominatorInput() {
        String input = "2 / -4";
        Fraction expected = null;
        assertEquals(expected, format.parse(input));
    }
}