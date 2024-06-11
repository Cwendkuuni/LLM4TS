
import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.util.MathUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(7, 3); // Proper fraction: 2 1/3
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        StringBuffer expectedResult = new StringBuffer("2 1/3");
        
        properFractionFormat.format(fraction, stringBuffer, fieldPosition);
        
        Assert.assertEquals(expectedResult.toString(), stringBuffer.toString());
    }
    
    @Test
    public void testParse() {
        String source = "2 3/4"; // Proper fraction: 11/4
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction expectedResult = new Fraction(11, 4);
        
        Fraction result = properFractionFormat.parse(source, parsePosition);
        
        Assert.assertEquals(expectedResult, result);
        Assert.assertEquals(source.length(), parsePosition.getIndex());
    }

    @Test
    public void testFormat_EmptyFraction() {
        Fraction fraction = new Fraction(0, 1); // Proper fraction: 0
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        StringBuffer expectedResult = new StringBuffer("0");
        
        properFractionFormat.format(fraction, stringBuffer, fieldPosition);
        
        Assert.assertEquals(expectedResult.toString(), stringBuffer.toString());
    }
    
    @Test
    public void testParse_EmptyFraction() {
        String source = "0"; // Proper fraction: 0
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction expectedResult = new Fraction(0, 1);
        
        Fraction result = properFractionFormat.parse(source, parsePosition);
        
        Assert.assertEquals(expectedResult, result);
        Assert.assertEquals(source.length(), parsePosition.getIndex());
    }

    @Test
    public void testFormat_NegativeFraction() {
        Fraction fraction = new Fraction(-7, 3); // Proper fraction: -2 1/3
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        StringBuffer expectedResult = new StringBuffer("-2 1/3");
        
        properFractionFormat.format(fraction, stringBuffer, fieldPosition);
        
        Assert.assertEquals(expectedResult.toString(), stringBuffer.toString());
    }
    
    @Test
    public void testParse_NegativeFraction() {
        String source = "-2 3/4"; // Proper fraction: -11/4
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction expectedResult = new Fraction(-11, 4);
        
        Fraction result = properFractionFormat.parse(source, parsePosition);
        
        Assert.assertEquals(expectedResult, result);
        Assert.assertEquals(source.length(), parsePosition.getIndex());
    }

    @Test
    public void testFormat_ImproperFraction() {
        Fraction fraction = new Fraction(5, 2); // Proper fraction: 2 1/2
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        StringBuffer expectedResult = new StringBuffer("2 1/2");
        
        properFractionFormat.format(fraction, stringBuffer, fieldPosition);
        
        Assert.assertEquals(expectedResult.toString(), stringBuffer.toString());
    }
    
    @Test
    public void testParse_ImproperFraction() {
        String source = "2 5/2"; // Proper fraction: 9/2
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction expectedResult = new Fraction(9, 2);
        
        Fraction result = properFractionFormat.parse(source, parsePosition);
        
        Assert.assertEquals(expectedResult, result);
        Assert.assertEquals(source.length(), parsePosition.getIndex());
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}