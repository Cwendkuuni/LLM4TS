import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    // Test cases for format method

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("3 / 4", sb.toString());
    }

    // Test cases for parse method

    @Test
    public void testParse_ValidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 4/5";
        Fraction fraction = format.parse(input, null);
        assertEquals(19, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test
    public void testParse_ImproperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3/4";
        Fraction fraction = format.parse(input, null);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testParse_InvalidInput_NoWhiteSpace() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "34/5";
        Fraction fraction = format.parse(input, null);
        assertNull(fraction);
    }

    @Test
    public void testParse_InvalidInput_WhiteSpaceMissing() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 4/5";
        Fraction fraction = format.parse(input, null);
        assertNull(fraction);
    }

    // Test cases for getWholeFormat method

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format.getWholeFormat());
    }

    // Test cases for setWholeFormat method

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(NumberFormat.getInstance());
        assertNotNull(format.getWholeFormat());
    }

}