import org.junit.Test;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;
import java.text.NumberFormat;
import java.util.Locale;
import static org.junit.Assert.assertEquals;

public class ProperFractionFormatTest {

    private ProperFractionFormat formatter = new ProperFractionFormat();
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        String result = formatter.format(fraction);
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse() {
        String source = "2 / 5";
        Fraction expected = new Fraction(2, 5);
        Fraction result = formatter.parse(source);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatAndParse() {
        Fraction fraction = new Fraction(5, 7);
        String formatted = formatter.format(fraction);
        Fraction parsed = formatter.parse(formatted);
        assertEquals(fraction, parsed);
    }
    
    @Test
    public void testFormatNegativeFraction() {
        Fraction fraction = new Fraction(-1, 2);
        String expected = "-1 / 2";
        String result = formatter.format(fraction);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatWholeNumber() {
        Fraction fraction = new Fraction(3);
        String expected = "3";
        String result = formatter.format(fraction);
        assertEquals(expected, result);
    }
    
    @Test
    public void testParseWholeNumber() {
        String source = "5";
        Fraction expected = new Fraction(5);
        Fraction result = formatter.parse(source);
        assertEquals(expected, result);
    }
    
    @Test
    public void testParseNegativeWholeNumber() {
        String source = "-7";
        Fraction expected = new Fraction(-7);
        Fraction result = formatter.parse(source);
        assertEquals(expected, result);
    }
    
    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() {
        String source = "3 - 1 / 2";
        Fraction result = formatter.parse(source);
    }
    
    @Test(expected = ParseException.class)
    public void testParseInvalidFraction() {
        String source = "-3 - 1 / 2";
        Fraction result = formatter.parse(source);
    }
    
    @Test
    public void testFormatWithCustomFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance(Locale.US);
        NumberFormat numeratorFormat = NumberFormat.getInstance(Locale.US);
        NumberFormat denominatorFormat = NumberFormat.getInstance(Locale.US);
        ProperFractionFormat customFormatter = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = new Fraction(2, 3);
        wholeFormat.setMinimumIntegerDigits(2);
        numeratorFormat.setMaximumFractionDigits(3);
        numeratorFormat.setMinimumFractionDigits(2);
        denominatorFormat.setMinimumIntegerDigits(3);
        String expected = "02 000.333 / 001.000";
        String result = customFormatter.format(fraction);
        assertEquals(expected, result);
    }
}