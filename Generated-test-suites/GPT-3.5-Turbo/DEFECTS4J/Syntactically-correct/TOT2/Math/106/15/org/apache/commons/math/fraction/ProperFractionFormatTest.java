import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {

    @Test
    public void testFormat_shouldFormatFractionInProperFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);

        properFractionFormat.format(fraction, stringBuffer, pos);

        assertEquals("1 1/2", stringBuffer.toString());
    }

    @Test
    public void testParse_shouldParseFractionStringToFractionObject() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "1 1/2";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionString, pos);

        assertNotNull(result);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testParse_shouldReturnNullForInvalidFractionString() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "invalid";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionString, pos);

        assertNull(result);
    }

    @Test
    public void testSetWholeFormat_shouldSetWholeNumberFormatCorrectly() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat numberFormat = NumberFormat.getInstance();
        properFractionFormat.setWholeFormat(numberFormat);

        assertEquals(numberFormat, properFractionFormat.getWholeFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormat_shouldThrowIllegalArgumentExceptionForNullFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();

        properFractionFormat.setWholeFormat(null);
    }

}