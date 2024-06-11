import org.junit.Test;
import org.junit.Assert;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        Assert.assertNotNull(format.getWholeFormat());
    }

    @Test
    public void testConstructorWithNumberFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(numberFormat);
        Assert.assertEquals(numberFormat, format.getWholeFormat());
    }

    @Test
    public void testConstructorWithAllFormats() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance();

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Assert.assertEquals(wholeFormat, format.getWholeFormat());
        Assert.assertEquals(numeratorFormat, format.getNumeratorFormat());
        Assert.assertEquals(denominatorFormat, format.getDenominatorFormat());
    }

    @Test
    public void testFormatWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, 1);
        Assert.assertEquals("2", format.format(fraction));
    }

    @Test
    public void testFormatWithoutWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        Assert.assertEquals("1 / 2", format.format(fraction));
    }

    @Test
    public void testFormatWithNegativeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-1, 2);
        Assert.assertEquals("-1 / 2", format.format(fraction));
    }

    @Test
    public void testParseWithValidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("2 / 3");
        Assert.assertEquals(2, fraction.getNumerator());
        Assert.assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testParseWithInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("2/3");
        Assert.assertNull(fraction);
    }

    @Test
    public void testParseWithNegativeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("-2 / 3");
        Assert.assertEquals(-2, fraction.getNumerator());
        Assert.assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testParseWithInvalidWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("2.5 / 3");
        Assert.assertNull(fraction);
    }

    @Test
    public void testParseWithInvalidNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("2 / abc");
        Assert.assertNull(fraction);
    }

    @Test
    public void testParseWithInvalidDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("2 / 0");
        Assert.assertNull(fraction);
    }
}