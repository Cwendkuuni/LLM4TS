import static org.junit.Assert.*;
import org.junit.Test;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;

public class ProperFractionFormatTest {

    private NumberFormat testNumberFormat() {
        NumberFormat format = NumberFormat.getNumberInstance();
      	return format;
    }

    private ProperFractionFormat initializeProperFractionFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(testNumberFormat());
        return properFractionFormat;
    }

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        assertTrue(properFractionFormat.getWholeFormat().equals(FractionFormat.getDefaultNumberFormat()));
    }

    @Test
    public void testConstructorWithFormat() {
        NumberFormat numberFormat = testNumberFormat();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(numberFormat);
        assertTrue(properFractionFormat.getWholeFormat().equals(numberFormat));
    }

    @Test
    public void testConstructorWithFormats() {
        NumberFormat numberFormat = testNumberFormat();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
        assertTrue(properFractionFormat.getWholeFormat().equals(numberFormat));
        assertTrue(properFractionFormat.getNumeratorFormat().equals(numberFormat));
        assertTrue(properFractionFormat.getDenominatorFormat().equals(numberFormat));
    }

    @Test
    public void testFormat() {
        ProperFractionFormat properFractionFormat = initializeProperFractionFormat();
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        properFractionFormat.format(fraction, sb, pos);
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat properFractionFormat = initializeProperFractionFormat();
        NumberFormat numberFormat = testNumberFormat();
        properFractionFormat.setWholeFormat(numberFormat);
        assertTrue(properFractionFormat.getWholeFormat().equals(numberFormat));
    }

    @Test
    public void testParse() {
        ProperFractionFormat properFractionFormat = initializeProperFractionFormat();
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = new Fraction(3, 4);
        Fraction parsedFraction = properFractionFormat.parse(source, pos);
        assertTrue(parsedFraction.equals(fraction));

        source = "3";
        pos.setIndex(0);
        parsedFraction = properFractionFormat.parse(source, pos);
        assertTrue(parsedFraction.equals(new Fraction(3, 1)));

        source = "3 / 0";
        pos.setIndex(0);
        parsedFraction = properFractionFormat.parse(source, pos);
        assertNull(parsedFraction);

        source = "3 / -4";
        pos.setIndex(0);
        parsedFraction = properFractionFormat.parse(source, pos);
        assertNull(parsedFraction);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNull() {
        ProperFractionFormat properFractionFormat = initializeProperFractionFormat();
        properFractionFormat.setWholeFormat(null);
    }

    @Test
    public void testToString() {
        ProperFractionFormat properFractionFormat = initializeProperFractionFormat();
        assertNotNull(properFractionFormat.toString());
    }

    @Test
    public void testHashCode() {
        ProperFractionFormat properFractionFormat1 = initializeProperFractionFormat();
        ProperFractionFormat properFractionFormat2 = initializeProperFractionFormat();
        assertEquals(properFractionFormat1.hashCode(), properFractionFormat2.hashCode());
    }

    @Test
    public void testEquals() {
        ProperFractionFormat properFractionFormat1 = initializeProperFractionFormat();
        ProperFractionFormat properFractionFormat2 = initializeProperFractionFormat();
        assertTrue(properFractionFormat1.equals(properFractionFormat1));
        assertTrue(properFractionFormat1.equals(properFractionFormat2));
        assertFalse(properFractionFormat1.equals(null));
        assertFalse(properFractionFormat1.equals(new Object()));
    }
}