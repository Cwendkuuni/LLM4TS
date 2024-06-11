import static org.junit.Assert.*;
import org.junit.Test;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }

    @Test
    public void testProperFractionFormatCustomConstructor() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat fractionFormat = new ProperFractionFormat(format);
        assertNotNull(fractionFormat);
    }
    
    @Test
    public void testProperFractionFormatCustomConstructor() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getCurrencyInstance();
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance();
        ProperFractionFormat fractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(fractionFormat);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer result = format.format(fraction, new StringBuffer(), new FieldPosition(0));
        assertEquals("3 / 4", result.toString());
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
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("3 / 4", pos);
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test
    public void testFormatWithNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-5, 3);
        StringBuffer result = format.format(fraction, new StringBuffer(), new FieldPosition(0));
        assertEquals("-1 2 / 3", result.toString());
    }

    @Test
    public void testFormatWithNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, -5);
        StringBuffer result = format.format(fraction, new StringBuffer(), new FieldPosition(0));
        assertEquals("-2 / 5", result.toString());
    }

    @Test
    public void testFormatWithNegativeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, -4);
        StringBuffer result = format.format(fraction, new StringBuffer(), new FieldPosition(0));
        assertEquals("-3 / 4", result.toString());
    }

    @Test
    public void testParseWithInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("2 - 3 / 4", pos);
        assertNull(fraction);
        assertEquals(2, pos.getErrorIndex());
    }
}