import static org.junit.Assert.*;
import java.text.*;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.*;

public class ProperFractionFormatTest {
    private ProperFractionFormat format;
    
    @Before
    public void setup() {
        format = new ProperFractionFormat();
    }
    
    @Test
    public void format_WholeNumberOnly_ReturnsFormattedFraction() {
        Fraction fraction = new Fraction(5);
        StringBuffer expected = new StringBuffer("5");
        
        StringBuffer result = format.format(fraction, new StringBuffer(), new FieldPosition(0));
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void format_FractionOnly_ReturnsFormattedFraction() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer expected = new StringBuffer("2 / 3");
        
        StringBuffer result = format.format(fraction, new StringBuffer(), new FieldPosition(0));
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void format_WholeNumberAndFraction_ReturnsFormattedFraction() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer expected = new StringBuffer("1 2 / 3");
        
        StringBuffer result = format.format(fraction, new StringBuffer(), new FieldPosition(0));
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void format_ZeroNumerator_ReturnsFormattedFractionWithWholeNumber() {
        Fraction fraction = new Fraction(0, 5);
        StringBuffer expected = new StringBuffer("0");
        
        StringBuffer result = format.format(fraction, new StringBuffer(), new FieldPosition(0));
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void format_NegativeWholeNumber_ReturnsFormattedFraction() {
        Fraction fraction = new Fraction(-3, 2);
        StringBuffer expected = new StringBuffer("-1 1 / 2");
        
        StringBuffer result = format.format(fraction, new StringBuffer(), new FieldPosition(0));
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void getWholeFormat_ReturnsWholeFormat() {
        NumberFormat wholeFormat = format.getWholeFormat();
        
        assertNotNull(wholeFormat);
    }
    
    @Test
    public void setWholeFormat_ValidFormat_SetsWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getCurrencyInstance();
        
        format.setWholeFormat(wholeFormat);
        
        assertEquals(wholeFormat, format.getWholeFormat());
    }
    
    @Test(expected = NullArgumentException.class)
    public void setWholeFormat_NullFormat_ThrowsException() {
        format.setWholeFormat(null);
    }
    
    @Test
    public void parse_ValidFraction_ReturnsParsedFraction() {
        String source = "2 / 3";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(2, 3);
        
        Fraction result = format.parse(source, pos);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void parse_ValidWholeNumber_ReturnsParsedFraction() {
        String source = "5";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(5, 1);
        
        Fraction result = format.parse(source, pos);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void parse_InvalidFormat_ReturnsNull() {
        String source = "2 / a";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = format.parse(source, pos);
        
        assertNull(result);
    }
    
    @Test
    public void parse_NegativeNumerator_ReturnsNull() {
        String source = "-2 / 3";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = format.parse(source, pos);
        
        assertNull(result);
    }
}