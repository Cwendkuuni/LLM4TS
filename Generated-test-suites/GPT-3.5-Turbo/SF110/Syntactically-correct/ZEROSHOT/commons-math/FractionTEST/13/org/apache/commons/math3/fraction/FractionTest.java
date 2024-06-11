import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import java.math.*;
import org.apache.commons.math3.*;

public class FractionTest {

    private Fraction fraction;

    @Before
    public void setUp() {
        fraction = new Fraction(1, 2);
    }

    @Test
    public void testAbs() {
        Fraction absFraction = fraction.abs();
        Assert.assertEquals(1, absFraction.getNumerator());
        Assert.assertEquals(2, absFraction.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction2 = new Fraction(1, 3);
        int result = fraction.compareTo(fraction2);
        Assert.assertTrue(result > 0);

        fraction2 = new Fraction(1, 2);
        result = fraction.compareTo(fraction2);
        Assert.assertEquals(0, result);

        fraction2 = new Fraction(2, 3);
        result = fraction.compareTo(fraction2);
        Assert.assertTrue(result < 0);
    }

    @Test
    public void testDoubleValue() {
        double result = fraction.doubleValue();
        Assert.assertEquals(0.5, result, 0);
    }

    @Test
    public void testEquals() {
        Fraction fraction2 = new Fraction(1, 2);
        Assert.assertTrue(fraction.equals(fraction2));

        fraction2 = new Fraction(2, 4);
        Assert.assertTrue(fraction.equals(fraction2));

        fraction2 = new Fraction(1, 3);
        Assert.assertFalse(fraction.equals(fraction2));
    }

    @Test
    public void testFloatValue() {
        float result = fraction.floatValue();
        Assert.assertEquals(0.5, result, 0);
    }

    @Test
    public void testGetDenominator() {
        int result = fraction.getDenominator();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testGetNumerator() {
        int result = fraction.getNumerator();
        Assert.assertEquals(1, result);
    }

    @Test
    public void testHashCode() {
        int result = fraction.hashCode();
        Assert.assertEquals(31, result);
    }

    @Test
    public void testIntValue() {
        int result = fraction.intValue();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testLongValue() {
        long result = fraction.longValue();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testNegate() {
        Fraction negateFraction = fraction.negate();
        Assert.assertEquals(-1, negateFraction.getNumerator());
        Assert.assertEquals(2, negateFraction.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction reciprocalFraction = fraction.reciprocal();
        Assert.assertEquals(2, reciprocalFraction.getNumerator());
        Assert.assertEquals(1, reciprocalFraction.getDenominator());
    }

    @Test
    public void testAddFraction() {
        Fraction addFraction = fraction.add(new Fraction(1, 4));
        Assert.assertEquals(3, addFraction.getNumerator());
        Assert.assertEquals(4, addFraction.getDenominator());
    }

    @Test
    public void testAddInt() {
        Fraction addInt = fraction.add(1);
        Assert.assertEquals(3, addInt.getNumerator());
        Assert.assertEquals(2, addInt.getDenominator());
    }

    @Test
    public void testSubtractFraction() {
        Fraction subtractFraction = fraction.subtract(new Fraction(1, 4));
        Assert.assertEquals(1, subtractFraction.getNumerator());
        Assert.assertEquals(4, subtractFraction.getDenominator());
    }

    @Test
    public void testSubtractInt() {
        Fraction subtractInt = fraction.subtract(1);
        Assert.assertEquals(-1, subtractInt.getNumerator());
        Assert.assertEquals(2, subtractInt.getDenominator());
    }

    @Test
    public void testMultiplyFraction() {
        Fraction multiplyFraction = fraction.multiply(new Fraction(1, 4));
        Assert.assertEquals(1, multiplyFraction.getNumerator());
        Assert.assertEquals(8, multiplyFraction.getDenominator());
    }

    @Test
    public void testMultiplyInt() {
        Fraction multiplyInt = fraction.multiply(2);
        Assert.assertEquals(1, multiplyInt.getNumerator());
        Assert.assertEquals(1, multiplyInt.getDenominator());
    }

    @Test
    public void testDivideFraction() {
        Fraction divideFraction = fraction.divide(new Fraction(1, 4));
        Assert.assertEquals(2, divideFraction.getNumerator());
        Assert.assertEquals(1, divideFraction.getDenominator());
    }

    @Test
    public void testDivideInt() {
        Fraction divideInt = fraction.divide(2);
        Assert.assertEquals(1, divideInt.getNumerator());
        Assert.assertEquals(4, divideInt.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        double result = fraction.percentageValue();
        Assert.assertEquals(50, result, 0);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction reducedFraction = Fraction.getReducedFraction(2, 4);
        Assert.assertEquals(1, reducedFraction.getNumerator());
        Assert.assertEquals(2, reducedFraction.getDenominator());
    }

    @Test
    public void testToString() {
        String result = fraction.toString();
        Assert.assertEquals("1 / 2", result);
    }

    @Test
    public void testGetField() {
        FractionField field = fraction.getField();
        Assert.assertNotNull(field);
    }
}