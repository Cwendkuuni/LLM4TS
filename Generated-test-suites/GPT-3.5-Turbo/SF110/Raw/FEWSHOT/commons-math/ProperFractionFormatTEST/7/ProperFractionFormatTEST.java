import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.FieldPosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {
    private ProperFractionFormat properFractionFormat;
  
    @Before
    public void setup() {
        properFractionFormat = new ProperFractionFormat();
    }
  
    @Test
    public void testConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ProperFractionFormat properFractionFormatWithNumberFormat = new ProperFractionFormat(NumberFormat.getInstance());

        assertEquals(NumberFormat.getInstance(), properFractionFormat.getWholeFormat());
        assertEquals(NumberFormat.getInstance(), properFractionFormatWithNumberFormat.getWholeFormat());
    }
  
    @Test
    public void testConstructorWithNumberFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(numberFormat);

        assertEquals(numberFormat, properFractionFormat.getWholeFormat());
    }

    @Test
    public void testConstructorWithFormats() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getCurrencyInstance();
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance();

        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
        assertEquals(numeratorFormat, properFractionFormat.getNumeratorFormat());
        assertEquals(denominatorFormat, properFractionFormat.getDenominatorFormat());
    }
  
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);

        properFractionFormat.format(fraction, stringBuffer, fieldPosition);

        assertEquals("3 / 4", stringBuffer.toString());
        assertEquals(0, fieldPosition.getBeginIndex());
        assertEquals(0, fieldPosition.getEndIndex());
    }
  
    @Test
    public void testFormatWithWholePart() {
        Fraction fraction = new Fraction(7, 4);
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);

        properFractionFormat.format(fraction, stringBuffer, fieldPosition);

        assertEquals("1 3 / 4", stringBuffer.toString());
        assertEquals(0, fieldPosition.getBeginIndex());
        assertEquals(0, fieldPosition.getEndIndex());
    }
  
    @Test
    public void testFormatWithNegativeFraction() {
        Fraction fraction = new Fraction(-3, 4);
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);

        properFractionFormat.format(fraction, stringBuffer, fieldPosition);

        assertEquals("-3 / 4", stringBuffer.toString());
        assertEquals(0, fieldPosition.getBeginIndex());
        assertEquals(0, fieldPosition.getEndIndex());
    }
  
    @Test
    public void testParse() {
        String source = "2 / 3";
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, parsePosition);

        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
        assertEquals(5, parsePosition.getIndex());
    }
  
    @Test
    public void testParseWithWholePart() {
        String source = "1 2 / 3";
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, parsePosition);

        assertEquals(5, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
        assertEquals(9, parsePosition.getIndex());
    }
  
    @Test
    public void testParseWithInvalidFraction() {
        String source = "2 - 3";
        ParsePosition parsePosition = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse(source, parsePosition);

        assertNull(fraction);
        assertEquals(0, parsePosition.getIndex());
        assertEquals(2, parsePosition.getErrorIndex());
    }
  
    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullFormat() {
        properFractionFormat.setWholeFormat(null);
    }
}