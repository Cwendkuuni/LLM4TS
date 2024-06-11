import org.junit.Test;
import org.junit.Assert;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        format.format(fraction, buffer, position);
        Assert.assertEquals("1 / 2", buffer.toString());
    }

    @Test
    public void testFormat_WholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        format.format(fraction, buffer, position);
        Assert.assertEquals("1 1 / 2", buffer.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 / 2";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        Assert.assertEquals(new Fraction(1, 2), fraction);
        Assert.assertEquals(source.length(), position.getIndex());
    }

    @Test
    public void testParse_WholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 1 / 2";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        Assert.assertEquals(new Fraction(3, 2), fraction);
        Assert.assertEquals(source.length(), position.getIndex());
    }

    @Test
    public void testParse_InvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 /";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        Assert.assertNull(fraction);
        Assert.assertEquals(-1, position.getErrorIndex());
    }

    @Test
    public void testParse_InvalidWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "x / 2";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        Assert.assertNull(fraction);
        Assert.assertEquals(0, position.getIndex());
        Assert.assertEquals(0, position.getErrorIndex());
    }
}