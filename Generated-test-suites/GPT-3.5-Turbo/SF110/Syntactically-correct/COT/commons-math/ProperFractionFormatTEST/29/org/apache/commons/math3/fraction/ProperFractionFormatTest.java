import org.apache.commons.math3.fraction.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.junit.Test;
import java.text.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat_WholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 1);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("5", result.toString());
    }
    
    @Test
    public void testFormat_ZeroNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 5);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("0 / 5", result.toString());
    }
    
    @Test
    public void testFormat_ZeroDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 0);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("5 / 0", result.toString());
    }
    
    @Test
    public void testFormat_NegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("-3 / 4", result.toString());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructor_NullFormat() {
        ProperFractionFormat format = new ProperFractionFormat(null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat_NullFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testParse_NullSource() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.parse(null, new ParsePosition(0));
    }
    
    @Test(expected = NullPointerException.class)
    public void testParse_NullParsePosition() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.parse("5 / 4", null);
    }
    
    // Additional test cases for other methods can be added here

}



// Generated test cases go here

