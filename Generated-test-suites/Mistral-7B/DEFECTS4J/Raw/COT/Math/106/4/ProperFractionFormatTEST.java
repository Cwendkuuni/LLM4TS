import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.NumberFormat;
import java.math.ParsePosition;
import java.text.FieldPosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        String actual = format.format(fraction, new StringBuffer(), null);
        String expected = "1 / 2";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCustomWholeFormatConstructor() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        String actual = format.format(fraction, toAppendTo, pos);
        String expected = wholeFormat.format(1) + " " + "1 / 2";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatMethod() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 3);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        format.format(fraction, toAppendTo, pos);
        String actual = toAppendTo.toString();
        String expected = "1 / 3";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

    @Test
    public void testParseMethodWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "3";
        Fraction fraction = format.parse(source, pos);
        Assert.assertEquals(new Fraction(3, 1), fraction);
    }

    @Test
    public void testParseMethodWithProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "1 2/3";
        Fraction fraction = format.parse(source, pos);
        Assert.assertEquals(new Fraction(1, 2, 3), fraction);
    }

    @Test(expected = ParseException.class)
    public void testParseMethodWithImproperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "2 2/3";
        format.parse(source, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseMethodWithWholeNumberNegativeSign() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "-3";
        format.parse(source, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseMethodWithProperFractionNegativeSignWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "-3 2/3";
        format.parse(source, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseMethodWithProperFractionNegativeSignNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "3 -2/3";
        format.parse(source, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseMethodWithProperFractionNegativeSignDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "3 2/-3";
        format.parse(source, pos);
    }

    @Test
    public void testParseMethodWithBigDecimal() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        wholeFormat.setParseBigDecimal(true);
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        ParsePosition pos = new ParsePosition(0);
        String source = "3.5 2/3";
        Fraction fraction = format.parse(source, pos);
        BigDecimal expectedWhole = new BigDecimal("3.5");
        BigDecimal expectedNumerator = new BigDecimal("2");
        BigInteger expectedDenominator = new BigInteger("3");
        Assert.assertEquals(new Fraction(expectedWhole, new Fraction(expectedNumerator, expectedDenominator)), fraction);
    }

    @Test
    public void testParseMethodWithBigInteger() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        ParsePosition pos = new ParsePosition(0);
        String source = "3 2/-3";
        Fraction fraction = format.parse(source, pos);
        BigInteger expectedWhole = new BigInteger("3");
        BigInteger expectedNumerator = new BigInteger("2");
        BigInteger expectedDenominator = new BigInteger("-3");
        Assert.assertEquals(new Fraction(expectedWhole, new Fraction(expectedNumerator, expectedDenominator)), fraction);
    }
}