import junit.framework.TestCase;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest extends TestCase {
    
    private ProperFractionFormat properFractionFormat;
    
    protected void setUp() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numFormat = NumberFormat.getInstance();
        NumberFormat denFormat = NumberFormat.getInstance();
        properFractionFormat = new ProperFractionFormat(wholeFormat, numFormat, denFormat);
    }
    
    public void testFormatWithPositiveFraction() {
        Fraction fraction = new Fraction(1, 2);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("1/2", buffer.toString());
    }
    
    public void testFormatWithZeroFraction() {
        Fraction fraction = new Fraction(0, 3);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("0/3", buffer.toString());
    }
    
    public void testFormatWithNegativeFraction() {
        Fraction fraction = new Fraction(-2, 3);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("-2/3", buffer.toString());
    }
    
    public void testFormatWithPositiveWholeNumber() {
        Fraction fraction = new Fraction(2, 1);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("2", buffer.toString());
    }
    
    public void testFormatWithNegativeWholeNumber() {
        Fraction fraction = new Fraction(-4, 1);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("-4", buffer.toString());
    }
    
    public void testFormatWithPositiveWholeNumberAndFraction() {
        Fraction fraction = new Fraction(5, 2);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("2 1/2", buffer.toString());
    }
    
    public void testFormatWithNegativeWholeNumberAndFraction() {
        Fraction fraction = new Fraction(-7, 4);
        StringBuffer buffer = new StringBuffer();
        properFractionFormat.format(fraction, buffer, null);
        assertEquals("-1 3/4", buffer.toString());
    }
    
    public void testParseWithValidFractionString() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("3/4", pos);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
        assertEquals(3, pos.getIndex());
    }
    
    public void testParseWithEmptyString() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("", pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(-1, pos.getErrorIndex());
    }
    
    public void testParseWithInvalidWholeNumber() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("x 1/2", pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }
    
    public void testParseWithInvalidDenominator() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("3/0", pos);
        assertNull(fraction);
        assertEquals(2, pos.getIndex());
        assertEquals(2, pos.getErrorIndex());
    }
    
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
        assertNotNull(wholeFormat);
    }
    
    public void testSetWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        properFractionFormat.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
    }
    
    protected void tearDown() {
        properFractionFormat = null;
    }
}