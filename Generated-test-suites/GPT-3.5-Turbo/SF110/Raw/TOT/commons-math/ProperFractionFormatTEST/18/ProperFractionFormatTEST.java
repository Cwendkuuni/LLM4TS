import org.junit.Test;
import static org.junit.Assert.*;
import java.text.*;
import org.apache.commons.math3.fraction.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }

    @Test
    public void testConstructorWithNumberFormat() {
        DecimalFormat decimalFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(decimalFormat);
        assertNotNull(format);
        assertEquals(decimalFormat, format.getWholeFormat());
    }

    @Test
    public void testConstructorWithNumberFormats() {
        DecimalFormat wholeFormat = new DecimalFormat("#");
        DecimalFormat numeratorFormat = new DecimalFormat("#");
        DecimalFormat denominatorFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(format);
        assertEquals(wholeFormat, format.getWholeFormat());
        assertEquals(numeratorFormat, format.getNumeratorFormat());
        assertEquals(denominatorFormat, format.getDenominatorFormat());
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        format.format(fraction, sb, pos);
        assertEquals("3 / 4", sb.toString());

        fraction = new Fraction(-5, 3);
        sb = new StringBuffer();
        pos = new FieldPosition(0);
        format.format(fraction, sb, pos);
        assertEquals("-1 2 / 3", sb.toString());

        fraction = new Fraction(-5, -3);
        sb = new StringBuffer();
        pos = new FieldPosition(0);
        format.format(fraction, sb, pos);
        assertEquals("1 2 / 3", sb.toString());

        fraction = new Fraction(1, 1);
        sb = new StringBuffer();
        pos = new FieldPosition(0);
        format.format(fraction, sb, pos);
        assertEquals("1 / 1", sb.toString());

        fraction = new Fraction(0, 4);
        sb = new StringBuffer();
        pos = new FieldPosition(0);
        format.format(fraction, sb, pos);
        assertEquals("0", sb.toString());

        fraction = new Fraction(9, 3);
        sb = new StringBuffer();
        pos = new FieldPosition(0);
        format.format(fraction, sb, pos);
        assertEquals("3", sb.toString());

        fraction = new Fraction(0, 0);
        sb = new StringBuffer();
        pos = new FieldPosition(0);
        format.format(fraction, sb, pos);
        assertEquals("0", sb.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = format.parse("3 / 4", pos);
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());

        fraction = format.parse("-1 2 / 3", pos);
        assertNotNull(fraction);
        assertEquals(-7, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());

        fraction = format.parse("1 2 / 3", pos);
        assertNotNull(fraction);
        assertEquals(7, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());

        fraction = format.parse("1 / 1", pos);
        assertNotNull(fraction);
        assertEquals(1, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());

        fraction = format.parse("0", pos);
        assertNotNull(fraction);
        assertEquals(0, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());

        fraction = format.parse("3", pos);
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());

        fraction = format.parse("0 / 0", pos);
        assertNull(fraction);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

}