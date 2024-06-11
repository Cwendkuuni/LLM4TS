import static org.junit.Assert.*;
import org.junit.Test;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test 1
        Fraction fraction1 = new Fraction(1, 2);
        StringBuffer buffer1 = new StringBuffer();
        format.format(fraction1, buffer1, null);
        assertEquals("1 / 2", buffer1.toString());

        // Test 2
        Fraction fraction2 = new Fraction(-3, 2);
        StringBuffer buffer2 = new StringBuffer();
        format.format(fraction2, buffer2, null);
        assertEquals("-1 1 / 2", buffer2.toString());

        // Test 3
        Fraction fraction3 = new Fraction(5, 3);
        StringBuffer buffer3 = new StringBuffer();
        format.format(fraction3, buffer3, null);
        assertEquals("1 2 / 3", buffer3.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test 1
        String source1 = "1 / 2";
        ParsePosition position1 = new ParsePosition(0);
        Fraction fraction1 = format.parse(source1, position1);
        assertEquals(1, fraction1.getNumerator());
        assertEquals(2, fraction1.getDenominator());

        // Test 2
        String source2 = "-1 1 / 2";
        ParsePosition position2 = new ParsePosition(0);
        Fraction fraction2 = format.parse(source2, position2);
        assertEquals(-3, fraction2.getNumerator());
        assertEquals(2, fraction2.getDenominator());

        // Test 3
        String source3 = "1 2 / 3";
        ParsePosition position3 = new ParsePosition(0);
        Fraction fraction3 = format.parse(source3, position3);
        assertEquals(5, fraction3.getNumerator());
        assertEquals(3, fraction3.getDenominator());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormat_Null() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}