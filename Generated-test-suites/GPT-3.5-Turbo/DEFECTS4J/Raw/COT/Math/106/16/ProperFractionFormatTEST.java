import org.junit.Assert;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 4);
        String expected = "1 1 / 4";
        String result = format.format(fraction);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 1 / 4";
        Fraction expected = new Fraction(5, 4);
        Fraction result = format.parse(source);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseInvalidExpression() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 -1 / 4";
        Fraction result = format.parse(source);
        Assert.assertNull(result);
    }

    @Test
    public void testParseInvalidNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 a / 4";
        Fraction result = format.parse(source);
        Assert.assertNull(result);
    }

    @Test
    public void testParseNoNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 / 4";
        Fraction expected = new Fraction(1, 4);
        Fraction result = format.parse(source);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseNoDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 1";
        Fraction result = format.parse(source);
        Assert.assertNull(result);
    }
}