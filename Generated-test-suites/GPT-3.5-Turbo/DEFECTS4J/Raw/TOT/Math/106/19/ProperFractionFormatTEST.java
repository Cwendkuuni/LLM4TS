import static org.junit.Assert.*;
import java.text.NumberFormat;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {
    
    private static ProperFractionFormat formatter;
    
    @BeforeClass
    public static void setUp() {
        formatter = new ProperFractionFormat(NumberFormat.getInstance());
    }
    
    @Test
    public void format_shouldReturnFormattedFractionString() {
        String fractionString = formatter.format(new Fraction(3, 4));
        assertEquals("3 / 4", fractionString);
    }
    
    @Test
    public void format_withNegativeFraction_shouldReturnFormattedFractionString() {
        String fractionString = formatter.format(new Fraction(-1, 2));
        assertEquals("-1 / 2", fractionString);
    }
    
    @Test
    public void format_withZeroNumerator_shouldReturnFormattedFractionString() {
        String fractionString = formatter.format(new Fraction(0, 7));
        assertEquals("0 / 7", fractionString);
    }
    
    @Test
    public void parse_shouldReturnParsedFraction() {
        Fraction fraction = formatter.parse("3 / 4");
        assertEquals(new Fraction(3, 4), fraction);
    }
    
    @Test
    public void parse_withNegativeFractionString_shouldReturnParsedFraction() {
        Fraction fraction = formatter.parse("-1 / 2");
        assertEquals(new Fraction(-1, 2), fraction);
    }
    
    @Test
    public void parse_withImproperFractionString_shouldReturnParsedFraction() {
        Fraction fraction = formatter.parse("1 2/3");
        assertEquals(new Fraction(5, 3), fraction);
    }
    
    @Test
    public void parse_withInvalidFractionString_shouldReturnNull() {
        Fraction fraction = formatter.parse("abc");
        assertNull(fraction);
    }
    
    @Test
    public void parse_withDenominatorZero_shouldReturnNull() {
        Fraction fraction = formatter.parse("1 / 0");
        assertNull(fraction);
    }
    
}