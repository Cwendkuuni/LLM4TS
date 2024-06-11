import static org.junit.Assert.*;
import org.junit.Test;
import java.text.*;
import org.apache.commons.math3.fraction.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat_WithWholeNumber_ReturnsFormattedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 3);
        StringBuffer expectedResult = new StringBuffer();
        expectedResult.append("1 2 / 3");
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = format.format(fraction, new StringBuffer(), pos);
        assertEquals(expectedResult.toString(), result.toString());
    }
    
    @Test
    public void testFormat_WithNegativeFraction_ReturnsFormattedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-4, 5);
        StringBuffer expectedResult = new StringBuffer();
        expectedResult.append("-4 / 5");
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = format.format(fraction, new StringBuffer(), pos);
        assertEquals(expectedResult.toString(), result.toString());
    }
    
    @Test
    public void testFormat_WithZeroWholeNumber_ReturnsFormattedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 7);
        StringBuffer expectedResult = new StringBuffer();
        expectedResult.append("0 / 7");
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = format.format(fraction, new StringBuffer(), pos);
        assertEquals(expectedResult.toString(), result.toString());
    }
    
    @Test
    public void testFormat_WithZeroFraction_ReturnsFormattedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 1);
        StringBuffer expectedResult = new StringBuffer();
        expectedResult.append("0 / 1");
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = format.format(fraction, new StringBuffer(), pos);
        assertEquals(expectedResult.toString(), result.toString());
    }
    
    @Test
    public void testParse_WithValidInput_ReturnsParsedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "2 3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        assertEquals(11, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }
    
    @Test
    public void testParse_WithNegativeWholeNumber_ReturnsParsedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-5 / 6";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        assertEquals(-5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }
    
    @Test
    public void testParse_WithZeroFraction_ReturnsParsedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "0 / 9";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }
    
    @Test
    public void testParse_WithInvalidInput_ReturnsNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "4 5";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        assertNull(result);
    }

}