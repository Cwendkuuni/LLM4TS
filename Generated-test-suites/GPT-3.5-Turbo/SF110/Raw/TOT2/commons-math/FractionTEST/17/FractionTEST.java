import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructor() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction absFraction = fraction.abs();
        assertEquals(3, absFraction.getNumerator());
        assertEquals(4, absFraction.getDenominator());
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1.5, fraction.doubleValue(), 0.001);
    }

    // Test for edge case where denominator is 0
    @Test(expected = MathArithmeticException.class)
    public void testFractionZeroDenominator() {
        Fraction fraction = new Fraction(1, 0);
    }

    // Test for error handling when denominator is negative
    @Test(expected = MathArithmeticException.class)
    public void testFractionNegativeDenominator() {
        Fraction fraction = new Fraction(1, -2);
    }

    // Add more test cases for other methods...

}