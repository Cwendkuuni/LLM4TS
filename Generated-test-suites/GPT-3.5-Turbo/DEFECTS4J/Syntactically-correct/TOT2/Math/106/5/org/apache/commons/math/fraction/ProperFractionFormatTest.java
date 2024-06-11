import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, 3);
        StringBuffer result = new StringBuffer();
        formatter.format(fraction, result, null);
        assertEquals("2 / 3", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = formatter.parse("5 1/2", pos);
        assertEquals(11, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        ProperFractionFormat formatter = new ProperFractionFormat();
        formatter.setWholeFormat(format);
        assertSame(format, formatter.getWholeFormat());
    }

    @Test
    public void testEdgeCases() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        // Test edge case of 0/1
        Fraction zeroFraction = new Fraction(0, 1);
        StringBuffer zeroResult = new StringBuffer();
        formatter.format(zeroFraction, zeroResult, null);
        assertEquals("0 / 1", zeroResult.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWholeFormat() {
        ProperFractionFormat formatter = new ProperFractionFormat();
        formatter.setWholeFormat(null);
    }

    // Add more test cases for edge cases and exception handling scenarios

}