
import org.apache.commons.math3.fraction.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testConstructor1() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }
    
    @Test
    public void testConstructor2() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance());
        assertNotNull(format);
    }

    @Test
    public void testConstructor3() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getInstance(), NumberFormat.getInstance(), NumberFormat.getInstance());
        assertNotNull(format);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        format.format(fraction, buffer, position);
        assertEquals("3 / 4", buffer.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format.getWholeFormat());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 / 4";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat numberFormat = NumberFormat.getInstance();
        format.setWholeFormat(numberFormat);
        assertEquals(numberFormat, format.getWholeFormat());
    }

}

