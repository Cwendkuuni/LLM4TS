import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.NumberFormat;

public class ProperFractionFormatTest {

    ProperFractionFormat format;

    @Before
    public void setUp() {
        format = new ProperFractionFormat(NumberFormat.getInstance());
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer expected = new StringBuffer();
        expected.append("3 / 4");

        format.format(fraction, sb, pos);

        assertEquals(expected.toString(), sb.toString());
        assertEquals(0, pos.getBeginIndex());
        assertEquals(0, pos.getEndIndex());
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test
    public void testParse() {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(3, 4);

        Fraction result = format.parse(source, pos);

        assertNotNull(result);
        assertEquals(expected.getNumerator(), result.getNumerator());
        assertEquals(expected.getDenominator(), result.getDenominator());
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat newWholeFormat = NumberFormat.getCurrencyInstance();
        format.setWholeFormat(newWholeFormat);

        assertEquals(newWholeFormat, format.getWholeFormat());
    }
}