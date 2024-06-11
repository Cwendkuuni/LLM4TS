import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FractionTest {

    private Fraction fraction;

    @Before
    public void setUp() {
        fraction = new Fraction(2, 3);
    }

    @Test
    public void testAbs() {
        Fraction abs = fraction.abs();
        assertEquals(2, abs.getNumerator());
        assertEquals(3, abs.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction smaller = new Fraction(1, 2);
        Fraction larger = new Fraction(3, 4);

        assertTrue(fraction.compareTo(smaller) > 0);
        assertTrue(fraction.compareTo(larger) < 0);
        assertEquals(0, fraction.compareTo(new Fraction(4, 6)));
    }

    @Test
    public void testDoubleValue() {
        assertEquals(0.6666666666666666, fraction.doubleValue(), 0.00001);
    }

    @Test
    public void testEquals() {
        Fraction equal = new Fraction(2, 3);
        Fraction notEqual = new Fraction(1, 2);

        assertTrue(fraction.equals(equal));
        assertFalse(fraction.equals(notEqual));
    }

    @Test
    public void testFloatValue() {
        assertEquals(0.6666667, fraction.floatValue(), 0.0000001);
    }

    @Test
    public void testGetDenominator() {
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        assertEquals(2, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction equal = new Fraction(2, 3);

        assertEquals(fraction.hashCode(), equal.hashCode());
    }

    @Test
    public void testIntValue() {
        assertEquals(0, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        assertEquals(0, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction negated = fraction.negate();
        assertEquals(-2, negated.getNumerator());
        assertEquals(3, negated.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction reciprocal = fraction.reciprocal();
        assertEquals(3, reciprocal.getNumerator());
        assertEquals(2, reciprocal.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fractionToAdd = new Fraction(1, 2);
        Fraction result = fraction.add(fractionToAdd);
        assertEquals(7, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testAddInt() {
        Fraction result = fraction.add(1);
        assertEquals(5, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fractionToSubtract = new Fraction(1, 2);
        Fraction result = fraction.subtract(fractionToSubtract);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testSubtractInt() {
        Fraction result = fraction.subtract(1);
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fractionToMultiply = new Fraction(3, 4);
        Fraction result = fraction.multiply(fractionToMultiply);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testMultiplyInt() {
        Fraction result = fraction.multiply(2);
        assertEquals(4, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fractionToDivide = new Fraction(1, 2);
        Fraction result = fraction.divide(fractionToDivide);
        assertEquals(4, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testDivideInt() {
        Fraction result = fraction.divide(2);
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        assertEquals(66.66666666666666, fraction.percentageValue(), 0.00001);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction fractionToReduce = new Fraction(12, 16);
        Fraction reduced = Fraction.getReducedFraction(fractionToReduce.getNumerator(),
                fractionToReduce.getDenominator());
        assertEquals(3, reduced.getNumerator());
        assertEquals(4, reduced.getDenominator());
    }

    @Test
    public void testToString() {
        assertEquals("2 / 3", fraction.toString());
    }

    @Test
    public void testGetField() {
        assertEquals(FractionField.getInstance(), fraction.getField());
    }
}