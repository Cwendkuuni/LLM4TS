
import org.junit.Test;
import org.junit.Assert;

public class ProperFractionFormatTest {

    @Test
    public void format_withWholeNumber_zeroFraction_shouldFormatCorrectly() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, 1);

        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), null);

        Assert.assertEquals("2 / 1", result.toString());
    }

    @Test
    public void format_withWholeNumber_nonZeroFraction_shouldFormatCorrectly() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(4, 3);

        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), null);

        Assert.assertEquals("1 1 / 3", result.toString());
    }

    @Test
    public void format_withNegativeFraction_shouldFormatCorrectly() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(-1, 3);

        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), null);

        Assert.assertEquals("-1 / 3", result.toString());
    }

    @Test
    public void format_withWholeNumberAndNegativeFraction_shouldFormatCorrectly() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(-5, 3);

        StringBuffer result = properFractionFormat.format(fraction, new StringBuffer(), null);

        Assert.assertEquals("-1 2 / 3", result.toString());
    }

    @Test
    public void parse_withValidFormat_shouldReturnCorrectFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "-1 2 / 3";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionString, pos);

        Assert.assertEquals(-5, result.getNumerator());
        Assert.assertEquals(3, result.getDenominator());
    }

    @Test
    public void parse_withInvalidFormat_shouldReturnNull() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "invalid format";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionString, pos);

        Assert.assertNull(result);
        Assert.assertEquals(0, pos.getErrorIndex());
    }

    @Test
    public void parse_withNegativeNumerator_shouldReturnNull() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "-1 / 3";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionString, pos);

        Assert.assertNull(result);
        Assert.assertEquals(0, pos.getErrorIndex());
    }

    @Test
    public void parse_withNegativeDenominator_shouldReturnNull() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "1 / -3";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionString, pos);

        Assert.assertNull(result);
        Assert.assertEquals(0, pos.getErrorIndex());
    }

    @Test
    public void parse_withMissingNumerator_shouldReturnNull() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "1 /";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionString, pos);

        Assert.assertNull(result);
        Assert.assertEquals(2, pos.getErrorIndex());
    }

    @Test
    public void parse_withMissingDenominator_shouldReturnNull() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String fractionString = "1";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = properFractionFormat.parse(fractionString, pos);

        Assert.assertNull(result);
        Assert.assertEquals(1, pos.getErrorIndex());
    }
}

