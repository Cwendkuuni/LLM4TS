import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 4);
        StringBuffer result = new StringBuffer();
        properFormat.format(fraction, result, null);
        assertEquals("1 1/4", result.toString());
    }

    @Test
    public void testFormatWithNegativeFraction() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(-5, 4);
        StringBuffer result = new StringBuffer();
        properFormat.format(fraction, result, null);
        assertEquals("-1 1/4", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFormat.parse("1 1/4", pos);
        assertEquals(new Fraction(5, 4), fraction);
    }

    @Test
    public void testParseWithInvalidInput() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFormat.parse("-1 -1/4", pos);
        assertNull(fraction);
    }
}