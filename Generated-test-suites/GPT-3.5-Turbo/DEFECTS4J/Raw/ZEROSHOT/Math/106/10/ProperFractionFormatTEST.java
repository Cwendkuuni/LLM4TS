import static org.junit.Assert.*;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.apache.commons.math.fraction.Fraction;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormatZeroFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 1);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        properFractionFormat.format(fraction, buffer, position);
        assertEquals("0", buffer.toString());
    }

    @Test
    public void testFormatProperFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction1 = new Fraction(7, 2);
        StringBuffer buffer1 = new StringBuffer();
        FieldPosition position1 = new FieldPosition(0);
        properFractionFormat.format(fraction1, buffer1, position1);
        assertEquals("3 1/2", buffer1.toString());

        Fraction fraction2 = new Fraction(-7, 2);
        StringBuffer buffer2 = new StringBuffer();
        FieldPosition position2 = new FieldPosition(0);
        properFractionFormat.format(fraction2, buffer2, position2);
        assertEquals("-3 1/2", buffer2.toString());

        Fraction fraction3 = new Fraction(-3, 8);
        StringBuffer buffer3 = new StringBuffer();
        FieldPosition position3 = new FieldPosition(0);
        properFractionFormat.format(fraction3, buffer3, position3);
        assertEquals("-3/8", buffer3.toString());
    }

    @Test
    public void testParseZeroFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String source = "0";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, position);
        assertEquals(0, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testParseProperFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String source1 = "3 1/2";
        ParsePosition position1 = new ParsePosition(0);
        Fraction fraction1 = properFractionFormat.parse(source1, position1);
        assertEquals(7, fraction1.getNumerator());
        assertEquals(2, fraction1.getDenominator());

        String source2 = "-3 1/2";
        ParsePosition position2 = new ParsePosition(0);
        Fraction fraction2 = properFractionFormat.parse(source2, position2);
        assertEquals(-7, fraction2.getNumerator());
        assertEquals(2, fraction2.getDenominator());

        String source3 = "-3/8";
        ParsePosition position3 = new ParsePosition(0);
        Fraction fraction3 = properFractionFormat.parse(source3, position3);
        assertEquals(-3, fraction3.getNumerator());
        assertEquals(8, fraction3.getDenominator());
    }
}