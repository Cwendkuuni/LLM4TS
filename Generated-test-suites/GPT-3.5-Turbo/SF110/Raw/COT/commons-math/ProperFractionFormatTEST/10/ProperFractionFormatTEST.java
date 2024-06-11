import org.junit.Test;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.exception.NullArgumentException;

import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }

    @Test
    public void testConstructorWithNumberFormat() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance());
        assertNotNull(format);
    }

    @Test
    public void testConstructorWithNumberFormats() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance(), NumberFormat.getInstance(), NumberFormat.getInstance());
        assertNotNull(format);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, 3);
        String expected = "2 / 3";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction expected = new Fraction(2, 3);
        String source = "2 / 3";
        Fraction result = format.parse(source);
        assertEquals(expected, result);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}