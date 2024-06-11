import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer expectedResult = new StringBuffer(10);
        format.format(fraction, expectedResult, new FieldPosition(0, 0));
        String result = expectedResult.toString();
        String expected = "1/2";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCustomProperFractionFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        wholeFormat.setMinimumFractionDigits(2);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = new Fraction(3, 4);
        StringBuffer expectedResult = new StringBuffer(10);
        format.format(fraction, expectedResult, new FieldPosition(0, 0));
        String result = expectedResult.toString();
        String expected = "3.00/4";
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNumberFormat() {
        // Arrange: wholeFormat is null
        NumberFormat wholeFormat = null;
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, null, null);
    }

    @Test(expected = ParseException.class)
    public void testInvalidProperFractionFormatParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String input = "-3 -1/2";
        Fraction fraction = format.parse(input, pos);
    }

    @Test(expected = ParseException.class)
    public void testInvalidProperFractionFormatParse2() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String input = "3 -1/2";
        format.parse(input, pos); // this should parse correctly
        // Arrange: pos is reset at the end of the parse
        String input2 = "1/0";
        Fraction fraction = format.parse(input2, pos);
    }

    @Test
    public void testBigDecimalProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(new BigDecimal("1.5"), new BigDecimal("3"));
        StringBuffer expectedResult = new StringBuffer(10);
        format.format(fraction, expectedResult, new FieldPosition(0, 0));
        String result = expectedResult.toString();
        String expected = "1/2";
        Assert.assertEquals(expected, result);
    }
}