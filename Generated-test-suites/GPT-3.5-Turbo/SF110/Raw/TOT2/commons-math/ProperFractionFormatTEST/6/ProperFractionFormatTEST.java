import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);

        format.format(fraction, result, pos);

        assertEquals("1 1 / 2", result.toString());
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
        String input = "1 1 / 2";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = format.parse(input, pos);

        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat newFormat = NumberFormat.getIntegerInstance();

        format.setWholeFormat(newFormat);

        assertEquals(newFormat, format.getWholeFormat());
    }

    // Additional test cases for edge cases and exceptions can be added here

}