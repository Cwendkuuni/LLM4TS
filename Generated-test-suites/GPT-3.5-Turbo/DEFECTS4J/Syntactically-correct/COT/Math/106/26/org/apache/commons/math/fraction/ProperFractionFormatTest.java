
import static org.junit.Assert.assertEquals;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat formatter;

    @Before
    public void setUp() {
        formatter = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = formatter.format(fraction, buffer, pos);
        assertEquals("1 1/2", result.toString());
    }

    @Test
    public void testParse() {
        String source = "1 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = formatter.parse(source, pos);
        assertEquals(new Fraction(3, 2), result);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidInput() {
        String source = "1/2 3";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = formatter.parse(source, pos);
    }
}
