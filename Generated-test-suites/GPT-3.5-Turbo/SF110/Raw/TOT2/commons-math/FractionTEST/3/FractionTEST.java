import org.junit.Test;

import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructor() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructorZeroDenominator() {
        try {
            Fraction fraction = new Fraction(1, 0);
            fail("Expected MathArithmeticException for zero denominator");
        } catch (MathArithmeticException e) {
            // Pass
        }
    }

    @Test
    public void testFractionConstructorNegativeDenominator() {
        Fraction fraction = new Fraction(-1, 2);
        assertEquals(-1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    // Add more test cases for other methods as required

}