import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.simpledefense.ReflectionTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;
    private NumberFormat wholeFormat;
    private NumberFormat numeratorFormat;
    private NumberFormat denominatorFormat;

    @BeforeEach
    public void setup() {
        Locale locale = Locale.ENGLISH;
        wholeFormat = NumberFormat.getNumberInstance(locale);
        numeratorFormat = NumberFormat.getNumberInstance(locale);
        denominatorFormat = NumberFormat.getNumberInstance(locale);
        properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
    }

    @Test
    public void testFormatFraction() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer expected = new StringBuffer();
        properFractionFormat.format(fraction, expected, null);
        String formatted = expected.toString();
        String expectedFormatted = "2 / 3";
        Assertions.assertEquals(expectedFormatted, formatted);

        fraction = new Fraction(-2, 3);
        expected.setLength(0);
        properFractionFormat.format(fraction, expected, null);
        formatted = expected.toString();
        expectedFormatted = "-2 / 3";
        Assertions.assertEquals(expectedFormatted, formatted);

        fraction = new Fraction(0, 3);
        expected.setLength(0);
        properFractionFormat.format(fraction, expected, null);
        formatted = expected.toString();
        expectedFormatted = "0";
        Assertions.assertEquals(expectedFormatted, formatted);

        fraction = new Fraction(3, 2);
        expected.setLength(0);
        properFractionFormat.format(fraction, expected, null);
        formatted = expected.toString();
        expectedFormatted = "3 / 2";
        Assertions.assertEquals(expectedFormatted, formatted);

        fraction = new Fraction(4, 2);
        expected.setLength(0);
        properFractionFormat.format(fraction, expected, null);
        formatted = expected.toString();
        expectedFormatted = "2 2 / 1";
        Assertions.assertEquals(expectedFormatted, formatted);

        fraction = new Fraction(-4, 2);
        expected.setLength(0);
        properFractionFormat.format(fraction, expected, null);
        formatted = expected.toString();
        expectedFormatted = "-2 -2 / 1";
        Assertions.assertEquals(expectedFormatted, formatted);

        fraction = new Fraction(BigInteger.valueOf(1234567890), BigInteger.valueOf(234567890));
        expected.setLength(0);
        properFractionFormat.format(fraction, expected, null);
        formatted = expected.toString();
        expectedFormatted = "1234567890 / 234567890";
        Assertions.assertEquals(expectedFormatted, formatted);

        fraction = new Fraction(new BigDecimal("1.234567891"), new BigDecimal("2.345678901"));
        expected.setLength(0);
        properFractionFormat.format(fraction, expected, null);
        formatted = expected.toString();
        expectedFormatted = "1.234567891 / 2.345678901";
        Assertions.assertEquals(expectedFormatted, formatted);
    }

    @Test
    public void testParseFraction() {
        String source = "2 / 3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNotNull(fraction);
        Assertions.assertEquals(2, fraction.getNumerator());
        Assertions.assertEquals(3, fraction.getDenominator());

        source = "-2 / 3";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNotNull(fraction);
        Assertions.assertEquals(-2, fraction.getNumerator());
        Assertions.assertEquals(3, fraction.getDenominator());

        source = "0";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNull(fraction);
        Assertions.assertEquals(0, pos.getIndex());

        source = "3 / 2";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNotNull(fraction);
        Assertions.assertEquals(3, fraction.getNumerator());
        Assertions.assertEquals(2, fraction.getDenominator());

        source = "2 2 / 1";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNotNull(fraction);
        Assertions.assertEquals(2, fraction.getNumerator());
        Assertions.assertEquals(2, fraction.getDenominator());
        Assertions.assertEquals(1, properFractionFormat.getDenominatorFormat().getMaximumFractionDigits());

        source = "-2 -2 / 1";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNotNull(fraction);
        Assertions.assertEquals(-2, fraction.getNumerator());
        Assertions.assertEquals(-2, fraction.getDenominator());
        Assertions.assertEquals(1, properFractionFormat.getDenominatorFormat().getMaximumFractionDigits());

        source = "1234567890 / 234567890";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNotNull(fraction);
        Assertions.assertEquals(1234567890, fraction.getNumerator().longValue());
        Assertions.assertEquals(234567890, fraction.getDenominator().longValue());

        source = "1.234567891 / 2.345678901";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNotNull(fraction);
        Assertions.assertEquals(new BigDecimal("1.234567891"), fraction.getNumber());
        Assertions.assertEquals(new BigDecimal("2.345678901"), fraction.getDenominator());

        source = "1 / 0";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNull(fraction);
        Assertions.assertEquals(-1, pos.getErrorIndex());

        source = "3 / 0";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNull(fraction);
        Assertions.assertEquals(-1, pos.getErrorIndex());

        source = "2 -1 / 2";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNull(fraction);
        Assertions.assertEquals(-1, pos.getErrorIndex());

        source = "2 -1 / -2";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNotNull(fraction);
        Assertions.assertEquals(-2, fraction.getNumerator());
        Assertions.assertEquals(1, fraction.getDenominator().intValue());

        // Test setWholeFormat
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        ProperFractionFormat newProperFractionFormat = ReflectionTestUtils.getFieldValue(properFractionFormat, "wholeFormat");
        ReflectionTestUtils.setFieldValue(properFractionFormat, "wholeFormat", wholeFormat);
        String expectedWholeFormat = "##0.##";
        wholeFormat.setPattern(expectedWholeFormat);
        source = "2.5 / 3";
        pos.setIndex(0);
        fraction = properFractionFormat.parse(source, pos);
        Assertions.assertNotNull(fraction);
        Assertions.assertEquals(2, fraction.getNumerator().intValue());
        Assertions.assertEquals(5, fraction.getDenominator().intValue());
        Assertions.assertEquals("2.5", wholeFormat.format(new Double(2.5)));
    }
}