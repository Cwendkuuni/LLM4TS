import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructor() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 4);
        Fraction result = fraction1.add(fraction2);
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testFractionDivideByZero() {
        try {
            Fraction fraction = new Fraction(5, 0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-2, 3);
        Fraction absFraction = fraction.abs();
        assertEquals(2, absFraction.getNumerator());
        assertEquals(3, absFraction.getDenominator());
    }

    @Test
    public void testFractionToString() {
        Fraction fraction = new Fraction(5, 9);
        assertEquals("5 / 9", fraction.toString());
    }

    @Test
    public void testFractionGetReducedFraction() {
        Fraction fraction = Fraction.getReducedFraction(20, 25);
        assertEquals(4, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }
}