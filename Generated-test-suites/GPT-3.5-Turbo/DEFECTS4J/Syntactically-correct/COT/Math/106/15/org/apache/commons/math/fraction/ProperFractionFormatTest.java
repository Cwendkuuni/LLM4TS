
import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(3, 2);
        
        format.format(fraction, buffer, pos);
        
        assertEquals("1 1/2", buffer.toString());
        assertEquals(0, pos.getBeginIndex());
        assertEquals(0, pos.getEndIndex());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 1/2";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction fraction = format.parse(source, pos);
        
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }
}
