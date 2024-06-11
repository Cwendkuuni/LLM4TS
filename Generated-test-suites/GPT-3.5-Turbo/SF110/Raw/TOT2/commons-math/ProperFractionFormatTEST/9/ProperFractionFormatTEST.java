import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testProperFractionFormatConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        assertNotNull(properFractionFormat);
    }
    
    @Test
    public void testFormatMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(3, 4);
        StringBuffer result = properFractionFormat.format(fraction, sb, pos);
        assertEquals("3 / 4", result.toString());
    }
    
    @Test
    public void testParseMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("1 2 / 3", pos);
        assertEquals(new Fraction(5, 3), fraction);
    }
    
    @Test
    public void testSetWholeFormatMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat numberFormat = NumberFormat.getInstance();
        properFractionFormat.setWholeFormat(numberFormat);
        assertEquals(numberFormat, properFractionFormat.getWholeFormat());
    }
}