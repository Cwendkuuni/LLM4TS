import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {

    private static final NumberFormat DEFAULT_WHOLE_FORMAT = NumberFormat.getNumberInstance(Locale.ENGLISH);
    private static final NumberFormat DEFAULT_NUMERATOR_FORMAT = NumberFormat.getNumberInstance(Locale.ENGLISH);
    private static final NumberFormat DEFAULT_DENOMINATOR_FORMAT = NumberFormat.getNumberInstance(Locale.ENGLISH);

    @Test
    public void testConstructorWithDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        String result = format.format(fraction, toAppendTo, pos);
        assertEquals("1 / 2", result.toString());

        ParsePosition pp = new ParsePosition(0);
        Fraction parsedFraction = format.parse("1 / 2", pp);
        assertEquals(fraction, parsedFraction);
    }

    @Test
    public void testConstructorWithCustomFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getPercentInstance();
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance();

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction1 = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        String result = format.format(fraction1, toAppendTo, pos);
        assertEquals("-100.0% / 200.0%", result.toString());

        ParsePosition pp = new ParsePosition(0);
        Fraction parsedFraction = format.parse("100.0% / 200.0%", pp);
        assertEquals(fraction1, parsedFraction);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test format method with positive whole number
        Fraction fraction1 = new Fraction(2, 1);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction1, toAppendTo, pos);
        assertEquals("2", toAppendTo.toString());

        // Test format method with negative whole number
        Fraction fraction2 = new Fraction(-2, 1);
        StringBuffer toAppendTo2 = new StringBuffer();
        FieldPosition pos2 = new FieldPosition(0, 0);

        format.format(fraction2, toAppendTo2, pos2);
        assertEquals("-2", toAppendTo2.toString());

        // Test format method with no whole number
        Fraction fraction3 = new Fraction(0, 1);
        StringBuffer toAppendTo3 = new StringBuffer();
        FieldPosition pos3 = new FieldPosition(0, 0);

        format.format(fraction3, toAppendTo3, pos3);
        assertEquals("0 / 1", toAppendTo3.toString());

        // Test format method with numerator and denominator greater than 0
        Fraction fraction4 = new Fraction(3, 4);
        StringBuffer toAppendTo4 = new StringBuffer();
        FieldPosition pos4 = new FieldPosition(0, 0);

        format.format(fraction4, toAppendTo4, pos4);
        assertEquals("3 / 4", toAppendTo4.toString());

        // Test format method with numerator and denominator less than 0
        Fraction fraction5 = new Fraction(-3, -4);
        StringBuffer toAppendTo5 = new StringBuffer();
        FieldPosition pos5 = new FieldPosition(0, 0);

        format.format(fraction5, toAppendTo5, pos5);
        assertEquals("3 / 4", toAppendTo5.toString()); // The result is the same as for positive numerator and denominator, because the sign is in the whole number part
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test parse method with positive whole number, positive numerator and positive denominator
        String testString1 = "2";
        ParsePosition pp1 = new ParsePosition(0);
        Fraction expectedFraction1 = new Fraction(2, 1);

        Fraction actualFraction1 = format.parse(testString1, pp1);
        assertEquals(expectedFraction1, actualFraction1);

        // Test parse method with negative whole number, positive numerator and positive denominator
        String testString2 = "-2";
        ParsePosition pp2 = new ParsePosition(0);
        Fraction expectedFraction2 = new Fraction(-2, 1);

        Fraction actualFraction2 = format.parse(testString2, pp2);
        assertEquals(expectedFraction2, actualFraction2);

        // Test parse method with no whole number, positive numerator and positive denominator
        String testString3 = "0 / 1";
        ParsePosition pp3 = new ParsePosition(0);
        Fraction expectedFraction3 = new Fraction(0, 1);

        Fraction actualFraction3 = format.parse(testString3, pp3);
        assertEquals(expectedFraction3, actualFraction3);

        // Test parse method with positive whole number, positive numerator and denominator greater than 1
        String testString4 = "3 / 4";
        ParsePosition pp4 = new ParsePosition(0);
        Fraction expectedFraction4 = new Fraction(3, 4);

        Fraction actualFraction4 = format.parse(testString4, pp4);
        assertEquals(expectedFraction4, actualFraction4);

        // Test parse method with negative whole number, positive numerator and negative denominator
        String testString5 = "-3 / -4";
        ParsePosition pp5 = new ParsePosition(0);
        Fraction expectedFraction5 = new Fraction(-3, -4);

        Fraction actualFraction5 = format.parse(testString5, pp5);
        assertEquals(expectedFraction5, actualFraction5);

        // Test parse method with negative whole number, positive numerator and positive denominator
        String testString6 = "-3 / 4";
        ParsePosition pp6 = new ParsePosition(0);
        Fraction expectedFraction6 = new Fraction(-3, 4);

        Fraction actualFraction6 = format.parse(testString6, pp6);
        assertEquals(expectedFraction6, actualFraction6);

        // Test parse method with invalid input
        String testString7 = "3 -1/2";
        ParsePosition pp7 = new ParsePosition(0);

        Fraction actualFraction7 = format.parse(testString7, pp7);
        assertNull(actualFraction7);
        assertEquals(1, pp7.getIndex()); // The error index should be set to the invalid character index
    }

    @Test
    public void testParseWithBigDecimalAndBigInteger() {
        ProperFractionFormat format = new ProperFractionFormat();

        BigDecimal whole = new BigDecimal("2");
        BigInteger numerator = new BigInteger("3");
        BigInteger denominator = new BigInteger("4");

        String testString = whole.toString() + " " + numerator.toString() + " / " + denominator.toString();
        ParsePosition pp = new ParsePosition(0);
        Fraction expectedFraction = new Fraction(whole.intValueExact(), denominator.intValueExact());

        Fraction actualFraction = format.parse(testString, pp);
        assertEquals(expectedFraction, actualFraction);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat newWholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);

        format.setWholeFormat(newWholeFormat);
        FormatFormatter formatter = new FormatFormatter();
        assertEquals(newWholeFormat, format.getWholeFormat());
        String testString = "2";

        Fraction expectedFraction = new Fraction(2, 1);
        ParsePosition pp = new ParsePosition(0);

        Fraction actualFraction = format.parse(testString, pp);
        assertEquals(expectedFraction, actualFraction);

        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(expectedFraction, toAppendTo, pos);
        assertEquals(newWholeFormat.format(2), toAppendTo.toString());
    }

    private static void parseAndIgnoreWhitespace(String source, ParsePosition pos) {
        while (Character.isWhitespace(parseNextCharacter(source, pos))) {
            pos.setIndex(pos.getIndex() + 1);
        }
    }

    private static int parseNextCharacter(String source, ParsePosition pos) {
        int index = pos.getIndex();
        int c = (index < source.length()) ? source.charAt(index) : 0;
        pos.setIndex(index + 1);

        return c;
    }
}