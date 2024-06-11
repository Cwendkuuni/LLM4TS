import static org.junit.Assert.*;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionValue() {
        Fraction fraction = new Fraction(5, 3);
        assertEquals(1.6666666666666667, fraction.doubleValue(), 0.0001);
    }

    @Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(5, 3);
        Fraction fraction2 = new Fraction(10, 6);
        assertTrue(fraction1.equals(fraction2));
    }

    @Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(5, 3);
        Fraction negatedFraction = fraction.negate();
        assertEquals(-5, negatedFraction.getNumerator());
        assertEquals(3, negatedFraction.getDenominator());
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(5, 3);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(7, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(5, 3);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(10, result.getNumerator());
        assertEquals(9, result.getDenominator());
    }

    @Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(5, 3);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroDenominatorException() {
        Fraction fraction = new Fraction(5, 0);
    }

    @Test
    public void testFieldInstance() {
        Fraction fraction = new Fraction(5, 3);
        assertEquals(FractionField.getInstance(), fraction.getField());
    }

}