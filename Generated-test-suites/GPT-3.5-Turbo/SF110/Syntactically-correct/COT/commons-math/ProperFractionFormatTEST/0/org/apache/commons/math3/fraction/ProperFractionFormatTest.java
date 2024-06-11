
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testConstructor1() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }
    
    @Test
    public void testConstructor2() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance());
        assertNotNull(format);
    }
    
    @Test
    public void testConstructor3() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance(), NumberFormat.getInstance(), NumberFormat.getInstance());
        assertNotNull(format);
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);

        StringBuffer result = format.format(fraction, buffer, pos);

        assertEquals("1 / 2", result.toString());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 / 2";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = format.parse(source, pos);

        assertNotNull(result);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
        assertEquals(source.length(), pos.getIndex());
    }
}