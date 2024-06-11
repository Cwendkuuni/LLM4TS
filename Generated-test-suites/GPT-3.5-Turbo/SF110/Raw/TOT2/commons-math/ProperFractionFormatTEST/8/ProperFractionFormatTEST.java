import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testProperFractionFormatConstructor1() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        assertNotNull(properFractionFormat);
    }
    
    @Test
    public void testProperFractionFormatConstructor2() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(format);
        assertNotNull(properFractionFormat);
    }
    
    @Test
    public void testProperFractionFormatConstructor3() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance();
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(properFractionFormat);
    }
    
    @Test
    public void testFormatMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(2, 3);
        properFractionFormat.format(fraction, buffer, pos);
        assertEquals("2 / 3", buffer.toString());
    }
    
    @Test
    public void testParseMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("2 / 3", pos);
        assertNotNull(fraction);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }
    
    @Test
    public void testWholeFormatGetter() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        properFractionFormat.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
    }
    
    @Test
    public void testSetWholeFormatMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        properFractionFormat.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
    }
}