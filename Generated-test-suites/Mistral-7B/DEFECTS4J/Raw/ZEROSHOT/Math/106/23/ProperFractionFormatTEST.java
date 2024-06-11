import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
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
    public void testDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test formatting
        Fraction fraction = new Fraction(1, 2);
        StringBuffer expected = new StringBuffer();
        format.format(fraction, expected, null);
        Assert.assertEquals("1 / 2", expected.toString());

        // Test parsing
        String source = "1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(source, pos);
        Assert.assertNotNull(parsed);
        Assert.assertEquals(fraction, parsed);

        // Test error parsing
        source = "1 / 3 - 2 / 4";
        pos.setIndex(0);
        parsed = format.parse(source, pos);
        Assert.assertNull(parsed);
        Assert.assertEquals(4, pos.getErrorIndex());
    }

    @Test
    public void testCustomFormat() {
        Locale locale = Locale.US;

        // Test formatting with custom formats
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(locale);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(locale);
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance(locale);
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        fractionTestFormatAndParse(format, locale, new Fraction(BigInteger.valueOf(3), BigInteger.valueOf(2)), "3 % %", "3 % %");
        fractionTestFormatAndParse(format, locale, new Fraction(BigDecimal.valueOf(3, 1), BigDecimal.valueOf(4)), "3 % %", "3 % %");
        fractionTestFormatAndParse(format, locale, new Fraction(BigInteger.valueOf(-3), BigInteger.valueOf(2)), "-3 % %", "-3 % %");
        fractionTestFormatAndParse(format, locale, new Fraction(BigDecimal.valueOf(-3, 1), BigDecimal.valueOf(2)), "-3 % %", "-3 % %");
        fractionTestFormatAndParse(format, locale, new Fraction(new BigInteger("12345678901234567890"), BigInteger.valueOf(1234567890)), "12345678901234567890 % %", "12345678901234567890 % %");

        // Test parsing with custom formats
        wholeFormat.setMinimumIntegerDigits(2);
        numeratorFormat.setMinimumFractalsDigits(2);
        denominatorFormat.setMinimumFractalsDigits(2);

        fractionTestFormatAndParse(format, locale, new Fraction(BigInteger.valueOf(3), BigInteger.valueOf(2)), "03 % %", "3 % %");
        fractionTestFormatAndParse(format, locale, new Fraction(BigDecimal.valueOf(3, 1), BigDecimal.valueOf(4)), "003.0 % %", "3 % %");
        fractionTestFormatAndParse(format, locale, new Fraction(BigInteger.valueOf(-3), BigInteger.valueOf(2)), "-03 % %", "-3 % %");
        fractionTestFormatAndParse(format, locale, new Fraction(BigDecimal.valueOf(-3, 1), BigDecimal.valueOf(2)), "-003.0 % %", "-3 % %");
        fractionTestFormatAndParse(format, locale, new Fraction(BigInteger.valueOf(12345678901234567890), BigInteger.valueOf(1234567890)), "012345678901234567890 % %", "12345678901234567890 % %");

        // Test parsing with invalid input
        String invalidSource = "3 % % / 2 % %";
        pos.setIndex(0);
        parsed = format.parse(invalidSource, pos);
        Assert.assertNull(parsed);
        Assert.assertEquals(15, pos.getErrorIndex());
    }

    private void fractionTestFormatAndParse(ProperFractionFormat format, Locale locale, Fraction fraction, String source, String expected) {
        // Test formatting
        StringBuffer buffer = new StringBuffer();
        format.format(fraction, buffer, null);
        Assert.assertEquals(expected, buffer.toString());

        // Test parsing
        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(source, pos);
        Assert.assertNotNull(parsed);
        Assert.assertEquals(fraction, parsed);
    }
}