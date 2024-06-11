import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuilder expected = new StringBuilder();
        StringBuilder result = new StringBuilder();
        
        Fraction fraction = new Fraction(3, 2);
        FieldPosition fieldPosition = new FieldPosition(0);
        properFractionFormat.format(fraction, expected, fieldPosition);
        
        result.append("1 1/2");
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        
        String input = "1 1/2";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, parsePosition);
        
        Fraction expected = new Fraction(3, 2);
        
        assertEquals(expected, fraction);
    }
    
    @Test
    public void testParseWithInvalidFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        
        String input = "1 / 2";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, parsePosition);
        
        assertNull(fraction);
    }
    
    @Test
    public void testFormatWithNegativeInput() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuilder expected = new StringBuilder();
        StringBuilder result = new StringBuilder();
        
        Fraction fraction = new Fraction(-3, 2);
        FieldPosition fieldPosition = new FieldPosition(0);
        properFractionFormat.format(fraction, expected, fieldPosition);
        
        result.append("-1 1/2");
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testParseWithWrongInput() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        
        String input = "1 2 / 3";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, parsePosition);
        
        assertNull(fraction);
    }

    @Test
    public void testFormatWithZeroFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuilder expected = new StringBuilder();
        StringBuilder result = new StringBuilder();
        
        Fraction fraction = new Fraction(0, 3);
        FieldPosition fieldPosition = new FieldPosition(0);
        properFractionFormat.format(fraction, expected, fieldPosition);
        
        result.append("0");
        
        assertEquals(expected.toString(), result.toString());
    }
}