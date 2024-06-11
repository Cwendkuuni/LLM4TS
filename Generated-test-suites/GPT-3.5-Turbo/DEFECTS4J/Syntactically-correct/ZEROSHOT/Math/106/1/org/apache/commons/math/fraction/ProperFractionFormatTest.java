import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
    
    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
        assertNotNull(format.getWholeFormat());
        assertNotNull(format.getNumeratorFormat());
        assertNotNull(format.getDenominatorFormat());
    }
    
    @Test
    public void testCustomFormatConstructor() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat fractionFormat = new ProperFractionFormat(format);
        assertNotNull(fractionFormat);
        assertEquals(format, fractionFormat.getWholeFormat());
        assertEquals(format, fractionFormat.getNumeratorFormat());
        assertEquals(format, fractionFormat.getDenominatorFormat());
    }
    
    @Test
    public void testCustomWholeFormatConstructor() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        
        ProperFractionFormat fractionFormat = new ProperFractionFormat(wholeFormat,
                numeratorFormat, denominatorFormat);
        
        assertNotNull(fractionFormat);
        assertEquals(wholeFormat, fractionFormat.getWholeFormat());
        assertEquals(numeratorFormat, fractionFormat.getNumeratorFormat());
        assertEquals(denominatorFormat, fractionFormat.getDenominatorFormat());
    }
    
    @Test
    public void testFormatWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1/2";
        String actual = format.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithoutWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        String expected = "1/2";
        String actual = format.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseWithValidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction expected = new Fraction(3, 2);
        Fraction actual = format.parse("1 1/2");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseWithInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction actual = format.parse("-1 1/2");
        assertNull(actual);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}