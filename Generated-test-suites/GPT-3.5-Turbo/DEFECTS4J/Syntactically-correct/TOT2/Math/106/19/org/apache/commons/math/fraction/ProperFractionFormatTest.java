import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        format.format(fraction, buffer, position);
        assertEquals("1 1/2", buffer.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "1 1/2";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(fractionString, position);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFormatEdgeCase() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        format.format(fraction, buffer, position);
        assertEquals("-1 1/2", buffer.toString());
    }

    @Test
    public void testParseEdgeCase() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "-1 1/2";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(fractionString, position);
        assertEquals(-3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testParseInvalidString() {
        ProperFractionFormat format = new ProperFractionFormat();
        String invalidFractionString = "3 -1/2";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(invalidFractionString, position);
        assertEquals(null, fraction);
    }
}