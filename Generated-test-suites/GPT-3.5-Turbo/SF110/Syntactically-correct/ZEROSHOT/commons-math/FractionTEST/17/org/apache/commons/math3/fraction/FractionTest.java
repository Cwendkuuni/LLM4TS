import org.junit.Assert;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructor() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(3, fraction.getNumerator());
        Assert.assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testAbs() {
        Fraction fraction1 = new Fraction(-3, 4);
        Fraction fraction2 = new Fraction(3, 4);
        Assert.assertEquals(fraction2, fraction1.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(4, 5);
        Assert.assertEquals(1, fraction1.compareTo(fraction2));
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(6, 8);
        Assert.assertEquals(true, fraction1.equals(fraction2));
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(0.75, fraction.doubleValue(), 0.001);
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(0.75f, fraction.floatValue(), 0.001);
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(3, fraction.getNumerator());
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(920, fraction.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(0, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(0L, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(-3, 4);
        Assert.assertEquals(fraction2, fraction1.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(4, 3);
        Assert.assertEquals(fraction2, fraction1.reciprocal());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction sum = fraction1.add(fraction2);
        Assert.assertEquals(new Fraction(5, 4), sum);
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction sum = fraction.add(2);
        Assert.assertEquals(new Fraction(11, 4), sum);
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction difference = fraction1.subtract(fraction2);
        Assert.assertEquals(new Fraction(1, 4), difference);
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction difference = fraction.subtract(2);
        Assert.assertEquals(new Fraction(-5, 4), difference);
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction product = fraction1.multiply(fraction2);
        Assert.assertEquals(new Fraction(3, 8), product);
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction product = fraction.multiply(2);
        Assert.assertEquals(new Fraction(3, 2), product);
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction quotient = fraction1.divide(fraction2);
        Assert.assertEquals(new Fraction(3, 2), quotient);
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction quotient = fraction.divide(2);
        Assert.assertEquals(new Fraction(3, 8), quotient);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(75.0, fraction.percentageValue(), 0.001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroDenominatorInConstructor() {
        Fraction fraction = new Fraction(3, 0);
    }

    @Test
    public void testZeroDenominatorInToString() {
        Fraction fraction = new Fraction(3, 0);
        Assert.assertEquals("0", fraction.toString());
    }
}