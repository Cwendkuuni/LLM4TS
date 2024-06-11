import org.junit.Assert;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-5, 3);
        Fraction result = fraction.abs();
        Assert.assertEquals(new Fraction(5, 3), result);
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        int result = fraction1.compareTo(fraction2);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        double result = fraction.doubleValue();
        Assert.assertEquals(0.75, result, 0.01);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(6, 8);
        boolean result = fraction1.equals(fraction2);
        Assert.assertTrue(result);
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(3, 4);
        float result = fraction.floatValue();
        Assert.assertEquals(0.75, result, 0.01);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(3, 4);
        int result = fraction.getDenominator();
        Assert.assertEquals(4, result);
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(3, 4);
        int result = fraction.getNumerator();
        Assert.assertEquals(3, result);
    }

    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(3, 4);
        int result = fraction.hashCode();
        Assert.assertEquals(3483, result);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);
        int result = fraction.intValue();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 4);
        long result = fraction.longValue();
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.negate();
        Assert.assertEquals(new Fraction(-3, 4), result);
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.reciprocal();
        Assert.assertEquals(new Fraction(4, 3), result);
    }

    @Test
    public void testAddFraction() {
        Fraction fraction1 = new Fraction(1, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.add(fraction2);
        Assert.assertEquals(new Fraction(3, 4), result);
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.add(2);
        Assert.assertEquals(new Fraction(11, 4), result);
    }

    @Test
    public void testSubtractFraction() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.subtract(fraction2);
        Assert.assertEquals(new Fraction(1, 4), result);
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.subtract(2);
        Assert.assertEquals(new Fraction(-5, 4), result);
    }

    @Test
    public void testMultiplyFraction() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.multiply(fraction2);
        Assert.assertEquals(new Fraction(3, 8), result);
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.multiply(2);
        Assert.assertEquals(new Fraction(6, 4), result);
    }

    @Test
    public void testDivideFraction() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.divide(fraction2);
        Assert.assertEquals(new Fraction(3, 2), result);
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.divide(2);
        Assert.assertEquals(new Fraction(3, 8), result);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        double result = fraction.percentageValue();
        Assert.assertEquals(75.0, result, 0.01);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction fraction = Fraction.getReducedFraction(6, 8);
        Assert.assertEquals(new Fraction(3, 4), fraction);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(3, 4);
        String result = fraction.toString();
        Assert.assertEquals("3 / 4", result);
    }
}