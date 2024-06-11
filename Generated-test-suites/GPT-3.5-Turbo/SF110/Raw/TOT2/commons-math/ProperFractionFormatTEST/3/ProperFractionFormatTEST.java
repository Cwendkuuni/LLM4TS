import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;
import java.text.NumberFormat;
import java.text.FieldPosition;

public class ProperFractionFormatTest {
    
    @Test
    public void testConstructor1() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }
    
    @Test
    public void testConstructor2() {
        NumberFormat nf = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(nf);
        assertNotNull(format);
    }
    
    @Test
    public void testConstructor3() {
        NumberFormat whole = NumberFormat.getIntegerInstance();
        NumberFormat num = NumberFormat.getInstance();
        NumberFormat den = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(whole, num, den);
        assertNotNull(format);
    }
    
    @Test
    public void testGetWholeFormat() {
        NumberFormat nf = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(nf);
        assertEquals(nf, format.getWholeFormat());
    }
    
    @Test
    public void testSetWholeFormat() {
        NumberFormat nf = NumberFormat.getIntegerInstance();
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(nf);
        assertEquals(nf, format.getWholeFormat());
    }
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 2);
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        
        assertEquals("2 1 / 2", format.format(fraction, sb, pos).toString());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 1 / 2";
        Fraction parsedFraction = format.parse(source, new ParsePosition(0));
        Fraction expectedFraction = new Fraction(5, 2);
        
        assertEquals(expectedFraction, parsedFraction);
    }
}