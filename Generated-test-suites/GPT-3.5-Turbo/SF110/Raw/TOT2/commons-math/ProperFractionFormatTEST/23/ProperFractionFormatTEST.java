import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.*;

public class ProperFractionFormatTest {

    @Test
    public void testConstructorDefault() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
        assertNotNull(format.getWholeFormat());
        assertNotNull(format.getNumeratorFormat());
        assertNotNull(format.getDenominatorFormat());
    }

    @Test
    public void testConstructorWithNumberFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(numberFormat);
        assertNotNull(format);
        assertNotNull(format.getWholeFormat());
        assertNotNull(format.getNumeratorFormat());
        assertNotNull(format.getDenominatorFormat());
    }

    @Test
    public void testConstructorWithWholeNumeratorDenominatorFormat() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(format);
        assertNotNull(format.getWholeFormat());
        assertNotNull(format.getNumeratorFormat());
        assertNotNull(format.getDenominatorFormat());
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer result = format.format(fraction, buffer, position);
        assertEquals("2 1/2", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 4/5";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertEquals(new Fraction(19, 5), fraction);
    }

    // Add more test cases for edge cases and error scenarios

}