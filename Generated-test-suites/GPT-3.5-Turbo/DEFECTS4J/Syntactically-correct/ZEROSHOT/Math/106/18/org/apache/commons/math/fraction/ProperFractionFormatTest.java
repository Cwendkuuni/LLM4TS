import static org.junit.Assert.*;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        properFractionFormat.format(fraction, result, pos);
        assertEquals("3 / 4", result.toString());
    }

    @Test
    public void testFormatWithWholeNumber() {
        Fraction fraction = new Fraction(5, 4);
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        properFractionFormat.format(fraction, result, pos);
        assertEquals("1 1/4", result.toString());
    }

    @Test
    public void testFormatWithNegativeFraction() {
        Fraction fraction = new Fraction(-3, 4);
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        properFractionFormat.format(fraction, result, pos);
        assertEquals("-3 / 4", result.toString());
    }

    @Test
    public void testFormatWithNegativeWholeNumber() {
        Fraction fraction = new Fraction(-5, 4);
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        properFractionFormat.format(fraction, result, pos);
        assertEquals("-1 1/4", result.toString());
    }

    @Test
    public void testParse() {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testParseWithWholeNumber() {
        String source = "1 1/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertEquals(5, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testParseWithNegativeFraction() {
        String source = "-3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertEquals(-3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testParseWithNegativeWholeNumber() {
        String source = "-1 1/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(source, pos);
        assertEquals(-5, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }
}