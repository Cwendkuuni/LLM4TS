import org.junit.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testConstructor1() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format.getWholeFormat());
        assertNotNull(format.getNumeratorFormat());
        assertNotNull(format.getDenominatorFormat());
    }

    @Test
    public void testConstructor2() {
        NumberFormat nf = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(nf);
        assertEquals(nf, format.getWholeFormat());
        assertEquals(nf, format.getNumeratorFormat());
        assertEquals(nf, format.getDenominatorFormat());
    }

    @Test
    public void testConstructor3() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
        assertEquals(numeratorFormat, format.getNumeratorFormat());
        assertEquals(denominatorFormat, format.getDenominatorFormat());
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, 3);
        String result = format.format(fraction);
        assertEquals("2 / 3", result);
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat nf = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(nf);
        assertEquals(nf, format.getWholeFormat());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 / 3";
        Fraction fraction = format.parse(source);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat nf = NumberFormat.getInstance();
        format.setWholeFormat(nf);
        assertEquals(nf, format.getWholeFormat());
    }

}