import org.apache.commons.math.fraction.Fraction;
import org.junit.Assert;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat_PositiveFraction_ReturnsProperFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(7, 2);
        String expected = "3 1/2";
        String actual = format.format(fraction);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormat_ZeroFraction_ReturnsProperFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0);
        String expected = "0";
        String actual = format.format(fraction);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormat_NegativeFraction_ReturnsProperFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-5, 2);
        String expected = "-2 1/2";
        String actual = format.format(fraction);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse_ValidProperFraction_ReturnsFractionObject() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 1/2";
        Fraction expected = new Fraction(7, 2);
        Fraction actual = format.parse(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse_ValidWholeNumber_ReturnsFractionObject() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "7";
        Fraction expected = new Fraction(7);
        Fraction actual = format.parse(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse_ValidFractionOnly_ReturnsFractionObject() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "1/2";
        Fraction expected = new Fraction(1, 2);
        Fraction actual = format.parse(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse_NullInput_ReturnsNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = null;
        Fraction actual = format.parse(input);
        Assert.assertNull(actual);
    }

    @Test
    public void testParse_InvalidFormat_ThrowsParseException() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "abc";
        try {
            format.parse(input);
            Assert.fail("Expected ParseException was not thrown.");
        } catch (ParseException e) {
            // Expected exception was thrown
        }
    }
}