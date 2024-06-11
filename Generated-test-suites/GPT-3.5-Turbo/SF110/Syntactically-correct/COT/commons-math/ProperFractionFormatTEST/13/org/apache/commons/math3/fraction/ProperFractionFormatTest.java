import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
     
    // Test format() method
     
    @Test
    public void testFormat_basicFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        format.format(fraction, buffer, pos);
        assertEquals("3 / 4", buffer.toString());
    }
     
    // Add more test cases for format() method if necessary
     
    // Test getWholeFormat() method
     
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format.getWholeFormat());
    }
     
    // Add more test cases for getWholeFormat() method if necessary
     
    // Test parse() method
     
    @Test
    public void testParse_basicFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }
     
    // Add more test cases for parse() method if necessary
     
    // Test setWholeFormat() method
     
    @Test
    public void testSetWholeFormat_validFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
     
    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat_nullFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
     
    // Add more test cases for setWholeFormat() method if necessary
     
}