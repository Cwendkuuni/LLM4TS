import org.junit.Assert;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1/2";
        String actual = format.format(fraction);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "1 1/2";
        Fraction expected = new Fraction(3, 2);
        Fraction actual = format.parse(fractionString);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseInvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String fractionString = "1/2/3";
        Fraction actual = format.parse(fractionString);
        Assert.assertNull(actual);
    }
}