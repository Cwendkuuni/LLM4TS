import org.junit.Test;
import java.text.NumberFormat;
import java.text.ParsePosition;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

  // Test case for format(Fraction fraction, StringBuffer toAppendTo, FieldPosition pos)
    @Test
    public void formatFraction() {
        Fraction fraction = new Fraction(3, 16);
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuffer stringBuffer = new StringBuffer();
        properFractionFormat.format(fraction, stringBuffer, new FieldPosition(0));
        assertEquals("3/16", stringBuffer.toString());
    }

  // Test case for parse(String source, ParsePosition pos)
    @Test
    public void parseString() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("5/8", parsePosition);
        assertEquals(5, fraction.getNumerator());
        assertEquals(8, fraction.getDenominator());
        assertEquals(4, parsePosition.getIndex());
    }

  // Test case for parse(String source, ParsePosition pos) - Invalid whole number
    @Test
    public void parseInvalidWholeNumberString() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("-5/8", parsePosition);
        assertNull(fraction);
        assertEquals(0, parsePosition.getIndex());
        assertEquals(0, parsePosition.getErrorIndex());
    }

  // Test case for parse(String source, ParsePosition pos) - Invalid fraction
    @Test
    public void parseInvalidFractionString() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("5/0", parsePosition);
        assertNull(fraction);
        assertEquals(0, parsePosition.getIndex());
        assertEquals(2, parsePosition.getErrorIndex());
    }

  // Test case for getWholeFormat() and setWholeFormat(NumberFormat format)
    @Test
    public void getAndSetWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat numberFormat = NumberFormat.getInstance();
        properFractionFormat.setWholeFormat(numberFormat);
        assertEquals(numberFormat, properFractionFormat.getWholeFormat());
    }
    
}