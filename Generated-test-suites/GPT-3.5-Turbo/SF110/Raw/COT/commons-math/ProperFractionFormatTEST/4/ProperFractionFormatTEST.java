
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction fraction = new Fraction(5, 2);
        String expected = "2 1 / 2";
        String actual = format.format(fraction);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        String source = "2 1 / 2";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction expected = new Fraction(5, 2);
        Fraction actual = format.parse(source, pos);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        NumberFormat wholeFormat = new DecimalFormat("#");
        format.setWholeFormat(wholeFormat);
        
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}
