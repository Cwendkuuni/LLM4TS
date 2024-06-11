import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;
import java.text.NumberFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 5);
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        
        StringBuffer result = format.format(fraction, sb, pos);
        
        assertEquals("3 / 5", result.toString());
    }
    
    @Test
    public void testWholeFormatNotNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        assertNotNull(format.getWholeFormat());
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat newFormat = NumberFormat.getInstance();
        
        format.setWholeFormat(newFormat);
        
        assertEquals(newFormat, format.getWholeFormat());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        
        Fraction fraction = format.parse("1 2 / 3", pos);
        
        assertEquals(new Fraction(5, 3), fraction);
    }
    
    @Test
    public void testParseInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        
        Fraction fraction = format.parse("invalid input", pos);
        
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        format.setWholeFormat(null);
    }
    
}