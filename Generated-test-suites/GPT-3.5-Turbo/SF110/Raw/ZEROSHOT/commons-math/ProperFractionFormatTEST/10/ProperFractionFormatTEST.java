import org.junit.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }

    @Test
    public void testNumberFormatConstructor() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(numberFormat);
        assertNotNull(format);
    }

    @Test
    public void testAllFormatConstructor() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(format);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        StringBuffer expectedResult = new StringBuffer("3 / 4");
        assertEquals(expectedResult.toString(), format.format(fraction, buffer, position).toString());
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
        String source = "2 / 3";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertTrue(fraction instanceof Fraction);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
        assertEquals(source.length(), position.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

}