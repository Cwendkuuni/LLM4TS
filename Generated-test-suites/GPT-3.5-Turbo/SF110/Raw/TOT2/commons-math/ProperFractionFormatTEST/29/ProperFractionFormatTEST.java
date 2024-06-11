import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.NumberFormat;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 2);
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        String result = properFractionFormat.format(fraction, stringBuffer, fieldPosition).toString();

        Assert.assertEquals("2 1/2", result);
    }

    @Test
    public void testParse() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("2 1/2", parsePosition);

        Assert.assertEquals(new Fraction(5, 2), fraction);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat numberFormat = NumberFormat.getInstance();
        properFractionFormat.setWholeFormat(numberFormat);

        Assert.assertEquals(numberFormat, properFractionFormat.getWholeFormat());
    }
}