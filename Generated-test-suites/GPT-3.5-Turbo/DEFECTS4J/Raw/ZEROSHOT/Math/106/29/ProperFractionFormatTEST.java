import org.apache.commons.math.fraction.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1/2";
        String result = format.format(fraction);
        assertEquals(expected, result);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "1 1/2";
        Fraction expected = new Fraction(3, 2);
        Fraction result = format.parse(input);
        assertEquals(expected, result);
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat expected = NumberFormat.getInstance();
        NumberFormat result = format.getWholeFormat();
        assertEquals(expected, result);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(0);
        format.setMinimumFractionDigits(0);
        format.setMaximumIntegerDigits(Integer.MAX_VALUE);
        format.setMinimumIntegerDigits(1);
        format.setParseIntegerOnly(true);
        format.setParseBigDecimal(false);

        format.setWholeFormat(format);
        NumberFormat expected = format;
        NumberFormat result = format.getWholeFormat();
        assertEquals(expected, result);
    }

    @Test
    public void testWithCustomFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        wholeFormat.setGroupingUsed(false);
        wholeFormat.setMaximumFractionDigits(0);
        wholeFormat.setMinimumFractionDigits(0);
        wholeFormat.setMaximumIntegerDigits(Integer.MAX_VALUE);
        wholeFormat.setMinimumIntegerDigits(1);
        wholeFormat.setParseIntegerOnly(true);
        wholeFormat.setParseBigDecimal(false);

        NumberFormat numeratorFormat = NumberFormat.getInstance();
        numeratorFormat.setGroupingUsed(false);
        numeratorFormat.setMaximumFractionDigits(0);
        numeratorFormat.setMinimumFractionDigits(0);
        numeratorFormat.setMaximumIntegerDigits(Integer.MAX_VALUE);
        numeratorFormat.setMinimumIntegerDigits(1);
        numeratorFormat.setParseIntegerOnly(true);
        numeratorFormat.setParseBigDecimal(false);

        NumberFormat denominatorFormat = NumberFormat.getInstance();
        denominatorFormat.setGroupingUsed(false);
        denominatorFormat.setMaximumFractionDigits(0);
        denominatorFormat.setMinimumFractionDigits(0);
        denominatorFormat.setMaximumIntegerDigits(Integer.MAX_VALUE);
        denominatorFormat.setMinimumIntegerDigits(1);
        denominatorFormat.setParseIntegerOnly(true);
        denominatorFormat.setParseBigDecimal(false);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1/2";
        String result = format.format(fraction);
        assertEquals(expected, result);

        String input = "1 1/2";
        Fraction expectedFraction = new Fraction(3, 2);
        Fraction resultFraction = format.parse(input);
        assertEquals(expectedFraction, resultFraction);

        NumberFormat expectedWholeFormat = wholeFormat;
        NumberFormat resultWholeFormat = format.getWholeFormat();
        assertEquals(expectedWholeFormat, resultWholeFormat);
    }

}