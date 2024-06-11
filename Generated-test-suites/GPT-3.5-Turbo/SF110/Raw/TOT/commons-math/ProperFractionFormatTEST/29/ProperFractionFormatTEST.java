import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.math.RoundingMode;

public class ProperFractionFormatTest {
    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    // Test format() method
    
    @Test
    public void testFormat_WholeOnly() {
        Fraction fraction = new Fraction(5, 1);
        StringBuffer expectedFormattedValue = new StringBuffer("5");
        FieldPosition pos = new FieldPosition(0);
        assertEquals(expectedFormattedValue.toString(), properFractionFormat.format(fraction, new StringBuffer(), pos).toString());
    }
    
    @Test
    public void testFormat_WholeAndNumerator() {
        Fraction fraction = new Fraction(10, 2);
        StringBuffer expectedFormattedValue = new StringBuffer("5 0/2");
        FieldPosition pos = new FieldPosition(0);
        assertEquals(expectedFormattedValue.toString(), properFractionFormat.format(fraction, new StringBuffer(), pos).toString());
    }
    
    @Test
    public void testFormat_OnlyNumerator() {
        Fraction fraction = new Fraction(0, 2);
        StringBuffer expectedFormattedValue = new StringBuffer("0/2");
        FieldPosition pos = new FieldPosition(0);
        assertEquals(expectedFormattedValue.toString(), properFractionFormat.format(fraction, new StringBuffer(), pos).toString());
    }
    
    @Test
    public void testFormat_OnlyDenominator() {
        Fraction fraction = new Fraction(5, 0);
        StringBuffer expectedFormattedValue = new StringBuffer("5/0");
        FieldPosition pos = new FieldPosition(0);
        assertEquals(expectedFormattedValue.toString(), properFractionFormat.format(fraction, new StringBuffer(), pos).toString());
    }
    
    // Test getWholeFormat() method
    
    @Test
    public void testGetWholeFormat_DefaultFormat() {
        NumberFormat expectedFormat = NumberFormat.getInstance();
        assertEquals(expectedFormat, properFractionFormat.getWholeFormat());
    }
    
    // Test parse() method
    
    @Test
    public void testParse_WholeOnly() {
        String source = "5";
        ParsePosition pos = new ParsePosition(0);
        Fraction expectedFraction = new Fraction(5, 1);
        assertEquals(expectedFraction, properFractionFormat.parse(source, pos));
    }
    
    @Test
    public void testParse_WholeAndNumerator() {
        String source = "5 7/8";
        ParsePosition pos = new ParsePosition(0);
        Fraction expectedFraction = new Fraction(47, 8);
        assertEquals(expectedFraction, properFractionFormat.parse(source, pos));
    }
    
    @Test
    public void testParse_InvalidFormat() {
        String source = "5 7/8 ";
        ParsePosition pos = new ParsePosition(0);
        assertNull(properFractionFormat.parse(source, pos));
    }
    
    @Test
    public void testParse_NullValue() {
        String source = null;
        ParsePosition pos = new ParsePosition(0);
        assertNull(properFractionFormat.parse(source, pos));
    }
    
    // Test setWholeFormat() method
    
    @Test
    public void testSetWholeFormat_NullFormat() {
        try {
            properFractionFormat.setWholeFormat(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertEquals(LocalizedFormats.WHOLE_FORMAT, e.getSpecifier());
        }
    }
}