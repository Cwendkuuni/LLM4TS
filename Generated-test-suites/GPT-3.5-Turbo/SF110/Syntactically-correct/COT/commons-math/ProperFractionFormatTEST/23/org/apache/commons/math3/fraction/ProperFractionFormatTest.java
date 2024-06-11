import org.apache.commons.math3.fraction.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.text.*;
import org.junit.Before;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        StringBuffer expected = new StringBuffer();
        Fraction fraction = new Fraction(1, 2);
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), pos);

        expected.append("1 / 2");
        assertEquals(expected.toString(), result.toString());
        assertEquals(0, pos.getBeginIndex());
        assertEquals(0, pos.getEndIndex());
    }

    @Test
    public void testFormat_withWholeNumber() {
        StringBuffer expected = new StringBuffer();
        Fraction fraction = new Fraction(3, 2);
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), pos);

        expected.append("1 1 / 2");
        assertEquals(expected.toString(), result.toString());
        assertEquals(0, pos.getBeginIndex());
        assertEquals(0, pos.getEndIndex());
    }

    @Test
    public void testFormat_withNegativeNumber() {
        StringBuffer expected = new StringBuffer();
        Fraction fraction = new Fraction(-3, 2);
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), pos);

        expected.append("-1 1 / 2");
        assertEquals(expected.toString(), result.toString());
        assertEquals(0, pos.getBeginIndex());
        assertEquals(0, pos.getEndIndex());
    }

    @Test
    public void testParse() {
        String source = "1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(1, 2);
        Fraction result = properFractionFormat.parse(source, pos);

        assertEquals(expected, result);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testParse_withWholeNumber() {
        String source = "1 1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(3, 2);
        Fraction result = properFractionFormat.parse(source, pos);

        assertEquals(expected, result);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testParse_withNegativeNumber() {
        String source = "-1 1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(-3, 2);
        Fraction result = properFractionFormat.parse(source, pos);

        assertEquals(expected, result);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testParse_unparsableFraction() {
        String source = "abc";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
    }
}