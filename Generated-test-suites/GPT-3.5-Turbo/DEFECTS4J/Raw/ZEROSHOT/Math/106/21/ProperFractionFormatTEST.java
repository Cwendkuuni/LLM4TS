import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        String result = properFractionFormat.format(fraction);
        assertEquals("3/4", result);
    }
    
    @Test
    public void testParse() {
        String source = "3/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(3, 4), result);
        assertEquals(3, pos.getIndex());
    }
    
    @Test
    public void testParseInvalidInput() {
        String source = "abc";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }
    
    @Test
    public void testParseWithWhitespace() {
        String source = " 3 / 4 ";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(3, 4), result);
        assertEquals(7, pos.getIndex());
    }
    
    @Test
    public void testParseNegativeFraction() {
        String source = "-3/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(-3, 4), result);
        assertEquals(4, pos.getIndex());
    }
    
    @Test
    public void testParseImproperFraction() {
        String source = "1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }
    
    @Test
    public void testParseInvalidFractionWithWhitespace() {
        String source = " -3 / 4 ";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }
    
    @Test
    public void testParseInvalidFractionWithExtraWhitespace() {
        String source = " -3     / 4 ";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }
    
    @Test
    public void testParseWithInvalidDenominator() {
        String source = "2/a";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }
    
    @Test
    public void testSetWholeFormat() {
        properFractionFormat.setWholeFormat(NumberFormat.getIntegerInstance());
        NumberFormat format = properFractionFormat.getWholeFormat();
        assertTrue(format instanceof NumberFormat);
    }
}