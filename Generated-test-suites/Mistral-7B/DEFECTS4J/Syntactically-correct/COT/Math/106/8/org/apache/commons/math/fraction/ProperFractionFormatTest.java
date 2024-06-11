import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.NumberFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction formattedFraction = properFractionFormat.format(new Fraction(1, 2), new StringBuilder(), null);
        Assert.assertEquals("1 / 2", formattedFraction.toString());
        
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse("1 / 2", parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(1, 2), parsedFraction);
    }

    @Test
    public void testCustomNumberFormatConstructor() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        StringBuilder stringBuilder = new StringBuilder();
        Fraction formattedFraction = properFractionFormat.format(new Fraction(3, 4), stringBuilder, null);
        Assert.assertEquals("3 % % / 4", stringBuilder.toString());
        Assert.assertEquals("3.0%/4.0%", formattedFraction.toString());

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse("3.0%/4.0%", parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(3, 4), parsedFraction);
    }

    @Test
    public void testFormatMethodWithNegativeWholeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuilder stringBuilder = new StringBuilder();
        Fraction formattedFraction = properFractionFormat.format(new Fraction(-3, 2), stringBuilder, null);
        Assert.assertEquals("-3 / 2", formattedFraction.toString());

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse("-3 / 2", parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(-3, 2), parsedFraction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormatException() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumeratorOrDenominator() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuilder stringBuilder = new StringBuilder();

        // test negative numerator
        Fraction formattedFraction = properFractionFormat.format(new Fraction(-3, 2), stringBuilder, null);
        Assert.assertEquals("-3 / 2", formattedFraction.toString());

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse("-3 / 2", parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(-3, 2), parsedFraction);

        // test negative denominator
        String improperFraction = "-3 / -2";
        formattedFraction = properFractionFormat.format(new Fraction(3, -2), stringBuilder, null);
        Assert.assertEquals("3 / -2", formattedFraction.toString());

        parsedFraction = properFractionFormat.parse(improperFraction, parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(3, -2), parsedFraction);

        // test invalid fraction with negative whole, numerator, and denominator
        String invalidFraction = "-3 -1/2";
        try {
            formattedFraction = properFractionFormat.format(new Fraction(-3, new Fraction(-1, 2)), stringBuilder, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            parsedFraction = properFractionFormat.parse(invalidFraction, parsePosition);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseImproperFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuilder stringBuilder = new StringBuilder();

        Fraction formattedFraction = properFractionFormat.format(new Fraction(5, 2), stringBuilder, null);
        Assert.assertEquals("5 / 2", formattedFraction.toString());

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse("5 / 2", parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(5, 2), parsedFraction);
    }

    @Test
    public void testParseMixedNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuilder stringBuilder = new StringBuilder();

        // test simple mixed number
        Fraction formattedFraction = properFractionFormat.format(new Fraction(2, 1, 3), stringBuilder, null);
        Assert.assertEquals("2 3 / 1", formattedFraction.toString());

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse("2 3 / 1", parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(2, 1, 3), parsedFraction);

        // test mixed number with negative whole number
        formattedFraction = properFractionFormat.format(new Fraction(-2, 1, 3), stringBuilder, null);
        Assert.assertEquals("-2 3 / 1", formattedFraction.toString());

        parsedFraction = properFractionFormat.parse("-2 3 / 1", parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(-2, 1, 3), parsedFraction);
    }

    @Test
    public void testParseBigDecimal() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuilder stringBuilder = new StringBuilder();

        // test parse BigDecimal as whole number
        BigDecimal bigDecimal = new BigDecimal("12345678901234567890");
        Fraction formattedFraction = properFractionFormat.format(new Fraction(bigDecimal), stringBuilder, null);
        Assert.assertEquals("12345678901234567890", formattedFraction.toString());

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse(formattedFraction.toString(), parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(bigDecimal), parsedFraction);

        // test parse BigDecimal as numerator
        BigDecimal bigNumerator = new BigDecimal("11111111111111111111");
        BigDecimal bigDenominator = new BigDecimal("22222222222222222222");
        Fraction fraction = new Fraction(bigNumerator, bigDenominator);
        formattedFraction = properFractionFormat.format(fraction, stringBuilder, null);
        Assert.assertEquals("11111111111111111111 / 22222222222222222222", formattedFraction.toString());

        parsedFraction = properFractionFormat.parse(formattedFraction.toString(), parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(fraction, parsedFraction);

        // test parse BigDecimal with negative sign in whole number
        BigDecimal bigNegativeWhole = new BigDecimal("-12345678901234567890");
        Fraction negativeWholeFraction = new Fraction(bigNegativeWhole);
        formattedFraction = properFractionFormat.format(negativeWholeFraction, stringBuilder, null);
        Assert.assertEquals("-12345678901234567890", formattedFraction.toString());

        parsedFraction = properFractionFormat.parse(formattedFraction.toString(), parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(negativeWholeFraction, parsedFraction);
    }

    @Test
    public void testParseBigInteger() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuilder stringBuilder = new StringBuilder();

        // test parse BigInteger as whole number
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        Fraction formattedFraction = properFractionFormat.format(new Fraction(bigInteger), stringBuilder, null);
        Assert.assertEquals("12345678901234567890", formattedFraction.toString());

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse(formattedFraction.toString(), parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(bigInteger), parsedFraction);

        // test parse BigInteger as numerator
        BigInteger bigNumerator = new BigInteger("11111111111111111111");
        BigInteger bigDenominator = new BigInteger("22222222222222222222");
        Fraction fraction = new Fraction(bigNumerator, bigDenominator);
        formattedFraction = properFractionFormat.format(fraction, stringBuilder, null);
        Assert.assertEquals("11111111111111111111 / 22222222222222222222", formattedFraction.toString());

        parsedFraction = properFractionFormat.parse(formattedFraction.toString(), parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(fraction, parsedFraction);

        // test parse BigInteger with negative sign in whole number
        BigInteger bigNegativeWhole = new BigInteger("-12345678901234567890");
        Fraction negativeWholeFraction = new Fraction(bigNegativeWhole);
        formattedFraction = properFractionFormat.format(negativeWholeFraction, stringBuilder, null);
        Assert.assertEquals("-12345678901234567890", formattedFraction.toString());

        parsedFraction = properFractionFormat.parse(formattedFraction.toString(), parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(negativeWholeFraction, parsedFraction);
    }

    @Test
    public void testParseWithLocale() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuilder stringBuilder = new StringBuilder();

        // Test locale with period as decimal separator
        Locale locale = new Locale("fr_FR");
        properFractionFormat.setLocale(locale);

        Fraction formattedFraction = properFractionFormat.format(new Fraction(1, 2), stringBuilder, null);
        Assert.assertEquals("1, 1/2", formattedFraction.toString());

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction parsedFraction = properFractionFormat.parse("1, 1/2", parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(1, 2), parsedFraction);

        // Test locale with comma as decimal separator
        locale = new Locale("de_DE");
        properFractionFormat.setLocale(locale);

        formattedFraction = properFractionFormat.format(new Fraction(1, 2), stringBuilder, null);
        Assert.assertEquals("1, 1/2", formattedFraction.toString());

        parsedFraction = properFractionFormat.parse("1, 1/2", parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(1, 2), parsedFraction);

        // Test locale with different thousand separator
        locale = new Locale("es_ES");
        properFractionFormat.setLocale(locale);

        formattedFraction = properFractionFormat.format(new Fraction(1000000, 7), stringBuilder, null);
        Assert.assertEquals("1.000.000 / 7", formattedFraction.toString());

        parsedFraction = properFractionFormat.parse("1.000.000 / 7", parsePosition);
        Assert.assertNotNull(parsedFraction);
        Assert.assertEquals(new Fraction(1000000, 7), parsedFraction);
    }
}