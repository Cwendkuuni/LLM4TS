import static org.junit.Assert.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        assertNotNull(properFractionFormat);
    }

    @Test
    public void testFormatMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuffer stringBuffer = new StringBuffer();
        Fraction fraction = new Fraction(3, 4);
        FieldPosition fieldPosition = new FieldPosition(0);
        StringBuffer result = properFractionFormat.format(fraction, stringBuffer, fieldPosition);
        assertEquals("3 / 4", result.toString());
    }

    @Test
    public void testParseMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String source = "3 / 4";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, parsePosition);
        assertNotNull(fraction);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testSetWholeFormatMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat numberFormat = NumberFormat.getInstance();
        properFractionFormat.setWholeFormat(numberFormat);
        assertEquals(numberFormat, properFractionFormat.getWholeFormat());
    }
}