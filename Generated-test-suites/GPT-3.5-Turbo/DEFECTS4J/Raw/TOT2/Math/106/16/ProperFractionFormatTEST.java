import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1/2";
        assertEquals(expected, format.format(fraction));
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction expected = new Fraction(5, 3);
        Fraction result = format.parse("1 2/3");
        assertEquals(expected, result);
    }

    @Test
    public void testParseImproperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction expected = new Fraction(-7, 2);
        Fraction result = format.parse("-3 1/2");
        assertEquals(expected, result);
    }
    
    @Test
    public void testParseInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("-3 -1/2");
        assertNull(result);
    }
    
    @Test
    public void testInvalidWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat customWholeFormat = new DecimalFormat("#");
        format.setWholeFormat(customWholeFormat);
        Fraction fraction = new Fraction(5, 2);
        String expected = "2 1/2";
        assertEquals(expected, format.format(fraction));
    }
    
    @Test
    public void testEmptyStringParsing() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("");
        assertNull(result);
    }
}