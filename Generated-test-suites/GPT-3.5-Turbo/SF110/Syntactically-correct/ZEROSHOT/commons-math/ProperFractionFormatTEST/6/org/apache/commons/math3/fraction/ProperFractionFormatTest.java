import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.fraction.*;
import java.text.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

public class ProperFractionFormatTest {
    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), new FieldPosition(0));
        assertEquals("3 / 4", result.toString());
        
        fraction = new Fraction(5, 2);
        result = properFractionFormat.format(fraction, new StringBuffer(), new FieldPosition(0));
        assertEquals("2 1 / 2", result.toString());
        
        fraction = new Fraction(7, 7);
        result = properFractionFormat.format(fraction, new StringBuffer(), new FieldPosition(0));
        assertEquals("1", result.toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
        assertNotNull(wholeFormat);
    }
    
    @Test
    public void testParse() {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertNotNull(result);
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
        
        source = "2 1 / 2";
        pos = new ParsePosition(0);
        result = properFractionFormat.parse(source, pos);
        assertNotNull(result);
        assertEquals(5, result.getNumerator());
        assertEquals(2, result.getDenominator());
        
        source = "1";
        pos = new ParsePosition(0);
        result = properFractionFormat.parse(source, pos);
        assertNotNull(result);
        assertEquals(1, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }
    
    @Test
    public void testSetWholeFormat() {
        NumberFormat format = new DecimalFormat("#");
        properFractionFormat.setWholeFormat(format);
        NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
        assertEquals(format, wholeFormat);
    }

    // Add more test cases for other methods if needed

}

