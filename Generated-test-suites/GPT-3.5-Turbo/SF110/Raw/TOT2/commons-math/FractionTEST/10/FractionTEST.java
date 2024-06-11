import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructor() {
        Fraction testFraction = new Fraction(3.5);
        assertEquals(7, testFraction.getNumerator());
        assertEquals(2, testFraction.getDenominator());
    }

    @Test
    public void testFractionAbs() {
        Fraction testFraction = new Fraction(-3, 4);
        Fraction absFraction = testFraction.abs();
        assertEquals(3, absFraction.getNumerator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction firstFraction = new Fraction(3, 4);
        Fraction secondFraction = new Fraction(2, 3);
        assertEquals(1, firstFraction.compareTo(secondFraction));
    }

    // Add more test cases for other methods

}