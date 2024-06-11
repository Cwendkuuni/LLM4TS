import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.text.*;
import org.apache.commons.math3.fraction.*;
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
    public void testFormat_WithWholeNumber() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer expectedResult = new StringBuffer();
        expectedResult.append("2 / 3");
        
        StringBuffer actualResult = properFractionFormat.format(fraction, new StringBuffer(), null);
        
        assertEquals(expectedResult.toString(), actualResult.toString());
    }
    
    @Test
    public void testFormat_WithZeroWholeNumber() {
        Fraction fraction = new Fraction(0, 3);
        StringBuffer expectedResult = new StringBuffer();
        expectedResult.append("0 / 3");
        
        StringBuffer actualResult = properFractionFormat.format(fraction, new StringBuffer(), null);
        
        assertEquals(expectedResult.toString(), actualResult.toString());
    }
    
    @Test
    public void testFormat_WithNegativeNumAndDen() {
        Fraction fraction = new Fraction(-2, -3);
        StringBuffer expectedResult = new StringBuffer();
        expectedResult.append("-2 / -3");
        
        StringBuffer actualResult = properFractionFormat.format(fraction, new StringBuffer(), null);
        
        assertEquals(expectedResult.toString(), actualResult.toString());
    }
    
    @Test
    public void testFormat_WithWholeNumberAndSpace() {
        Fraction fraction = new Fraction(5, 3);
        StringBuffer expectedResult = new StringBuffer();
        expectedResult.append("1 2 / 3");
        
        StringBuffer actualResult = properFractionFormat.format(fraction, new StringBuffer(), null);
        
        assertEquals(expectedResult.toString(), actualResult.toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
        
        assertNotNull(wholeFormat);
    }
    
    @Test
    public void testParse_WithValidInput() {
        String source = "2 / 3";
        ParsePosition pos = new ParsePosition(0);
        Fraction expectedFraction = new Fraction(2, 3);
        
        Fraction actualFraction = properFractionFormat.parse(source, pos);
        
        assertEquals(expectedFraction.toString(), actualFraction.toString());
    }
    
    @Test
    public void testParse_WithInvalidInput() {
        String source = "2 /";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction actualFraction = properFractionFormat.parse(source, pos);
        
        assertNull(actualFraction);
    }
    
    @Test
    public void testSetWholeFormat_WithValidFormat() {
        NumberFormat format = new DecimalFormat();
        
        properFractionFormat.setWholeFormat(format);
        
        assertEquals(format, properFractionFormat.getWholeFormat());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat_WithNullFormat() {
        properFractionFormat.setWholeFormat(null);
    }
}