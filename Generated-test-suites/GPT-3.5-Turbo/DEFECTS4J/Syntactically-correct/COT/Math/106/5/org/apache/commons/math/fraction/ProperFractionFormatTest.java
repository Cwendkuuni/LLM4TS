
import static org.junit.Assert.*;
import org.junit.Test;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {
    
    ProperFractionFormat format = new ProperFractionFormat();
    
    // Test format method
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer stringBuffer = new StringBuffer();
        format.format(fraction, stringBuffer, new FieldPosition(0));
        assertEquals("3 / 4", stringBuffer.toString());
        
        fraction = new Fraction(7, 4);
        stringBuffer = new StringBuffer();
        format.format(fraction, stringBuffer, new FieldPosition(0));
        assertEquals("1 3 / 4", stringBuffer.toString());
    }
    
    // Test getWholeFormat method
    @Test
    public void testGetWholeFormat() {
        NumberFormat numberFormat = format.getWholeFormat();
        assertNotNull(numberFormat);
    }
    
    // Test getNumeratorFormat method
    @Test
    public void testGetNumeratorFormat() {
        NumberFormat numberFormat = format.getNumeratorFormat();
        assertNotNull(numberFormat);
    }
    
    // Test getDenominatorFormat method
    @Test
    public void testGetDenominatorFormat() {
        NumberFormat numberFormat = format.getDenominatorFormat();
        assertNotNull(numberFormat);
    }
    
    // Test parse method
    @Test
    public void testParse() {
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = format.parse("3 / 4", parsePosition);
        assertEquals(new Fraction(3, 4), fraction);
        
        parsePosition = new ParsePosition(0);
        fraction = format.parse("1 3 / 4", parsePosition);
        assertEquals(new Fraction(7, 4), fraction);
    }
    
    // Test setWholeFormat method
    @Test
    public void testSetWholeFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        format.setWholeFormat(numberFormat);
        assertEquals(numberFormat, format.getWholeFormat());
    }
}
