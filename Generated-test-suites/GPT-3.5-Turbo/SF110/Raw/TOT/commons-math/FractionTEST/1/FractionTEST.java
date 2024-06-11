import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {
    private Fraction fraction1;
    private Fraction fraction2;

    @Before
    public void setUp() {
        fraction1 = new Fraction(1, 2);
        fraction2 = new Fraction(3, 4);
    }

    @Test
    public void testConstructor() {
        assertEquals(1, fraction1.getNumerator());
        assertEquals(2, fraction1.getDenominator());
    }

    @Test
    public void testEquals() {
        Fraction fraction3 = new Fraction(2, 4);
        assertEquals(fraction1, fraction3);
    }

    @Test
    public void testAdd() {
        Fraction result = fraction1.add(fraction2);
        assertEquals(5, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction result = fraction2.subtract(fraction1);
        assertEquals(1, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(3, result.getNumerator());
        assertEquals(8, result.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction result = fraction1.divide(fraction2);
        assertEquals(2, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZero() {
        Fraction zero = new Fraction(0);
        Fraction result = fraction1.divide(zero);
    }

    @Test
    public void testReciprocal() {
        Fraction reciprocal = fraction1.reciprocal();
        assertEquals(2, reciprocal.getNumerator());
        assertEquals(1, reciprocal.getDenominator());
    }

    @Test
    public void testNegate() {
        Fraction negated = fraction1.negate();
        assertEquals(-1, negated.getNumerator());
        assertEquals(2, negated.getDenominator());
    }

    @Test
    public void testAbs_Positive() {
        Fraction positive = new Fraction(2, 3);
        Fraction abs = positive.abs();
        assertEquals(2, abs.getNumerator());
        assertEquals(3, abs.getDenominator());
    }

    @Test
    public void testAbs_Negative() {
        Fraction negative = new Fraction(-2, 3);
        Fraction abs = negative.abs();
        assertEquals(2, abs.getNumerator());
        assertEquals(3, abs.getDenominator());
    }

    @Test
    public void testAbs_Zero() {
        Fraction zero = new Fraction(0);
        Fraction abs = zero.abs();
        assertEquals(0, abs.getNumerator());
        assertEquals(1, abs.getDenominator());
    }

    @Test
    public void testCompareTo_LessThan() {
        assertTrue(fraction1.compareTo(fraction2) < 0);
    }

    @Test
    public void testCompareTo_GreaterThan() {
        assertTrue(fraction2.compareTo(fraction1) > 0);
    }

    @Test
    public void testCompareTo_Equal() {
        Fraction fraction3 = new Fraction(1, 2);
        assertTrue(fraction1.compareTo(fraction3) == 0);
    }

    @Test
    public void testDoubleValue() {
        assertEquals(0.5, fraction1.doubleValue(), 0.0001);
    }

    @Test
    public void testFloatValue() {
        assertEquals(0.5f, fraction1.floatValue(), 0.0001);
    }

    @Test
    public void testIntValue() {
        assertEquals(0, fraction1.intValue());
    }

    @Test
    public void testLongValue() {
        assertEquals(0L, fraction1.longValue());
    }

    @Test
    public void testPercentageValue() {
        assertEquals(50.0, fraction1.percentageValue(), 0.0001);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction fraction3 = new Fraction(2, 4);
        Fraction reduced = Fraction.getReducedFraction(2, 4);
        assertEquals(fraction3, reduced);
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroDenominator_ThrowsException() {
        Fraction zeroDenominator = new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testOverflowInFraction_ThrowsException() {
        Fraction overflow = new Fraction(Integer.MIN_VALUE, 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testOverflowInMultiply_ThrowsException() {
        Fraction fraction3 = new Fraction(536870912);
        Fraction result = fraction3.multiply(fraction3);
    }

    @Test
    public void testToString() {
        String expected = "1 / 2";
        assertEquals(expected, fraction1.toString());
    }
}