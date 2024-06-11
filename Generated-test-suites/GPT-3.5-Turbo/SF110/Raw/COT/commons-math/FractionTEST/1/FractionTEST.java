import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-4, 5);
        assertEquals(new Fraction(4, 5), fraction.abs());

        fraction = new Fraction(4, 5);
        assertEquals(new Fraction(4, 5), fraction.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(2, 3);

        assertTrue(fraction1.compareTo(fraction2) > 0);
        assertTrue(fraction2.compareTo(fraction3) < 0);
        assertTrue(fraction1.compareTo(fraction3) > 0);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75, fraction.doubleValue(), 1e-6);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(6, 8);
        Fraction fraction3 = new Fraction(1, 2);

        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75f, fraction.floatValue(), 1e-6);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(6, 8);

        assertEquals(fraction1.hashCode(), fraction2.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(new Fraction(-3, 4), fraction.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(new Fraction(4, 3), fraction.reciprocal());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 4);

        assertEquals(new Fraction(3, 4), fraction1.add(fraction2));
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(1, 2);
        int i = 3;

        assertEquals(new Fraction(7, 2), fraction.add(i));
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 4);

        assertEquals(new Fraction(1, 2), fraction1.subtract(fraction2));
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(3, 4);
        int i = 1;

        assertEquals(new Fraction(-1, 4), fraction.subtract(i));
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 4);

        assertEquals(new Fraction(3, 16), fraction1.multiply(fraction2));
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(3, 4);
        int i = 2;

        assertEquals(new Fraction(3, 2), fraction.multiply(i));
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 4);

        assertEquals(new Fraction(3, 1), fraction1.divide(fraction2));
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(3, 4);
        int i = 2;

        assertEquals(new Fraction(3, 8), fraction.divide(i));
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(75.0, fraction.percentageValue(), 1e-6);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction fraction = Fraction.getReducedFraction(6, 8);
        assertEquals(new Fraction(3, 4), fraction);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals("3 / 4", fraction.toString());
    }
}

