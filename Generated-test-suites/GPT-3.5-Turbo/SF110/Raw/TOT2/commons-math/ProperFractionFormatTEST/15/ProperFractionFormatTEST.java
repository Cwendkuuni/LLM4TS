import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        assertEquals("1 1 / 2", format.format(fraction, buffer, pos).toString());
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
        NumberFormat newFormat = NumberFormat.getInstance();
        format.setWholeFormat(newFormat);
        assertEquals(newFormat, format.getWholeFormat());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("2 1 / 3", pos);
        assertEquals(new Fraction(7, 3), fraction);
    }

    @Test
    public void testConstructorWithNumberFormat() {
        NumberFormat format = NumberFormat.getNumberInstance();
        ProperFractionFormat properFormat = new ProperFractionFormat(format);
        assertNotNull(properFormat);
    }

    @Test
    public void testConstructorWithWholeNumeratorDenominatorFormat() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance();
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance();
        ProperFractionFormat properFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(properFormat);
    }
    
    // Add more test cases for edge cases and exception handling scenarios here
    
}