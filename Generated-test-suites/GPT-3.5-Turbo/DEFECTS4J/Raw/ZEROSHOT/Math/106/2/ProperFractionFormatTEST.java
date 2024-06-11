import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() {
        this.properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormatWithPositiveFraction() {
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithNegativeFraction() {
        Fraction fraction = new Fraction(-3, 4);
        String expected = "-3 / 4";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithWholeNumber() {
        Fraction fraction = new Fraction(5, 2);
        String expected = "2 1/2";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithNegativeWholeNumber() {
        Fraction fraction = new Fraction(-5, 2);
        String expected = "-2 1/2";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithZeroFraction() {
        Fraction fraction = new Fraction(0, 4);
        String expected = "0 / 4";
        String actual = properFractionFormat.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseWithValidInput() {
        String source = "2 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(5, 2);
        Fraction actual = properFractionFormat.parse(source, pos);
        assertEquals(expected, actual);
        assertEquals(7, pos.getIndex());
    }
    
    @Test
    public void testParseWithValidInputAndLeadingWhitespace() {
        String source = "   2 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(5, 2);
        Fraction actual = properFractionFormat.parse(source, pos);
        assertEquals(expected, actual);
        assertEquals(10, pos.getIndex());
    }
    
    @Test
    public void testParseWithInvalidFraction() {
        String source = "-3 -1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = properFractionFormat.parse(source, pos);
        assertNull(actual);
        assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testParseWithInvalidWholeNumber() {
        String source = "-3 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = properFractionFormat.parse(source, pos);
        assertNull(actual);
        assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testParseWithInvalidFractionFormat() {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = properFractionFormat.parse(source, pos);
        assertNull(actual);
        assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testParseWithInvalidFractionSeparator() {
        String source = "2 1 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = properFractionFormat.parse(source, pos);
        assertNull(actual);
        assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testParseWithInvalidNumberFormat() {
        String source = "2 a/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = properFractionFormat.parse(source, pos);
        assertNull(actual);
        assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testParseWithInvalidNumber() {
        String source = "abc";
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = properFractionFormat.parse(source, pos);
        assertNull(actual);
        assertEquals(0, pos.getIndex());
    }
    
    @Test
    public void testGetterAndSetterForWholeFormat() {
        NumberFormat expected = NumberFormat.getCurrencyInstance();
        this.properFractionFormat.setWholeFormat(expected);
        NumberFormat actual = this.properFractionFormat.getWholeFormat();
        assertEquals(expected, actual);
    }
}