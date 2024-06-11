import org.junit.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatConstructor1() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }

    @Test
    public void testProperFractionFormatConstructor2() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(format);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer expected = new StringBuffer("1 1 / 2");
        assertEquals(expected.toString(), format.format(fraction, result, pos).toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat newFormat = NumberFormat.getInstance();
        format.setWholeFormat(newFormat);
        assertEquals(newFormat, format.getWholeFormat());
    }

}