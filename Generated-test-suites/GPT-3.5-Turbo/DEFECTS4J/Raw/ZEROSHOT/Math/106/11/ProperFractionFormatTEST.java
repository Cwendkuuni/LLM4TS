import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.Before;
import org.junit.Test;
import java.text.NumberFormat;
import java.text.ParsePosition;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        properFractionFormat = new ProperFractionFormat(numberFormat);
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 2);
        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), null);
        assertEquals("1 1/2", result.toString());

        fraction = new Fraction(-5, 2);
        result = properFractionFormat.format(fraction, new StringBuffer(), null);
        assertEquals("-2 1/2", result.toString());

        fraction = new Fraction(-3, 4);
        result = properFractionFormat.format(fraction, new StringBuffer(), null);
        assertEquals("-3/4", result.toString());
    }

    @Test
    public void testParse() {
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse("1 1/2", pos);
        assertEquals(new Fraction(3, 2), result);
        assertEquals(6, pos.getIndex());

        pos = new ParsePosition(0);
        result = properFractionFormat.parse("11/2", pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());

        pos = new ParsePosition(0);
        result = properFractionFormat.parse("-3 1/2", pos);
        assertEquals(new Fraction(-7, 2), result);
        assertEquals(6, pos.getIndex());

        pos = new ParsePosition(0);
        result = properFractionFormat.parse("-3 -1/2", pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(6, pos.getErrorIndex());

        pos = new ParsePosition(0);
        result = properFractionFormat.parse("5", pos);
        assertEquals(new Fraction(5), result);
        assertEquals(1, pos.getIndex());
    }
}