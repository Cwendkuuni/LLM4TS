

import static org.junit.Assert.*;
import org.junit.Test;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.util.MathUtils;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFractionFormat;
    
    public ProperFractionFormatTest() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        FieldPosition pos = new FieldPosition(0);
        
        // Test case 1: Fraction with 1 as whole number
        Fraction fraction1 = new Fraction(1, 2);
        StringBuffer expected1 = new StringBuffer("1 / 2");
        StringBuffer actual1 = new StringBuffer();
        properFractionFormat.format(fraction1, actual1, pos);
        assertEquals(expected1.toString(), actual1.toString());
        
        // Test case 2: Fraction with 0 as whole number
        Fraction fraction2 = new Fraction(2, 3);
        StringBuffer expected2 = new StringBuffer("2 / 3");
        StringBuffer actual2 = new StringBuffer();
        properFractionFormat.format(fraction2, actual2, pos);
        assertEquals(expected2.toString(), actual2.toString());
        
        // Test case 3: Fraction with -1 as whole number
        Fraction fraction3 = new Fraction(-3, 4);
        StringBuffer expected3 = new StringBuffer("-3 / 4");
        StringBuffer actual3 = new StringBuffer();
        properFractionFormat.format(fraction3, actual3, pos);
        assertEquals(expected3.toString(), actual3.toString());
    }
    
    @Test
    public void testParse() {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        
        // Test case 1: Parse valid fraction with positive whole number
        Fraction expected1 = new Fraction(3, 4);
        Fraction actual1 = properFractionFormat.parse(source, pos);
        assertEquals(expected1.getNumerator(), actual1.getNumerator());
        assertEquals(expected1.getDenominator(), actual1.getDenominator());
        
        // Test case 2: Parse valid fraction with negative whole number
        source = "-2 / 5";
        pos = new ParsePosition(0);
        Fraction expected2 = new Fraction(-2, 5);
        Fraction actual2 = properFractionFormat.parse(source, pos);
        assertEquals(expected2.getNumerator(), actual2.getNumerator());
        assertEquals(expected2.getDenominator(), actual2.getDenominator());
        
        // Test case 3: Parse invalid fraction with negative fractional part
        source = "1 / -2";
        pos = new ParsePosition(0);
        Fraction actual3 = properFractionFormat.parse(source, pos);
        assertNull(actual3);
        
        // Test case 4: Parse invalid fraction with incorrect format
        source = "1:2";
        pos = new ParsePosition(0);
        Fraction actual4 = properFractionFormat.parse(source, pos);
        assertNull(actual4);
    }
}
