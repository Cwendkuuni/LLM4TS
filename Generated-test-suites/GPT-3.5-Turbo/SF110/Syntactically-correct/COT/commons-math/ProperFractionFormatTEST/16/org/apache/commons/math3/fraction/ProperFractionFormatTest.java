import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.FieldPosition;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
    }

    @Test
    public void testNumberFormatConstructor() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(numberFormat);
    }

    @Test
    public void testNumberFormatConstructorWithFormats() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer result = format.format(fraction, toAppendTo, pos);
        // assert formatted output is correct
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        // assert whole format is correct
    }

    @Test
    public void testParse() {
        String source = "1 1/2";
        ParsePosition pos = new ParsePosition(0);
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse(source, pos);
        // assert parsed fraction is correct
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

}